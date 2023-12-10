
package com.airhacks.gatelink.subscriptions.entity;

import com.airhacks.gatelink.keymanagement.control.KeyLoader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbTransient;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 * "... For each new subscription that the user agent generates for an
 * application, it also generates a P-256 [FIPS186] key pair for use in
 * elliptic-curve Diffie-Hellman (ECDH) [ECDH]. ..."
 *
 * {"endpoint":"https://fcm.googleapis.com/fcm/send/fgMwYcI1voE:APA91bG7e-lWxHqHrXQHl8q0DVZMKjWY32_ViKtHi0Go_uGr794V1aA1O5u4zSUlp52LyTzfllf4Ka9qsCB4z3q4ZlT4ZXUW8482VuHB1SOmUwks_sF9A5RjlG1ISA5galmk8u9qWNPv","expirationTime":null,"keys":{"p256dh":"BAbhIMLl60irVv_gxn3GEm4mk0PQsZsD9q3gfQJJAahVcUCyCi_p67ZZK_5q9rGJtPyv1Cen4ZkVC0-QM2itl6Q","auth":"kcdumC6d1QboOBpY-H3oRg"}}
 *
 * @author airhacks.com
 */
public class Subscription {

    public String endpoint;

    /**
     * Comprises keys provided by the browser, meant for encryption
     */
    public JsonObject keys;

    final static String PUBLIC_KEY = "p256dh";

    @JsonbTransient
    public String getP256dh() {
        return keys.getString(PUBLIC_KEY);
    }
    @JsonbTransient
    public String getAuth() {
        return keys.getString("auth");
    }
    @JsonbTransient
    public ECPublicKey getP256dhAsPublicKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyLoader.loadUrlEncodedPublicKey(this.getP256dh());
    }

    @Override
    public String toString() {
        return "Subscription{" + "endpoint=" + endpoint + ", keys=" + keys + '}';
    }

}
