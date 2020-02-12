
package com.airhacks.gatelink.log.boundary;

import java.util.Base64;

/**
 *
 * @author airhacks.com
 */
public class Tracer {

    static final String PREFIX = "pushserver";

    public void log(String message) {
        System.out.printf("[%s] %s\n", PREFIX, message);
    }

    public void log(String label, String message) {
        System.out.printf("[%s] %s %s\n", PREFIX, label, message);
    }

    public void log(String label, byte[] message) {
        String stringMessage = Base64.getUrlEncoder().encodeToString(message);
        System.out.printf("[%s] %s %s\n", PREFIX, label, stringMessage);
    }

}
