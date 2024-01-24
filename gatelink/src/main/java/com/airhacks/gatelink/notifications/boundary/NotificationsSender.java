
package com.airhacks.gatelink.notifications.boundary;

import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.jose4j.lang.JoseException;

import com.airhacks.gatelink.Boundary;
import com.airhacks.gatelink.encryption.boundary.EncryptionService;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.keymanagement.boundary.InMemoryKeyStore;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;
import com.airhacks.gatelink.log.boundary.Tracer;
import com.airhacks.gatelink.notifications.control.PushServiceClient;
import com.airhacks.gatelink.notifications.control.PushServiceClient.NotificationResponse;
import com.airhacks.gatelink.signature.control.JsonWebSignature;
import com.airhacks.gatelink.subscriptions.control.InMemorySubscriptionsStore;

import jakarta.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Boundary
public class NotificationsSender {

    @Inject
    InMemorySubscriptionsStore store;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    @Inject
    InMemoryKeyStore keyStore;

    @Inject
    EncryptionService encryptionService;

    @Inject
    Tracer tracer;


    /**
     * server contact person, push service will use the contact, in the case of
     * denial of service attacks etc.
     */
    @Inject
    @ConfigProperty(name = "subject", defaultValue = "mailto:admin@airhacks.com")
    String subject;

    @Counted(absolute = true, name = "forwardedMessages")
    public void send(String message) {
        tracer.log("Sending " + message);
        ECKeys serverKeys = this.keyStore.getKeys();

        this.store.all()
                .stream()
                .map(s -> new Notification(s, message))
                .forEach(n -> this.send(n, serverKeys));
    }

    public boolean send(Notification notification, ECKeys serverKeys) {
        try {
            var encryptedContent = this.encryptionService.encrypt(notification, serverKeys);
            String endpoint = notification.getEndpoint();
            tracer.log("Sending to: " + endpoint);
            var notificationStatus = this.sendEncryptedMessage(serverKeys, endpoint, encryptedContent);
            registry.counter("responses_" + notificationStatus.status()).inc();
            return notificationStatus.isSuccessful();
        } catch (JoseException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException
                | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new IllegalStateException("Cannot encrypt", ex);
        }
    }

    public NotificationResponse sendEncryptedMessage(ECKeys serverKeys, String endpoint, EncryptedContent encryptedContent)
            throws JoseException {
        tracer.log("Sending to endpoint " + endpoint);
        var audience = extractAud(endpoint);
        var salt = encryptedContent.getEncodedSalt();
        var ephemeralPublicKey = encryptedContent.getEncodedEphemeralPublicKey();
        var vapidPublicKey = serverKeys.getBase64URLEncodedPublicKeyWithoutPadding();
        tracer.log("audience: " + audience);
        var authorizationToken = JsonWebSignature.create(serverKeys.getPrivateKey(), subject, audience);
        registry.counter(audience).inc();
        return PushServiceClient.sendNotification(endpoint, salt, ephemeralPublicKey, vapidPublicKey, authorizationToken,encryptedContent.encryptedContent());
    }

    static String extractAud(String endpoint) {
            var uri = URI.create(endpoint);
            var host = uri.getHost();
            var protocol = uri.getScheme();
            if(uri == null || host == null)
                return endpoint;
            return String.format("%s://%s", protocol, host);       
    }

}
