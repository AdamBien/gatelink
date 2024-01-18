package com.airhacks.gatelink.notifications.control;

import java.io.IOException;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;;

public interface PushServiceClient {

    
    public static HttpResponse<Void> sendNotification(String endpoint, String salt, String ephemeralPublicKey, String vapidPublicKey, String authorizationToken, byte[] encryptedContent) 
    throws IOException, InterruptedException{
        var client = HttpClient.newHttpClient();
        var request = HttpRequest
        .newBuilder()
        .POST(BodyPublishers.ofByteArray(encryptedContent))
        .header("TTL", "2419200")
        .header("Content-Encoding", "aesgcm")
        .header("Encryption", "salt=" + salt)
        .header("Authorization", "WebPush " + authorizationToken)
        .header("Crypto-Key", "dh=" + ephemeralPublicKey + ";p256ecdsa=" + vapidPublicKey)
        .build();
        return client.send(request, BodyHandlers.discarding());
    }

}
