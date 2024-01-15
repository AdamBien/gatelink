
package com.airhacks.gatelink.log.boundary;

import com.airhacks.gatelink.Boundary;
import java.util.Base64;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 *
 * @author airhacks.com
 */
@Boundary
public class Tracer {

    static final String PREFIX = "pushserver";

    public void log(String message) {
        System.out.printf("[%s] %s\n", PREFIX, message);
    }

    public void log(String label, String message) {
        System.out.printf("[%s] %s %s\n", PREFIX, label, message);
    }

    public void log(String label, byte[] message) {
        var stringMessage = Base64.getUrlEncoder().encodeToString(message);
        System.out.printf("[%s] %s %s\n", PREFIX, label, stringMessage);
    }

    public static void debug(String label, byte[]... messages) {
        var encoder = Base64.getUrlEncoder();
        var stringified = Stream
                .of(messages)
                .map(encoder::encodeToString)
                .collect(Collectors.joining(","));
        System.out.printf("[%s] %s %s\n", PREFIX, label, stringified);
    }

}
