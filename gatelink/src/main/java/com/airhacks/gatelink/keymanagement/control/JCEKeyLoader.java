
package com.airhacks.gatelink.keymanagement.control;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import com.airhacks.gatelink.encryption.control.JCEEncryptor;


/**
 *
 * @author airhacks.com
 */
public interface JCEKeyLoader {

    public static ECPublicKey loadUrlEncodedPublicKey(String content) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] decodedPublicKey = Base64.getUrlDecoder().decode(content);
        return loadPublicKeyFromBytes(decodedPublicKey);
    }

    static ECPublicKey loadPublicKeyFromBytes(byte[] keyContent) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        var parameterSpec = JCEEncryptor.getParameterSpec();
        KeyFactory kf = KeyFactory.getInstance("EC");
        byte[] x = Arrays.copyOfRange(keyContent, 0, keyContent.length/2);
        byte[] y = Arrays.copyOfRange(keyContent, keyContent.length/2, keyContent.length);
        var w = new ECPoint(new BigInteger(1,x), new BigInteger(1,y));
        return (ECPublicKey) kf.generatePublic(new ECPublicKeySpec(w, parameterSpec));
    }

    public static ECPrivateKey loadURLEncodedPrivateKey(String encodedPrivateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPrivateKey = Base64.getUrlDecoder().decode(encodedPrivateKey);
        BigInteger s = new BigInteger(1, decodedPrivateKey);
        var parameterSpec = JCEEncryptor.getParameterSpec();
        var privateKeySpec = new ECPrivateKeySpec(s, parameterSpec);
        KeyFactory keyFactory = getKeyFactory();
        return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }

    static KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyFactory.getInstance("EC");
    }

}
