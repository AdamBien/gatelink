
package com.airhacks.gatelink;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 *
 * @author airhacks.com
 */
public class EncryptionTestEnvironment {

    protected ECPublicKey serverPublicKey;
    protected ECPrivateKey serverPrivateKey;
    protected ServerKeysWithSubscription serverKeysWithSubscription;

    public void init(String browserSubscriptionData) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        this.serverKeysWithSubscription = new ServerKeysWithSubscription(browserSubscriptionData);
        this.serverPublicKey = this.serverKeysWithSubscription.getServerPublicKey();
        this.serverPrivateKey = this.serverKeysWithSubscription.getServerPrivateKey();
    }

}
