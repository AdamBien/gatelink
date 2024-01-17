package com.airhacks.gatelink.signature.control;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.airhacks.gatelink.keymanagement.control.ECKeyGenerator;

public class JsonWebSignatureTest {

    @Test
    void create() {
        var subject = "mailto:admin@gate.link";
        var audience = "https://fcm.googleapis.com";
        var ecKeys = ECKeyGenerator.generate();
        var privateKey = ecKeys.getPrivateKey();
        var smallryeResult = JsonWebSignature.create(privateKey, subject, audience);
        assertNotNull(smallryeResult);
    }

    @Test
    void issuedAtIn12H() {
        var tsIn12hInSeconds = JsonWebSignature.in12h();
        var tsIn12hInMs = tsIn12hInSeconds * 1000;
        var in12H = Instant
                .ofEpochMilli(tsIn12hInMs)
                .atOffset(ZoneOffset.UTC);
        var now = Instant
                .now()
                .atOffset(ZoneOffset.UTC);
        var backFromFuture = in12H.minusHours(12);
        assertThat(backFromFuture.getHour()).isEqualTo(now.getHour());
        assertThat(backFromFuture.getMinute()).isEqualTo(now.getMinute());
        assertThat(backFromFuture.getSecond()).isEqualTo(now.getSecond());
    }
}