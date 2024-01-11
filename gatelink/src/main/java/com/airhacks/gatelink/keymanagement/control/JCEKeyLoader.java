
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
import java.util.Base64;

import com.airhacks.gatelink.bytes.control.Bytes;
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

    static ECPublicKey loadPublicKeyFromBytes(byte[] keyContent)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        /**
         * x and y coordinates have the same length (32). 
         * The first byte is the indicator. 0x4 means public key
         */
        int expectedLength = (65-1)/2;
        var indicator = keyContent[0];
        if(indicator != 4){
            throw new IllegalArgumentException("Parameter does not represent a public key");
        }
        System.out.println(" length " + keyContent.length);
        System.out.println(" first byte " + keyContent[0]);
        var parameterSpec = JCEEncryptor.getParameterSpec();
        KeyFactory kf = KeyFactory.getInstance("EC");
        var x = Bytes.fromUnsignedByteArray(keyContent, 1, expectedLength);
        var y = Bytes.fromUnsignedByteArray(keyContent, 1 + expectedLength, expectedLength);
        var w = new ECPoint(x, y);
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
