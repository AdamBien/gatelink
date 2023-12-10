/*
 */
package com.airhacks.gatelink.subscriptions.entity;

import java.security.Security;
import jakarta.json.Json;
import jakarta.json.bind.JsonbBuilder;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class SubscriptionTest {

    @BeforeEach
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }


    @Test
    public void testGetP256dhAsPublicKey() throws Exception {
        String mozillaPublicKey = "BBI3DLpP6uoCr53TVm58ztnRDJ6Wszr0oPIemSzJYLsLMYRhCeFU1EBE5mrq_kWSiNfj2Indn4GvgEAIPEXVLlg";
        Subscription subscription = new Subscription();
        subscription.keys = Json.createObjectBuilder().add(Subscription.PUBLIC_KEY, mozillaPublicKey).build();
        ECPublicKey p256dhAsPublicKey = subscription.getP256dhAsPublicKey();
        assertNotNull(p256dhAsPublicKey);
    }

    @Test
    public void jsonbDeserialize() {
        Subscription subscription = new Subscription();
        subscription.endpoint = "endpoint";
        subscription.keys = Json.createObjectBuilder().add("message", "hello").build();
        String json = JsonbBuilder.create().toJson(subscription);
        assertNotNull(json);
        System.out.println("json = " + json);
    }

}
