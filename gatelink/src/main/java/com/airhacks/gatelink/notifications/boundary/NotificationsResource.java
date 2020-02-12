
package com.airhacks.gatelink.notifications.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

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
