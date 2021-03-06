
package com.airhacks.gatelink.encryption.control;

import com.airhacks.gatelink.Control;
import com.airhacks.gatelink.keymanagement.entity.ServerKeys;
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

    @Metered
    public byte[] encrypt(Notification notification, ServerKeys keys, ECPublicKey ephemeralPublicKey, ECPrivateKey ephemeralPrivateKey, byte[] salt) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        ECPublicKey browserKey = notification.getPublicKey();
        byte[] secret = getKeyAgreement(browserKey, ephemeralPrivateKey);
        byte[] context = add(getBytes("P-256"), new byte[1], convert(browserKey), convert(ephemeralPublicKey));

        secret = hmacKeyDerivation(secret, notification.getAuthAsBytes(), buildInfo("auth", new byte[0]), SHA_256_LENGTH);

        byte[] keyInfo = buildInfo("aesgcm", context);
        byte[] nonceInfo = buildInfo("nonce", context);

        byte[] key = hmacKeyDerivation(secret, salt, keyInfo, 16);
        byte[] nonce = hmacKeyDerivation(secret, salt, nonceInfo, 12);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        GCMParameterSpec params = new GCMParameterSpec(TAG_SIZE * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), params);

        byte[] twoBytes = cipher.update(new byte[2]);
        byte[] encryptedMessage = cipher.doFinal(notification.getMessageAsBytes());
        byte[] paddedCipherText = add(twoBytes, encryptedMessage);
        return paddedCipherText;
    }

    byte[] getKeyAgreement(ECPublicKey browserKey, ECPrivateKey ephemeralPrivateKey) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(ephemeralPrivateKey);
        keyAgreement.doPhase(browserKey, true);
        return keyAgreement.generateSecret();
    }

    static byte[] buildInfo(String type, byte[] context) {
        ByteBuffer buffer = ByteBuffer.allocate(19 + type.length() + context.length);
        buffer.put(getBytes("Content-Encoding: "), 0, 18);
        buffer.put(getBytes(type), 0, type.length());
        buffer.put(new byte[1], 0, 1);
        buffer.put(context, 0, context.length);
        return buffer.array();
    }

    static byte[] getBytes(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unsupported encoding", ex);
        }
   }

    static byte[] hmacKeyDerivation(byte[] ikm, byte[] salt, byte[] info, int length) {

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(ikm, salt, info));
        byte[] okm = new byte[length];
        hkdf.generateBytes(okm, 0, length);
        return okm;
    }

    static byte[] add(byte[]  
        ...arrays){
        try ( ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            for (byte[] array : arrays) {
                if (array == null) {
                    continue;
                }
                stream.write(array);
            }
            return stream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Array addition failed", ex);

        }
    }
    public static ECNamedCurveParameterSpec getCurveParameterSpec() {
        return ECNamedCurveTable.getParameterSpec("prime256v1");
    }

    static byte[] convert(ECPublicKey publicKey) {
        byte[] bytes = publicKey.getQ().getEncoded(false);
        return add(unsignedIntToBytes(bytes.length), bytes);
    }

    static byte[] unsignedIntToBytes(int number) {
        byte result[] = new byte[2];
        result[1] = (byte) (number & 0xFF);
        result[0] = (byte) (number >> 8);
        return result;
    }


}
