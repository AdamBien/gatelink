
package com.airhacks.gatelink.encryption.boundary;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.microprofile.metrics.annotation.Metered;

import com.airhacks.gatelink.encryption.control.JCEEncryptor;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.keymanagement.control.JCEKeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

/**
 * https://docs.oracle.com/en/java/javase/21/security/oracle-providers.html#GUID-091BF58C-82AB-4C9C-850F-1660824D5254
 * 
 * @author airhacks.com
 */
public class EncryptionService {

    @Inject
    JCEEncryptor encryptor;

    SecureRandom random;

    @PostConstruct
    public void init() {
        this.random = new SecureRandom();
    }

    @Metered
    public byte[] getNextSalt() {
        /*
         * https://www.rfc-editor.org/rfc/rfc8291.html
         * 3.4. Encryption Summary
         * salt = random(16)
         */
        byte[] salt = new byte[16];
        this.random.nextBytes(salt);
        return salt;
    }

    public EncryptedContent encrypt(Notification notification, ECKeys keys)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        var ephemeralLocalKeys = JCEKeyGenerator.generateEphemeralKeys();
        var serverEphemeralPublic = (ECPublicKey) ephemeralLocalKeys.getPublic();
        var ephemeralPrivateKey = (ECPrivateKey) ephemeralLocalKeys.getPrivate();
        var salt = this.getNextSalt();
        var encryptedContent = this.encryptor.encrypt(notification, keys, serverEphemeralPublic, ephemeralPrivateKey,
                salt);
        return new EncryptedContent(encryptedContent, salt, serverEphemeralPublic);
    }
}
