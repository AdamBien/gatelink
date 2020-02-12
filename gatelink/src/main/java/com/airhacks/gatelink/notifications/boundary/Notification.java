
package com.airhacks.gatelink.notifications.boundary;

import com.airhacks.gatelink.subscriptions.entity.Subscription;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 *
 * @author airhacks.com
 */
public class Notification {

    private final Subscription subscription;
    private final String message;

    public Notification(Subscription subscription, String message) {
        this.subscription = subscription;
        this.message = message;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public String getMessage() {
        return message;
    }

    public ECPublicKey getPublicKey() {
        try {
            return this.subscription.getP256dhAsPublicKey();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("Key cannot be loaded", ex);
        }
    }

    public byte[] getAuthAsBytes() {
        return convertAuth(this.subscription.getAuth());

    }

    static byte[] convertAuth(String auth) {
        byte[] content = auth.getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlDecoder().decode(content);
    }

    public byte[] getMessageAsBytes() {
        try {
            return this.message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode bytes", ex);
        }
    }

    public String getEndpoint() {
        return this.subscription.endpoint;
    }


}
