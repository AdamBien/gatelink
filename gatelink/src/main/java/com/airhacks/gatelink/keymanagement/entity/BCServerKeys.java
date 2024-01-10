
package com.airhacks.gatelink.keymanagement.entity;

import java.util.Base64;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 *
 * @author airhacks.com
 */
public record BCServerKeys(ECPrivateKey privateKey,ECPublicKey publicKey) {

    public byte[] getPrivateKeyAsBytes() {
        return privateKey.getD().toByteArray();
    }

    public byte[] getUncompressedPublicKey() {
        return publicKey.getQ().getEncoded(false);
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
        return Json.createObjectBuilder()
                .add("publicKey", getBase64URLEncodedPublicKeyWithoutPadding())
                .add("privateKey", getBase64URLEncodedPrivateKeyWithoutPadding())
                .build();
    }

}
