
package com.airhacks.gatelink.notifications.boundary;

import java.net.MalformedURLException;
import java.net.URL;
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
import com.airhacks.gatelink.encryption.boundary.JCEEncryptionService;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.encryption.entity.JCEEncryptedContent;
import com.airhacks.gatelink.keymanagement.boundary.KeyStore;
import com.airhacks.gatelink.keymanagement.entity.BCServerKeys;
import com.airhacks.gatelink.keymanagement.entity.JCEServerKeys;
import com.airhacks.gatelink.log.boundary.Tracer;
import com.airhacks.gatelink.notifications.control.PushService;
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
    JCEEncryptionService encryptionService;

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
        JCEServerKeys serverKeys = this.keyStore.getKeys();

        this.store.all()
                .stream()
                .map(s -> new Notification(s, message))
                .forEach(n -> this.send(n, serverKeys));
    }

    public Response send(Notification notification, JCEServerKeys serverKeys) {
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

    public Response sendEncryptedMessage(JCEServerKeys serverKeys, String endpoint, JCEEncryptedContent encryptedContent)
            throws JoseException {
        tracer.log("Sending to endpoint " + endpoint);
        String audience = extractAud(endpoint);
        String salt = encryptedContent.getEncodedSalt();
        String ephemeralPublicKey = encryptedContent.getEncodedEphemeralPublicKey();
        String vapidPublicKey = serverKeys.getBase64URLEncodedPublicKeyWithoutPadding();

        JwtClaims claims = new JwtClaims();
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(12 * 60);
        claims.setSubject(this.subject);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setHeader("typ", "JWT");
        jws.setHeader("alg", "ES256");
        jws.setPayload(claims.toJson());
        jws.setKey(serverKeys.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
        registry.counter(audience).inc();
        return this.pushService.send(endpoint, salt, ephemeralPublicKey, vapidPublicKey, jws.getCompactSerialization(),
                encryptedContent.encryptedContent());
    }

    static String extractAud(String endpoint) {
        URL uri;
        try {
            uri = new URL(endpoint);
        } catch (MalformedURLException ex) {
            return endpoint;
        }
        String host = uri.getHost();
        String protocol = uri.getProtocol();
        return String.format("%s://%s", protocol, host);
    }

}
