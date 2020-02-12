
package com.airhacks.gatelink.keymanagement.entity;

import java.util.Base64;
import javax.json.Json;
import javax.json.JsonObject;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 *
 * @author airhacks.com
 */
public class ServerKeys {

    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;

    public ServerKeys(ECPrivateKey privateKey, ECPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

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
        return Json.createObjectBuilder().
                add("publicKey", getBase64URLEncodedPublicKeyWithoutPadding()).
                add("privateKey", getBase64URLEncodedPrivateKeyWithoutPadding()).
                build();
    }

}
