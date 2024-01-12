package com.airhacks.gatelink.encryption.control;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import javax.crypto.KeyAgreement;


public interface KeyExchange {

    /**
     * https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keyagreement-algorithms
     */
    static byte[] getKeyAgreement(ECPublicKey browserKey, ECPrivateKey ephemeralPrivateKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        var keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(ephemeralPrivateKey);
        keyAgreement.doPhase(browserKey, true);
        return keyAgreement.generateSecret();
    }
    
}
