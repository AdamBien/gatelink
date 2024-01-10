
package com.airhacks.gatelink.keymanagement.entity;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.Arrays;
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
        var ecPoint = publicKey.getW();
        return decompressedRepresentation(ecPoint);
    }
    /**
     * The length of the public key is: 65 hex digits (bytes)
     * @param ecPoint
     * @return
     */
    static byte[] decompressedRepresentation(ECPoint ecPoint) {
        var xArray = stripLeadingZeros(ecPoint.getAffineX().toByteArray());
        var yArray = stripLeadingZeros(ecPoint.getAffineY().toByteArray());
        var result = new byte[65];
        result[0] = 4;
        System.arraycopy(xArray, 0, result, 1, xArray.length);
        System.arraycopy(yArray, 0, result, 33, yArray.length);
        return result;

    } 

    static byte[] stripLeadingZeros(byte[] data) {
        int nonZeroIndex = 0;
        while (nonZeroIndex < data.length && data[nonZeroIndex] == 0) 
            nonZeroIndex++;
        return Arrays.copyOfRange(data, nonZeroIndex, data.length);
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
