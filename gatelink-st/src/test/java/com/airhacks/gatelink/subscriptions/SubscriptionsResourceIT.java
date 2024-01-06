package com.airhacks.gatelink.subscriptions;

import com.airhacks.gatelink.DataLoader;
import com.airhacks.gatelink.SystemTest;

import io.quarkus.test.junit.QuarkusTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import static jakarta.ws.rs.client.Entity.json;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
@QuarkusTest
public class SubscriptionsResourceIT {
    private WebTarget tut;
    private JsonObject chromeSubscription;

    @BeforeEach
    public void init() throws FileNotFoundException {
        this.tut = SystemTest.target("subscriptions");
        this.chromeSubscription = DataLoader.readDocument("chrome_subscription.json");
    }

    @Test
    public void subscribeTwice() {
        Response response = this.tut.request().post(json(this.chromeSubscription));
        assertThat(response.getStatus()).isEqualTo(204);

        response = this.tut.request().post(json(this.chromeSubscription));
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    public void subscribeAndDelete() throws UnsupportedEncodingException {

        String endpoint = this.chromeSubscription.getString("endpoint");

        Response response = this.tut.request().post(json(this.chromeSubscription));
        assertThat(response.getStatus()).isEqualTo(204);

        assertTrue(serverKnowsEndpoint(endpoint));

        Response deleteResponse = this.tut.path(encode(endpoint)).request().delete();
        assertThat(deleteResponse.getStatus()).isEqualTo(204);

        assertFalse(serverKnowsEndpoint(endpoint));
    }

    static String encode(String path) {
        byte[] rawBytes = path.getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().encodeToString(rawBytes);
    }

    @Test
    public void subscribeAndList() {
        Response response = this.tut.request().post(json(this.chromeSubscription));
        assertThat(response.getStatus()).isEqualTo(204);

        JsonArray subscriptions = getAllSubscriptions();
        int initialSize = subscriptions.size();

        //create again
        this.tut.request().post(json(this.chromeSubscription));

        response = this.tut.request().get();

        subscriptions = response.readEntity(JsonArray.class);
        int sizeAfterAddingIdenticalSubscription = subscriptions.size();

        assertThat(initialSize).isEqualTo(sizeAfterAddingIdenticalSubscription);

    }

    JsonArray getAllSubscriptions() {
        Response response;
        response = this.tut.request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        return response.readEntity(JsonArray.class);
    }

    boolean serverKnowsEndpoint(String endpoint) {
        long count = getAllSubscriptions().
                getValuesAs(JsonString.class).
                stream().
                map(JsonString::getString).
                filter(e -> e.equals(endpoint)).
                count();
        return count > 0;
    }

}
