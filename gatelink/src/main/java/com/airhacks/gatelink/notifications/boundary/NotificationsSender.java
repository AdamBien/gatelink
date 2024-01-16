
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
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import com.airhacks.gatelink.Boundary;
import com.airhacks.gatelink.encryption.boundary.EncryptionService;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.keymanagement.boundary.KeyStore;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;
import com.airhacks.gatelink.log.boundary.Tracer;
import com.airhacks.gatelink.notifications.control.PushService;
import com.airhacks.gatelink.signature.control.WebSignature;
import com.airhacks.gatelink.subscriptions.control.SubscriptionsStore;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Boundary
public class NotificationsSender {

    @Inject
    SubscriptionsStore store;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    @Inject
    KeyStore keyStore;

    @Inject
    EncryptionService encryptionService;

    @Inject
    Tracer tracer;

    @Inject
    PushService pushService;

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

    public Response send(Notification notification, ECKeys serverKeys) {
        Response response = null;
        try {
            var encryptedContent = this.encryptionService.encrypt(notification, serverKeys);
            String endpoint = notification.getEndpoint();
            tracer.log("Sending to: " + endpoint);
            response = this.sendEncryptedMessage(serverKeys, endpoint, encryptedContent);
            registry.counter("responses_" + response.getStatus()).inc();
        } catch (JoseException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException
                | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new IllegalStateException("Cannot encrypt", ex);
        }
        return response;
    }

    public Response sendEncryptedMessage(ECKeys serverKeys, String endpoint, EncryptedContent encryptedContent)
            throws JoseException {
        tracer.log("Sending to endpoint " + endpoint);
        var audience = extractAud(endpoint);
        var salt = encryptedContent.getEncodedSalt();
        var ephemeralPublicKey = encryptedContent.getEncodedEphemeralPublicKey();
        var vapidPublicKey = serverKeys.getBase64URLEncodedPublicKeyWithoutPadding();
        var serializedMessage = WebSignature.create(serverKeys.getPrivateKey(), vapidPublicKey, audience);
        registry.counter(audience).inc();
        return this.pushService.send(endpoint, salt, ephemeralPublicKey, vapidPublicKey, serializedMessage,
                encryptedContent.encryptedContent());
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
