
package com.airhacks.gatelink;

import com.airhacks.gatelink.keymanagement.control.BCKeyLoader;
import com.airhacks.gatelink.keymanagement.entity.BCServerKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;
import com.airhacks.gatelink.subscriptions.entity.Subscription;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author airhacks.com
 */
public class ServerKeysWithSubscription {

    private final ECPublicKey serverPublicKey;
    private final ECPrivateKey serverPrivateKey;
    private final String serverPublicKeyAsString;
    private final String serverPrivateKeyAsString;
    private final Subscription subscription;

    public ServerKeysWithSubscription(String browserData) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        Security.addProvider(new BouncyCastleProvider());

        VapidKeys serverKeys = DataLoader.fromJson("serverkeys.json", VapidKeys.class);
        this.serverPublicKeyAsString = serverKeys.publicKey;
        this.serverPrivateKeyAsString = serverKeys.privateKey;

        this.serverPublicKey = BCKeyLoader.loadUrlEncodedPublicKey(serverPublicKeyAsString);
        this.serverPrivateKey = BCKeyLoader.loadURLEncodedPrivateKey(serverPrivateKeyAsString);

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

    public BCServerKeys getServerKeys() {
        return new BCServerKeys(serverPrivateKey, serverPublicKey);
    }


}
