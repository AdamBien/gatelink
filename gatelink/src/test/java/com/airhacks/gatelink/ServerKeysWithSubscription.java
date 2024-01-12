
package com.airhacks.gatelink;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

import com.airhacks.gatelink.keymanagement.control.JCEKeyLoader;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;
import com.airhacks.gatelink.subscriptions.entity.Subscription;

/**
 *
 * @author airhacks.com
 */
public class ServerKeysWithSubscription {

    ECPublicKey serverPublicKey;
    ECPrivateKey serverPrivateKey;
    String serverPublicKeyAsString;
    String serverPrivateKeyAsString;
    Subscription subscription;

    public ServerKeysWithSubscription(String browserData) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        VapidKeys serverKeys = DataLoader.fromJson("serverkeys.json", VapidKeys.class);
        this.serverPublicKeyAsString = serverKeys.publicKey;
        this.serverPrivateKeyAsString = serverKeys.privateKey;

        this.serverPublicKey = JCEKeyLoader.loadUrlEncodedPublicKey(serverPublicKeyAsString);
        this.serverPrivateKey = JCEKeyLoader.loadURLEncodedPrivateKey(serverPrivateKeyAsString);

        this.subscription = DataLoader.fromJson(String.format("%s.json", browserData), Subscription.class);

    }

    public ECPublicKey getServerPublicKey() {
        return serverPublicKey;
    }

    public ECPrivateKey getServerPrivateKey() {
        return serverPrivateKey;
    }

    public String getServerPublicKeyAsString() {
        return serverPublicKeyAsString;
    }

    public String getServerPrivateKeyAsString() {
        return serverPrivateKeyAsString;
    }

    public Notification getNotification(String message) {
        return new Notification(subscription, message);
    }

    public ECKeys getServerKeys() {
        return new ECKeys(serverPrivateKey, serverPublicKey);
    }


}
