
package com.airhacks.gatelink.notifications.boundary;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import com.airhacks.gatelink.bytes.control.Bytes;
import com.airhacks.gatelink.subscriptions.entity.Subscription;

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


    public ECPublicKey getJCEPublicKey() {
        try {
            return this.subscription.getP256dhAsPublicKeyWithJCE();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("Key cannot be loaded", ex);
        }
    }

    public byte[] getAuthAsBytes() {
        return convertAuth(this.subscription.getAuth());

    }

    static byte[] convertAuth(String auth) {
        var content = Bytes.getBytes(auth);
        return Base64.getUrlDecoder().decode(content);
    }

    public byte[] getMessageAsBytes() {
       return Bytes.getBytes(this.message);
    }

    public String getEndpoint() {
        return this.subscription.endpoint;
    }


}
