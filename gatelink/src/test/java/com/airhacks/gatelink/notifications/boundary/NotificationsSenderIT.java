/*
 */
package com.airhacks.gatelink.notifications.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.airhacks.gatelink.EncryptionTestEnvironment;
import com.airhacks.gatelink.encryption.boundary.EncryptionServiceIT;
import com.airhacks.gatelink.log.boundary.Tracer;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.*;

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

    }

    public NotificationsSender getCut() throws Exception {
        this.initialize();
        return cut;
    }

    @Test
    public void sendNotification() throws IOException {
        var subscriptionOptional = this.getCurrentSubscription();
        assumeTrue(subscriptionOptional.isPresent());
        var notification = this.serverKeysWithSubscription.getNotification("hey duke " + System.currentTimeMillis());
        var response = this.cut.send(notification, this.serverKeysWithSubscription.getServerKeys());
        // int status = response.status()
        assertThat(response).isTrue();
        // assertThat(status).isEqualTo(201);
        /*
         * response.getHeaders().entrySet().forEach(e ->
         * System.out.printf("%s -> %s \n", e.getKey(), e.getValue()));
         * String responseMessage = response.readEntity(String.class);
         * System.out.println("responseMessage = " + responseMessage);
         */
    }

    Optional<String> getCurrentSubscription() throws IOException{
        var subscriptionPath = Path.of("src/test/resources/subscription.json");
        var subscriptionContent = Files.readString(subscriptionPath);
        if(subscriptionContent.isBlank() || subscriptionContent.isEmpty())
            return Optional.empty();

        return Optional.of(subscriptionContent);
    }

}
