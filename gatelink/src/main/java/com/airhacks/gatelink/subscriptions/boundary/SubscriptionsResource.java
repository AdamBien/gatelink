
package com.airhacks.gatelink.subscriptions.boundary;

import com.airhacks.gatelink.subscriptions.control.InMemorySubscriptionsStore;
import com.airhacks.gatelink.subscriptions.entity.PushSubscription;
import java.util.Base64;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    InMemorySubscriptionsStore store;

    @POST
    @Counted(name = "subscribeActions", absolute = true)
    public void subscribe(PushSubscription subscription) {
        System.out.println("Subscription " + subscription);
        this.store.addSubscription(subscription);
    }

    @DELETE
    public void removeAll() {
        this.store.removeAll();
    }

    @DELETE
    @Path("{endpoint}")
    @Counted(name = "unsubscribeActions", absolute = true)
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
