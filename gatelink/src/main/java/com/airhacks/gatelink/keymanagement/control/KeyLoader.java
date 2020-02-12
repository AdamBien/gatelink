
package com.airhacks.gatelink.keymanagement.control;

import com.airhacks.gatelink.encryption.control.Encryptor;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/**
 *
 * @author airhacks.com
 */
public interface KeyLoader {

    public static ECPublicKey loadUrlEncodedPublicKey(String content) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] decodedPublicKey = Base64.getUrlDecoder().decode(content);
        return loadPublicKeyFromBytes(decodedPublicKey);
    }

    static ECPublicKey loadPublicKeyFromBytes(byte[] keyContent) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyFactory keyFactory = getKeyFactory();
        ECParameterSpec parameterSpec = Encryptor.getCurveParameterSpec();
        ECCurve curve = parameterSpec.getCurve();
        ECPoint point = curve.decodePoint(keyContent);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, parameterSpec);
        return (ECPublicKey) keyFactory.generatePublic(pubSpec);

    }

    public static ECPrivateKey loadURLEncodedPrivateKey(String encodedPrivateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPrivateKey = Base64.getUrlDecoder().decode(encodedPrivateKey);
        BigInteger s = new BigInteger(1, decodedPrivateKey);
        ECParameterSpec parameterSpec = Encryptor.getCurveParameterSpec();
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, parameterSpec);
        KeyFactory keyFactory = getKeyFactory();

        return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }

    static KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyFactory.getInstance("ECDH", "BC");
    }

}
