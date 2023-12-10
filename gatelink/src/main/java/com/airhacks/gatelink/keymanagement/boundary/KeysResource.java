
package com.airhacks.gatelink.keymanagement.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Path("/keys")
@ApplicationScoped
public class KeysResource {

    @Inject
    KeyStore store;

    @GET
    public JsonObject getKey() {
        return this.store.getKeys().toJson();
    }

    @GET
    @Path("public")
    public String getPublicKey() {
        return this.store.getKeys().getBase64PublicKeyWithoutPadding();
    }




}
