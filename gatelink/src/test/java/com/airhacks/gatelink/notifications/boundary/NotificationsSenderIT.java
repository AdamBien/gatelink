/*
 */
package com.airhacks.gatelink.notifications.boundary;

import com.airhacks.gatelink.notifications.boundary.Notification;
import com.airhacks.gatelink.notifications.boundary.NotificationsSender;
import com.airhacks.gatelink.EncryptionTestEnvironment;
import com.airhacks.gatelink.encryption.boundary.EncryptionServiceIT;
import com.airhacks.gatelink.log.boundary.Tracer;
import com.airhacks.gatelink.notifications.control.PushServiceIT;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author airhacks.com
 */
public class NotificationsSenderIT extends EncryptionTestEnvironment {

    private NotificationsSender cut;

    @BeforeEach
    public void initialize() throws Exception {
        super.init("chrome");
        this.cut = new NotificationsSender();
        MetricRegistry registry = mock(MetricRegistry.class);
        Counter counter = mock(Counter.class);
        when(registry.counter(Mockito.anyString())).thenReturn(counter);
        this.cut.registry = registry;

        this.cut.tracer = new Tracer();
        this.cut.encryptionService = new EncryptionServiceIT().getCut();

        this.cut.pushService = new PushServiceIT().getCut();
    }

    public NotificationsSender getCut() throws Exception {
        this.initialize();
        return cut;
    }


    @Test
    public void sendNotification() {
        Notification notification = this.serverKeysWithSubscription.getNotification("hey duke " + System.currentTimeMillis());
        Response response = this.cut.send(notification, this.serverKeysWithSubscription.getServerKeys());
        int status = response.getStatus();
        assertThat(status, is(201));
        response.getHeaders().entrySet().forEach(e -> System.out.printf("%s -> %s \n", e.getKey(), e.getValue()));
        String responseMessage = response.readEntity(String.class);
        System.out.println("responseMessage = " + responseMessage);
    }

}
