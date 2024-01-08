package com.airhacks.gatelink.keymanagement.control;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import com.airhacks.gatelink.keymanagement.boundary.KeyGenerationException;
import com.airhacks.gatelink.keymanagement.entity.JavaSEServerKeys;

public class JavaSEKeyGenerator {

    public JavaSEServerKeys generateVapidKeys() {

        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("EC");

            keyPairGenerator.initialize(256);
            var serverKey = keyPairGenerator.generateKeyPair();
            var privateKey = (ECPrivateKey) serverKey.getPrivate();
            var publicKey = (ECPublicKey) serverKey.getPublic();
            return new JavaSEServerKeys(privateKey, publicKey);

        } catch ( NoSuchAlgorithmException  ex) {
            throw new KeyGenerationException(ex.getMessage());
        }
    }    
    
}
