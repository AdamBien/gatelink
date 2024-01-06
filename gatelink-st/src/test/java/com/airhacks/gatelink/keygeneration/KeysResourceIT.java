
package com.airhacks.gatelink.keygeneration;

import com.airhacks.gatelink.SystemTest;
import java.util.Base64;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class KeysResourceIT {

    private WebTarget tut;

    @BeforeEach
    public void init() {
        this.tut = SystemTest.target("keys");
    }

    @Test
    public void keyGeneration() {
        Response response = this.tut.
                request(MediaType.APPLICATION_JSON).
                get();
        assertThat(response.getStatus(), is(200));
        JsonObject keys = response.readEntity(JsonObject.class);
        System.out.println("keys = " + keys);
        String publicKey = keys.getString("publicKey");
        assertNotNull(publicKey);
        String privateKey = keys.getString("privateKey");
        assertNotNull(privateKey);
    }

    @Test
    public void alwaysReturnsTheSamePublicKey() {
        String first = fetchPublicKey();
        assertNotNull(first);
        byte[] decoded = Base64.getDecoder().decode(first);
        assertNotNull(decoded);
        String next = fetchPublicKey();
        assertNotNull(next);
        assertThat(first, is(next));
    }

    String fetchPublicKey() {
        Response response = this.tut.path("public").
                request(MediaType.APPLICATION_JSON).
                get();
        assertThat(response.getStatus(), is(200));
        String publicKey = response.readEntity(String.class);
        return publicKey;
    }



}
