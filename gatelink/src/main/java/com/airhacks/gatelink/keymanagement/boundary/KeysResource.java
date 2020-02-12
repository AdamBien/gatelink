
package com.airhacks.gatelink.keymanagement.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
