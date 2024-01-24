package com.airhacks.gatelink.encryption.control;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import javax.crypto.KeyAgreement;
/**
 * Implementation of the Elliptic Curve Diffie-Hellman (ECDH) key exchange / key agreement protol
 * 
 */
public interface KeyExchange {

    /**
     * Provides key agreement betweeen two parties
     * https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keyagreement-algorithms
     * 
     * Keyagreement algorithm is described in the spec: https://datatracker.ietf.org/doc/html/rfc3278
     */
    static byte[] getKeyAgreement(ECPublicKey browserKey, ECPrivateKey ephemeralPrivateKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        var keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(ephemeralPrivateKey);
        /*
         * For each of the correspondents in the key exchange, doPhase needs to be
         * called. For example, if this key exchange is with one other party, doPhase
         * needs to be called once, with the lastPhase flag set to true.
         */
        keyAgreement.doPhase(browserKey, true);
        return keyAgreement.generateSecret();
    }

}
