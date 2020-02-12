
package com.airhacks.gatelink.encryption.entity;

import java.security.PublicKey;
import java.util.Base64;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 *
 * @author airhacks.com
 */
public class EncryptedContent {

    private byte[] encryptedContent;
    private byte[] salt;
    private ECPublicKey ephemeralPublicKey;

    public EncryptedContent(byte[] encryptedContent, byte[] salt, ECPublicKey ephemeralPublicKey) {
        this.encryptedContent = encryptedContent;
        this.salt = salt;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getEncodedSalt() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(this.getSalt());
    }

    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }

    public String getEncodedEphemeralPublicKey() {
        byte[] encoded = this.ephemeralPublicKey.getQ().getEncoded(false);
        return Base64.getUrlEncoder().encodeToString(encoded);
    }


}
