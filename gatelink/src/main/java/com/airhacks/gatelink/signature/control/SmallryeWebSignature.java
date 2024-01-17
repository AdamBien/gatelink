package com.airhacks.gatelink.signature.control;

import java.security.interfaces.ECPrivateKey;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;

public class SmallryeWebSignature {

    public static String create(ECPrivateKey privateKeys, String subject, String audience) {
        return Jwt.audience(audience)
                .subject(subject)
                .expiresAt(12 * 60)
                .jws()
                .algorithm(SignatureAlgorithm.ES256)
                .header("typ", "JWT")
                .header("alg", SignatureAlgorithm.ES256.getAlgorithm())
                .sign(privateKeys);
    }

}
