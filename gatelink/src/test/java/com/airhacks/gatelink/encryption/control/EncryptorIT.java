/*
 */
package com.airhacks.gatelink.encryption.control;

import com.airhacks.gatelink.encryption.control.Encryptor;
import com.airhacks.gatelink.EncryptionTestEnvironment;
import com.airhacks.gatelink.encryption.boundary.EncryptionService;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.notifications.boundary.Notification;
import com.airhacks.gatelink.notifications.boundary.NotificationsSender;
import com.airhacks.gatelink.notifications.boundary.NotificationsSenderIT;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import javax.ws.rs.core.Response;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class EncryptorIT extends EncryptionTestEnvironment {

    private Encryptor cut;
    private ECPublicKey ephemeralPublic;
    private ECPrivateKey ephemeralPrivate;
    private byte[] salt;
    private KeyPair ephemeralKeys;

    @BeforeEach
    public void initialize() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidAlgorithmParameterException {
        super.init("chrome");
        EncryptionService service = new EncryptionService();
        service.init();
        this.ephemeralKeys = service.generateEphemeralKeys();
        this.ephemeralPublic = (ECPublicKey) ephemeralKeys.getPublic();
        this.ephemeralPrivate = (ECPrivateKey) ephemeralKeys.getPrivate();
        this.salt = service.getNextSalt();
        this.cut = new Encryptor();
    }



    @Test
    public void encryptAndSend() throws Exception {
        Notification notification = this.serverKeysWithSubscription.getNotification("hey duke");
        byte encrypted[] = this.cut.encrypt(notification, this.serverKeysWithSubscription.getServerKeys(), ephemeralPublic, ephemeralPrivate, this.salt);

        NotificationsSender notificationsSender = new NotificationsSenderIT().getCut();
        EncryptedContent encryptedContent = new EncryptedContent(encrypted, salt, ephemeralPublic);

        Response response = notificationsSender.sendEncryptedMessage(this.serverKeysWithSubscription.getServerKeys(), notification.getEndpoint(), encryptedContent);
        System.out.println("response = " + response.getStatus());
    }

}
