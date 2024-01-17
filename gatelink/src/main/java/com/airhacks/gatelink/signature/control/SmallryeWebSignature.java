package com.airhacks.gatelink.signature.control;

import java.security.interfaces.ECPrivateKey;


import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;

//https://web.dev/articles/push-notifications-web-push-protocol
public class SmallryeWebSignature {

    static long in12h(){
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
