# Push Server

# Prerequisites

Java 8+ is installed
A Java EE 8 + MicroProfile capable server is installed.
In case of OpenLiberty, also the HTTPS certificates for push gateways (chrome, firefox)
have to be installed in the keystore. openJDKs come with preinstalled certificates, so 
that JDK / openJDK stores can be directly used (`cacerts`) for that purpose.


# Installation

Copy the `pushserver.war` into auto deployment folder.

# Typical Use Case

1. At the first use / startup VAPID keypair is created and stored in memory. These keys are used for the identification agains push services.
2. The browser asks the user for permissions.
3. The browser fetches the public VAPID key from the server and uses it to generate 
a subscription object.
4. The `Subscription` is send back to push server.
5. The push server stores the subscription in memory and uses the "endpoint" slot as a key.
6. A message (payload) can be send to the server via `POST /resources/notifications`
7. The server will iterate over all subscriptions and send the payload using the endpoint from the `Subscription` object.
8. For each message a new keypair is generated. The generated ephemeral keys are salted and used for encryption. 
9. VAPID keys are used for signing (using JWT signature).

# Build 

## Build and Unit Test execution

`cd pushserver` then

`mvn package` creates a deployable WAR in the `target` folder

## Integration Test Stage

`mvn failsafe:integration-test` integration test execution. Push notifications
are sent without deployment. Requires updates of subscriptions and server keys in: `pushserver/src/test/resources`

## Build System Test

Switch to maven project pushserver-st: `cd pushserver-st`

`mvn package`

## Perform System Tests

System tests are accessing a deployed pushserver. The system tests are attempting to resolve the `service.uri` environment entry, then a Java SystemProperty and finally
they are using the `http://localhost:9080` as host name. It means: on local machine
with stock openliberty installed, the system tests should execution without any additional configuration.

`mvn failsafe:integration-test`








