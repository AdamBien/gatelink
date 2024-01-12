
package com.airhacks.gatelink.encryption.control;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.microprofile.metrics.annotation.Metered;

import com.airhacks.gatelink.Control;
import com.airhacks.gatelink.bytes.control.Bytes;
import com.airhacks.gatelink.keymanagement.entity.JCEServerKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;

/**
 *
 * @author airhacks.com
 */
@Control
public class JCEEncryptor {

    public static final int SHA_256_LENGTH = 32;

    private final int TAG_SIZE = 16;

    /**
     * 
     * ecdh_secret = ECDH(as_private, ua_public)
     * auth_secret = <from user agent>
     * salt = random(16)
     * Checkout: https://www.rfc-editor.org/rfc/rfc8291.html
     */
    @Metered
    public byte[] encrypt(Notification notification, JCEServerKeys keys, ECPublicKey ephemeralPublicKey,
            ECPrivateKey ephemeralPrivateKey, byte[] salt)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        var browserKey = notification.getJCEPublicKey();
        byte[] secret = KeyExchange.getKeyAgreement(browserKey, ephemeralPrivateKey);
        byte[] context = Bytes.add(Bytes.getBytes("P-256"), new byte[1], getPublicKeyAsBytes(browserKey), getPublicKeyAsBytes(ephemeralPublicKey));

        secret = HMacKeyDerivation.derive(secret, notification.getAuthAsBytes(), buildInfo("auth", new byte[0]),
                SHA_256_LENGTH);

        byte[] keyInfo = buildInfo("aesgcm", context);
        byte[] nonceInfo = buildInfo("nonce", context);

        byte[] key = HMacKeyDerivation.derive(secret, salt, keyInfo, 16);
        byte[] nonce = HMacKeyDerivation.derive(secret, salt, nonceInfo, 12);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        GCMParameterSpec params = new GCMParameterSpec(TAG_SIZE * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), params);

        byte[] twoBytes = cipher.update(new byte[2]);
        byte[] encryptedMessage = cipher.doFinal(notification.getMessageAsBytes());
        byte[] paddedCipherText = Bytes.add(twoBytes, encryptedMessage);
        return paddedCipherText;
    }


    static byte[] buildInfo(String type, byte[] context) {
        ByteBuffer buffer = ByteBuffer.allocate(19 + type.length() + context.length);
        buffer.put(Bytes.getBytes("Content-Encoding: "), 0, 18);
        buffer.put(Bytes.getBytes(type), 0, type.length());
        buffer.put(new byte[1], 0, 1);
        buffer.put(context, 0, context.length);
        return buffer.array();
    }




    public static ECGenParameterSpec getCurveGenParameterSpec() {
        return new ECGenParameterSpec("secp256r1");
    }

    public static ECParameterSpec getParameterSpec() {
        try {
            var algorithmParameters = AlgorithmParameters.getInstance("EC");
            algorithmParameters.init(getCurveGenParameterSpec());
            return algorithmParameters.getParameterSpec(ECParameterSpec.class);
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException e) {
                throw new IllegalStateException("Cannot create ECParameterSpec: " + e);
        }
    }

    // todo javadoc
    static byte[] getPublicKeyAsBytes(ECPublicKey publicKey) {
        byte[] bytes = JCEServerKeys.decompressedRepresentation(publicKey);
        var length = Bytes.unsignedIntToBytes(bytes.length);
        return Bytes.add(length, bytes);
    }

}
