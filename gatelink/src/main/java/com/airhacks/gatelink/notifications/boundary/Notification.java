
package com.airhacks.gatelink.notifications.boundary;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import com.airhacks.gatelink.subscriptions.entity.PushSubscription;

/**
 *
 * @author airhacks.com
 */
public class Notification {

    private final PushSubscription subscription;
    private final String message;

    public Notification(PushSubscription subscription, String message) {
        this.subscription = subscription;
        this.message = message;
    }

    public PushSubscription getSubscription() {
        return subscription;
    }

    public String getMessage() {
        return message;
    }


    public ECPublicKey getJCEPublicKey() {
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
        var content = auth.getBytes();
        return Base64.getUrlDecoder().decode(content);
    }

    public byte[] getMessageAsBytes() {
       return this.message.getBytes();
    }

    public String getEndpoint() {
        return this.subscription.endpoint;
    }


}
