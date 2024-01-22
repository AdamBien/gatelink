
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
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.microprofile.metrics.annotation.Metered;

import com.airhacks.gatelink.Control;
import com.airhacks.gatelink.bytes.control.ByteOperations;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;
import com.airhacks.gatelink.log.boundary.Tracer;
import com.airhacks.gatelink.notifications.boundary.Notification;

/**
 *
 * @author airhacks.com
 */
@Control
public class EncryptionFlow {

    public static final int SHA_256_LENGTH = 32;

  
    /**
     * 
     * ecdh_secret = ECDH(as_private, ua_public)
     * auth_secret = <from user agent>
     * salt = random(16)
     * Checkout: https://www.rfc-editor.org/rfc/rfc8291.html
     */
    @Metered
    public byte[] encrypt(Notification notification, ECKeys keys, ECPublicKey ephemeralPublicKey,
            ECPrivateKey ephemeralPrivateKey, byte[] salt)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        var browserKey = notification.getJCEPublicKey();
        // Use HKDF to combine the ECDH and authentication secrets
        // HKDF-Extract(salt=auth_secret, IKM=ecdh_secret)
        var secret = KeyExchange.getKeyAgreement(browserKey, ephemeralPrivateKey);
        var authInfo = info("auth", new byte[0]);
        secret = HMacKeyDerivation.derive(secret, notification.getAuthAsBytes(), authInfo,
                SHA_256_LENGTH);

        var context = ByteOperations.concat("P-256".getBytes(), new byte[1], getPublicKeyAsBytes(browserKey),
                getPublicKeyAsBytes(ephemeralPublicKey));

        // cek_info = "Content-Encoding: aes128gcm" || 0x00
        var contentEncryptionKeyInfo = info("aesgcm", context);
        //nonce_info = "Content-Encoding: nonce" || 0x00
        var nonceInfo = info("nonce", context);

        var key = HMacKeyDerivation.derive(secret, salt, contentEncryptionKeyInfo, 16);
        var nonce = HMacKeyDerivation.derive(secret, salt, nonceInfo, 12);

        var content = notification.getMessageAsBytes();
        Tracer.debug("AES parameters (key,content,nonce)", key,content,nonce);
        var aesEncrypted = AESEncryptor.encryptWithAES(key, content, nonce);
        Tracer.debug("AES encrypted content",aesEncrypted);
        return aesEncrypted;

    }

    static byte[] info(String type, byte[] context) {
        var buffer = ByteBuffer.allocate(19 + type.length() + context.length);
        buffer.put("Content-Encoding: ".getBytes(), 0, 18);
        buffer.put(type.getBytes(), 0, type.length());
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
        var bytes = ECKeys.decompressedRepresentation(publicKey);
        var length = ByteOperations.unsignedIntToBytes(bytes.length);
        return ByteOperations.concat(length, bytes);
    }

}
