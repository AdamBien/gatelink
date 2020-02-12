
package com.airhacks.gatelink.keymanagement.boundary;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@ApplicationException(rollback = true)
public class KeyGenerationException extends WebApplicationException {

    public KeyGenerationException(String message) {
        super(Response.status(500).header("reason", message).build());
    }

}
