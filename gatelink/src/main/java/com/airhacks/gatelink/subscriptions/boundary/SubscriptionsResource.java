
package com.airhacks.gatelink.subscriptions.boundary;

import com.airhacks.gatelink.subscriptions.control.SubscriptionsStore;
import com.airhacks.gatelink.subscriptions.entity.Subscription;
import java.util.Base64;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.stream.JsonCollectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.metrics.annotation.Counted;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/subscriptions")
public class SubscriptionsResource {

    @Inject
    SubscriptionsStore store;

    @POST
    @Counted(name = "subscribeActions", monotonic = true)
    public void subscribe(Subscription subscription) {
        System.out.println("Subscription " + subscription);
        this.store.addSubscription(subscription);
    }

    @DELETE
    public void removeAll() {
        this.store.removeAll();
    }

    @DELETE
    @Path("{endpoint}")
    @Counted(name = "unsubscribeActions", monotonic = true)
    public void unsubscribe(@PathParam("endpoint") String endpoint) {
        byte[] rawEndpoint = Base64.getUrlDecoder().decode(endpoint);
        this.store.remove(new String(rawEndpoint));
    }

    /**
     *
     * @return a JsonArray of endpoints. An endpoint uniquely identifies a
     * subscription
     */
    @GET
    public JsonArray all() {
        return this.store.all().stream().
                map(s -> s.endpoint).
                map(Json::createValue).
                collect(JsonCollectors.toJsonArray());
    }


}
