
package com.airhacks.gatelink.keymanagement.entity;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.Base64;

import com.airhacks.gatelink.bytes.control.ByteOperations;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Elliptic Curve key pair convenience methods
 * to return compressed and decompressed represaentations of the public key
 * @author airhacks.com
 */
public record ECKeys(ECPrivateKey privateKey,ECPublicKey publicKey) {

    public byte[] getPrivateKeyAsBytes() {
        return privateKey.getS().toByteArray();
    }

    public byte[] getUncompressedPublicKey() {
        var ecPoint = publicKey.getW();
        return decompressedRepresentation(ecPoint);
    }


    public static byte[] decompressedRepresentation(ECPublicKey publicKey) {
        return decompressedRepresentation(publicKey.getW());
    }

    /**
     * The length of the public key is: 65 hex digits (bytes)
     * @param ecPoint
     * @return
     */
    public static byte[] decompressedRepresentation(ECPoint ecPoint) {
        var xArray = ByteOperations.stripLeadingZeros(ecPoint.getAffineX().toByteArray());
        var yArray = ByteOperations.stripLeadingZeros(ecPoint.getAffineY().toByteArray());
        var result = new byte[65];
        result[0] = 4;
        System.arraycopy(xArray, 0, result, 1, xArray.length);
        System.arraycopy(yArray, 0, result, 33, yArray.length);
        return result;
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
