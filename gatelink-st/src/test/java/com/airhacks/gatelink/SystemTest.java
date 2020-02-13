
package com.airhacks.gatelink;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;

/**
 *
 * @author airhacks.com
 */
public interface SystemTest {

    public final static String SERVICE_URI = "service.uri";
    public final static String DEFAULT_HOST = "http://localhost:9080/";

    public static Client client() {
        return ClientBuilder.newClient().register(JsrJsonpProvider.class);
    }

    static WebTarget configurableTarget() {
        String serviceURI = System.getenv().
                getOrDefault(SERVICE_URI, System.getProperty(SERVICE_URI, DEFAULT_HOST));
        return client().target(serviceURI);
    }

    public static WebTarget target(String path) {
        return configurableTarget().
                path("pushserver").
                path("resources").
                path(path);
    }

    public static WebTarget context(String path) {
        return configurableTarget().
                path(path);
    }



}
