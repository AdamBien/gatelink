
package com.airhacks.gatelink.keymanagement.entity;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 *
 * @author airhacks.com
 */
public record JavaSEServerKeys(ECPrivateKey privateKey,ECPublicKey publicKey) {

    public byte[] getPrivateKeyAsBytes() {
        return privateKey.getS().toByteArray();
    }

    public byte[] getUncompressedPublicKey() {
        //return new byte[0];
        //return publicKey.getQ().getEncoded(false);
        return publicKey.getEncoded();
    }

    public String getBase64URLEncodedPrivateKeyWithoutPadding() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(getPrivateKeyAsBytes());
    }

    public String getBase64URLEncodedPublicKeyWithoutPadding() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(getUncompressedPublicKey());
    }

    public String getBase64PublicKeyWithoutPadding() {
        return Base64.getEncoder().withoutPadding().encodeToString(getUncompressedPublicKey());
    }

    public ECPrivateKey getPrivateKey() {
        return privateKey;
    }

    public ECPublicKey getPublicKey() {
        return publicKey;
    }

    public void logKeys() {
        System.out.println("-----");
        System.out.println(toJson());
        System.out.println("-----");
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder().
                add("publicKey", getBase64URLEncodedPublicKeyWithoutPadding()).
                add("privateKey", getBase64URLEncodedPrivateKeyWithoutPadding()).
                build();
    }

}
