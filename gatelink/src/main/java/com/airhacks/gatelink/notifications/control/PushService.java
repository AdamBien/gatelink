
package com.airhacks.gatelink.notifications.control;

import com.airhacks.gatelink.Control;
import com.airhacks.gatelink.log.boundary.Tracer;
import java.io.ByteArrayInputStream;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Control
public class PushService {

    private Client client;

    @Inject
    Tracer tracer;

    @PostConstruct
    public void initializeClient() {
        this.client = ClientBuilder.newClient();
    }

    public Response send(String endpoint, String salt, String ephemeralPublicKey, String vapidPublicKey, String authorizationToken, byte[] encryptedContent) {
        tracer.log("endpoint", endpoint);
        tracer.log("salt", salt);
        tracer.log("ephemeralPublicKey", ephemeralPublicKey);
        tracer.log("vapidPublicKey", vapidPublicKey);
        tracer.log("jsonWebSignature", authorizationToken);
        tracer.log("encryptedContent", encryptedContent);

        Response response = this.client.target(endpoint).
                request().
                header("TTL", "2419200").
                header("Content-Encoding", "aesgcm").
                header("Encryption", "salt=" + salt).
                header("Authorization", "WebPush " + authorizationToken).
                header("Crypto-Key", "dh=" + ephemeralPublicKey + ";p256ecdsa=" + vapidPublicKey).
                post(Entity.entity(new ByteArrayInputStream(encryptedContent), MediaType.APPLICATION_OCTET_STREAM));
        return response;
    }


}
