/*
 */
package com.airhacks.gatelink.subscriptions.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.bind.JsonbBuilder;

/**
 *
 * @author airhacks.com
 */
public class SubscriptionTest {



    @Test
    public void testGetP256dhAsPublicKey() throws Exception {
        String mozillaPublicKey = "BBI3DLpP6uoCr53TVm58ztnRDJ6Wszr0oPIemSzJYLsLMYRhCeFU1EBE5mrq_kWSiNfj2Indn4GvgEAIPEXVLlg";
        PushSubscription subscription = new PushSubscription();
        subscription.keys = Json.createObjectBuilder().add(PushSubscription.PUBLIC_KEY, mozillaPublicKey).build();
        var p256dhAsPublicKey = subscription.getP256dhAsPublicKey();
        assertNotNull(p256dhAsPublicKey);
    }

    @Test
    public void jsonbDeserialize() {
        PushSubscription subscription = new PushSubscription();
        subscription.endpoint = "endpoint";
        subscription.keys = Json.createObjectBuilder().add("message", "hello").build();
        String json = JsonbBuilder.create().toJson(subscription);
        assertNotNull(json);
        System.out.println("json = " + json);
    }

}
