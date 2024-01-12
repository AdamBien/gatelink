
package com.airhacks.gatelink.encryption.entity;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

import com.airhacks.gatelink.keymanagement.entity.ECKeys;


/**
 *
 * @author airhacks.com
 */
public record EncryptedContent(byte[] encryptedContent,byte[] salt,ECPublicKey ephemeralPublicKey){



    public String getEncodedSalt() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(this.salt);
    }

    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }

    public String getEncodedEphemeralPublicKey() {
        byte[] encoded = ECKeys.decompressedRepresentation(this.ephemeralPublicKey);
        return Base64.getUrlEncoder().encodeToString(encoded);
    }


}
