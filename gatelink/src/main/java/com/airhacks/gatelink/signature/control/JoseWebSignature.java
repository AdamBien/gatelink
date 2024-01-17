package com.airhacks.gatelink.signature.control;

import java.security.interfaces.ECPrivateKey;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

public interface JoseWebSignature {
    
    public static String create(ECPrivateKey privateKeys, String subject, String audience) {
        JwtClaims claims = new JwtClaims();
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(12 * 60);
        claims.setSubject(subject);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setHeader("typ", "JWT");
        jws.setHeader("alg", "ES256");
        jws.setPayload(claims.toJson());
        jws.setKey(privateKeys);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new IllegalStateException("JOSE serialization exception ",e);
        }
    }
}
