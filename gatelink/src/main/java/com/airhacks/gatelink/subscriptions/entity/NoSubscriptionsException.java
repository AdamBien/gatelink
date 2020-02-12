
package com.airhacks.gatelink.subscriptions.entity;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class NoSubscriptionsException extends WebApplicationException {

    public NoSubscriptionsException() {
        super(Response.status(400).header("reason", "no subscriptions available").build());
    }

}
