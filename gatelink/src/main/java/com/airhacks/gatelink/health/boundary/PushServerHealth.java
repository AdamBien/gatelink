
package com.airhacks.gatelink.health.boundary;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 *
 * @author airhacks.com
 */
@Health
@ApplicationScoped
public class PushServerHealth implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.builder().up().name("pushserver").build();
    }

}
