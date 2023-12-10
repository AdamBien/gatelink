
package com.airhacks.gatelink.keymanagement.boundary;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class KeyGenerationException extends WebApplicationException {

    public KeyGenerationException(String message) {
        super(Response.status(500).header("reason", message).build());
    }

}
