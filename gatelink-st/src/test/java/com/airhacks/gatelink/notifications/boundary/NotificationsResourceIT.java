/*
 */
package com.airhacks.gatelink.notifications.boundary;

import com.airhacks.gatelink.SystemTest;
import static jakarta.ws.rs.client.Entity.text;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class NotificationsResourceIT {

    private WebTarget tut;

    @BeforeEach
    public void initClient() {
        this.tut = SystemTest.target("notifications");
    }

    @Test
    public void sendNotification() {
        Response response = this.tut.request().post(text("hey duke " + System.currentTimeMillis()));
        assertThat(response.getStatus(), is(204));
    }


}
