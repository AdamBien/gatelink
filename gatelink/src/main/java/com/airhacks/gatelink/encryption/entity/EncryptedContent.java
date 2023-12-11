
package com.airhacks.gatelink.encryption.entity;

import java.security.PublicKey;
import java.util.Base64;
import org.bouncycastle.jce.interfaces.ECPublicKey;

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
        byte[] encoded = this.ephemeralPublicKey.getQ().getEncoded(false);
        return Base64.getUrlEncoder().encodeToString(encoded);
    }


}
