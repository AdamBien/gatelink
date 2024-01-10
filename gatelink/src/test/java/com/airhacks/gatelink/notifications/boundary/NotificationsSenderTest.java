/*
 */
package com.airhacks.gatelink.notifications.boundary;

import com.airhacks.gatelink.notifications.boundary.NotificationsSender;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class NotificationsSenderTest {

    private String endpoint;

    @BeforeEach
    public void init() {
        this.endpoint = "https://updates.push.services.mozilla.com/wpush/v2/gAAAAABcOjucf8G1Uc8JhbA6mkm25Vu_UhzdRb7RjrVI2yUPxu9raA2s1fJj6WPucpYK2zhTzBAOUtt8u4bgUvx1XkgO9dXZUIMx7B0s0jAfoxNWZ7f0plyLauQnD9QxEUy8LC5zoTcbB9PS0r7jFImteDVT-BgF11GDb1Vqgdx4VA_OG-TDb0U";
    }

    @Test
    public void extractHostFromAudience() throws Exception {
        String aud = NotificationsSender.extractAud(endpoint);
        assertThat(aud).isEqualTo("https://updates.push.services.mozilla.com");
    }

    @Test
    public void extractInvalidHostForAudience() throws Exception {
        String expected = "invalid";
        String actual = NotificationsSender.extractAud(expected);
        assertThat(actual).isEqualTo(expected);
    }

}
