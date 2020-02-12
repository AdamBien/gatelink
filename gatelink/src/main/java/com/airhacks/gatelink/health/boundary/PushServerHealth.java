
package com.airhacks.gatelink.health.boundary;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

/**
 *
 * @author airhacks.com
 */
@Readiness
@Liveness
@ApplicationScoped
public class PushServerHealth implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.builder().up().name("pushserver").build();
    }

}
