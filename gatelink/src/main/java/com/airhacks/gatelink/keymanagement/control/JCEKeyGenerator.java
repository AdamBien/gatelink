package com.airhacks.gatelink.keymanagement.control;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.KeyPair;
import java.security.spec.ECGenParameterSpec;

import com.airhacks.gatelink.keymanagement.boundary.KeyGenerationException;
import com.airhacks.gatelink.keymanagement.entity.JCEServerKeys;

public interface JCEKeyGenerator {

    public static JCEServerKeys generateVapidKeys() {

        try {
            var parameterSpec = new ECGenParameterSpec("secp256r1");
            var keyPairGenerator = KeyPairGenerator.getInstance("EC");

            keyPairGenerator.initialize(parameterSpec);
            var serverKey = keyPairGenerator.generateKeyPair();
            var privateKey = (ECPrivateKey) serverKey.getPrivate();
            var publicKey = (ECPublicKey) serverKey.getPublic();
            return new JCEServerKeys(privateKey, publicKey);

        } catch ( NoSuchAlgorithmException | InvalidAlgorithmParameterException  ex) {
            throw new KeyGenerationException(ex.getMessage());
        }
    }    

    public static KeyPair generateEphemeralKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        var parameterSpec = new ECGenParameterSpec("secp256r1");
        var keyPairGenerator = KeyPairGenerator.getInstance("EC");

        keyPairGenerator.initialize(parameterSpec);
        return keyPairGenerator.generateKeyPair();
}
    
}
