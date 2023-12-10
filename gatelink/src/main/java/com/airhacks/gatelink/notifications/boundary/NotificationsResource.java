
package com.airhacks.gatelink.notifications.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
@Path("/notifications")
public class NotificationsResource {

    @Inject
    NotificationsSender sender;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void send(String message) {
        this.sender.send(message);
    }

}
