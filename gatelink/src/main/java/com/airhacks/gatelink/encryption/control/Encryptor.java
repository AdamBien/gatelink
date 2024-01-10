
package com.airhacks.gatelink.encryption.control;

import com.airhacks.gatelink.Control;
import com.airhacks.gatelink.bytes.control.Bytes;
import com.airhacks.gatelink.keymanagement.entity.BCServerKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.eclipse.microprofile.metrics.annotation.Metered;

/**
 *
 * @author airhacks.com
 */
@Control
public class Encryptor {

    public static final int SHA_256_LENGTH = 32;

    private final int TAG_SIZE = 16;

    /**
     * 
     * ecdh_secret = ECDH(as_private, ua_public)
     * auth_secret = <from user agent>
     * salt = random(16)
     * Checkout: https://www.rfc-editor.org/rfc/rfc8291.html
     * 
     * @param notification
     * @param keys
     * @param ephemeralPublicKey
     * @param ephemeralPrivateKey
     * @param salt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    @Metered
    public byte[] encrypt(Notification notification, BCServerKeys keys, ECPublicKey ephemeralPublicKey,
            ECPrivateKey ephemeralPrivateKey, byte[] salt)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        ECPublicKey browserKey = notification.getPublicKey();
        byte[] secret = getKeyAgreement(browserKey, ephemeralPrivateKey);
        byte[] context = Bytes.add(Bytes.getBytes("P-256"), new byte[1], getPublicKeyAsBytes(browserKey), getPublicKeyAsBytes(ephemeralPublicKey));

        secret = hmacKeyDerivation(secret, notification.getAuthAsBytes(), buildInfo("auth", new byte[0]),
                SHA_256_LENGTH);

        byte[] keyInfo = buildInfo("aesgcm", context);
        byte[] nonceInfo = buildInfo("nonce", context);

        byte[] key = hmacKeyDerivation(secret, salt, keyInfo, 16);
        byte[] nonce = hmacKeyDerivation(secret, salt, nonceInfo, 12);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        GCMParameterSpec params = new GCMParameterSpec(TAG_SIZE * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), params);

        byte[] twoBytes = cipher.update(new byte[2]);
        byte[] encryptedMessage = cipher.doFinal(notification.getMessageAsBytes());
        byte[] paddedCipherText = Bytes.add(twoBytes, encryptedMessage);
        return paddedCipherText;
    }

    byte[] getKeyAgreement(ECPublicKey browserKey, ECPrivateKey ephemeralPrivateKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(ephemeralPrivateKey);
        keyAgreement.doPhase(browserKey, true);
        return keyAgreement.generateSecret();
    }

    static byte[] buildInfo(String type, byte[] context) {
        ByteBuffer buffer = ByteBuffer.allocate(19 + type.length() + context.length);
        buffer.put(Bytes.getBytes("Content-Encoding: "), 0, 18);
        buffer.put(Bytes.getBytes(type), 0, type.length());
        buffer.put(new byte[1], 0, 1);
        buffer.put(context, 0, context.length);
        return buffer.array();
    }



    static byte[] hmacKeyDerivation(byte[] ikm, byte[] salt, byte[] info, int length) {

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(ikm, salt, info));
        byte[] okm = new byte[length];
        hkdf.generateBytes(okm, 0, length);
        return okm;
    }

    public static ECNamedCurveParameterSpec getCurveParameterSpec() {
        return ECNamedCurveTable.getParameterSpec("prime256v1");
    }

    // todo javadoc
    static byte[] getPublicKeyAsBytes(ECPublicKey publicKey) {
        byte[] bytes = publicKey.getQ().getEncoded(false);
        var length = Bytes.unsignedIntToBytes(bytes.length);
        return Bytes.add(length, bytes);
    }

}
