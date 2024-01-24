package com.airhacks.gatelink.encryption.control;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.airhacks.gatelink.bytes.control.ByteOperations;

/**
 * AES encryption with GCM mode
 * https://www.ietf.org/rfc/rfc5116.txt
 * 
 * Recommendation for Block
 * Cipher Modes of Operation:
 * Galois/Counter Mode (GCM)
 * and GMAC:
 * https://csrc.nist.gov/pubs/sp/800/38/d/final
 * (https://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-38d.pdf)
 */
public interface AESEncryptor {

    int AUTHENTICATION_TAG_LENGTH = 16;

    /**
     * Encrypts the content with AES in Galois/Counter Mode (GCM) mode
     */
    static byte[] encryptWithAES(byte[] key, byte[] content, byte[] nonce)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        var params = new GCMParameterSpec(AUTHENTICATION_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), params);
        var twoBytes = cipher.update(new byte[2]);
        var encryptedMessage = cipher.doFinal(content);
        return ByteOperations.concat(twoBytes, encryptedMessage);
    }

}
