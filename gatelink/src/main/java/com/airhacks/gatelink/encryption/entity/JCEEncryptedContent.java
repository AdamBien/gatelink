
package com.airhacks.gatelink.encryption.entity;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

import com.airhacks.gatelink.keymanagement.entity.JCEServerKeys;


/**
 *
 * @author airhacks.com
 */
public record JCEEncryptedContent(byte[] encryptedContent,byte[] salt,ECPublicKey ephemeralPublicKey){



    public String getEncodedSalt() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(this.salt);
    }

    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }

    public String getEncodedEphemeralPublicKey() {
        var decompressed = JCEServerKeys.decompressedRepresentation(ephemeralPublicKey);
        return Base64.getUrlEncoder().encodeToString(decompressed);
    }


}
