package com.airhacks.gatelink.signature.control;

import java.security.interfaces.ECPrivateKey;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;

//https://web.dev/articles/push-notifications-web-push-protocol
public class JsonWebSignature {

    /**
     * ...The exp value is the expiration of the JWT, this prevent snoopers from
     * being able to re-use a JWT if they intercept it.
     * The expiration is a timestamp in seconds and must be no longer 24 hours.
     * 
     * @return 12h in future in seconds
     */
    static long in12h() {
        return (System.currentTimeMillis() / 1000) + 12 * 60 * 60;
    }

    public static String create(ECPrivateKey privateKey, String subject, String audience) {
        return Jwt.audience(audience)
                .subject(subject)
                .expiresAt(in12h())
                .jws()
                .algorithm(SignatureAlgorithm.ES256)
                .sign(privateKey);
    }
}
