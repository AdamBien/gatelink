/*
 */
package com.airhacks.gatelink.notifications.control;

import com.airhacks.gatelink.notifications.control.PushService;
import com.airhacks.gatelink.log.boundary.Tracer;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author airhacks.com
 */
public class PushServiceIT {

    private PushService cut;

    @BeforeEach
    public void init() {
        this.cut = new PushService();
        this.cut.initializeClient();
        this.cut.tracer = new Tracer();

    }

    public PushService getCut() {
        this.init();
        return cut;
    }

}
