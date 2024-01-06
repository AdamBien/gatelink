
package com.airhacks.gatelink;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

/**
 *
 * @author airhacks.com
 */
public interface SystemTest {

    public final static String SERVICE_URI = "service.uri";
    public final static String DEFAULT_HOST = "http://localhost:8080/";

    public static Client client() {
        return ClientBuilder.newClient();
    }

    static WebTarget configurableTarget() {
        String serviceURI = System.getenv().getOrDefault(SERVICE_URI, System.getProperty(SERVICE_URI, DEFAULT_HOST));
        return client().target(serviceURI);
    }

    public static WebTarget target(String path) {
        return configurableTarget()
                .path(path);
    }

    public static WebTarget context(String path) {
        return configurableTarget()
        .path(path);
    }

}
