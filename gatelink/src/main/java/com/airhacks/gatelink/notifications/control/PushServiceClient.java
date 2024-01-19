package com.airhacks.gatelink.notifications.control;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;


public interface PushServiceClient {

    record NotificationResponse(int status){
        public boolean isSuccessful(){
            return status >= 200 && status <= 400;
        }
    }

    public static NotificationResponse sendNotification(String endpoint, String salt, String ephemeralPublicKey,
            String vapidPublicKey, String authorizationToken, byte[] encryptedContent){
        var uri = URI.create(endpoint);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest
                    .newBuilder(uri)
                    .POST(BodyPublishers.ofByteArray(encryptedContent))
                    .header("TTL", "2419200")
                    .header("Content-Encoding", "aesgcm")
                    .header("Encryption", "salt=" + salt)
                    .header("Authorization", "WebPush " + authorizationToken)
                    .header("Crypto-Key", "dh=" + ephemeralPublicKey + ";p256ecdsa=" + vapidPublicKey)
                    .header("Content-type", "application/octet-stream")
                    .build();
            var response =  client.send(request, BodyHandlers.discarding());
            return new NotificationResponse(response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("cannot send message: " + e);
        }
    }

}
