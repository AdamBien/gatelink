/*
 */
package com.airhacks.gatelink.encryption.control;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.airhacks.gatelink.EncryptionTestEnvironment;
import com.airhacks.gatelink.encryption.boundary.EncryptionService;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.keymanagement.control.ECKeyGenerator;
import com.airhacks.gatelink.notifications.boundary.NotificationsSenderIT;

/**
 *
 * @author airhacks.com
 */
public class EncryptorIT extends EncryptionTestEnvironment {

    private JCEEncryptor cut;
    private ECPublicKey ephemeralPublic;
    private ECPrivateKey ephemeralPrivate;
    private byte[] salt;
    private KeyPair ephemeralKeys;

    @BeforeEach
    public void initialize() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidAlgorithmParameterException {
        super.init("chrome");
        EncryptionService service = new EncryptionService();
        service.init();
        this.ephemeralKeys = ECKeyGenerator.generate();
        this.ephemeralPublic = (ECPublicKey) ephemeralKeys.getPublic();
        this.ephemeralPrivate = (ECPrivateKey) ephemeralKeys.getPrivate();
        this.salt = service.getNextSalt();
        this.cut = new JCEEncryptor();
    }



    @Test
    public void encryptAndSend() throws Exception {
        var notification = this.serverKeysWithSubscription.getNotification("hey duke");
        byte encrypted[] = this.cut.encrypt(notification, this.serverKeysWithSubscription.getServerKeys(), ephemeralPublic, ephemeralPrivate, this.salt);

        var notificationsSender = new NotificationsSenderIT().getCut();
        var encryptedContent = new EncryptedContent(encrypted, salt, ephemeralPublic);

        var response = notificationsSender.sendEncryptedMessage(this.serverKeysWithSubscription.getServerKeys(), notification.getEndpoint(), encryptedContent);
        System.out.println("response = " + response.getStatus());
    }

}
