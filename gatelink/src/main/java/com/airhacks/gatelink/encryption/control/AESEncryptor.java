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

public interface AESEncryptor {

    int TAG_SIZE = 16;


    static byte[] encryptWithAES(byte[] key, byte[] content, byte[] nonce)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        var params = new GCMParameterSpec(TAG_SIZE * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), params);
        var twoBytes = cipher.update(new byte[2]);
        var encryptedMessage = cipher.doFinal(content);
        return ByteOperations.concat(twoBytes, encryptedMessage);
    }

}
