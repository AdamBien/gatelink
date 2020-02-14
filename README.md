# gatelink

WebPush Gateway (powered by [quarkus](https://quarkus.io))

The gatelink service implements:

- [Voluntary Application Server Identification for Web Push (VAPID)](https://tools.ietf.org/html/draft-thomson-webpush-vapid-02)
- [Message Encryption for Web Push](https://tools.ietf.org/html/rfc8291)

[Push Notification Overview](https://developers.google.com/web/fundamentals/push-notifications/web-push-protocol#more_headers)

# quickstart

## gatelink server build & start:

```
cd gatelink
mvn package
java -jar target/gatelink-[VERSION]-runner.jar 
```

alternative / development mode:

```
cd gatelink
mvn compile quarkus:dev
```

## gatelink docker build

```
mvn package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/gatelink-jvm .
docker run -i --rm -p 8080:8080 quarkus/gatelink-jvm
```

## test ui start

Install [browsersync](https://www.browsersync.io)

```
cd webpush-ui
./startBrowserSync.sh
```

## [webpush-ui](https://github.com/AdamBien/webpush/tree/master/webpush-ui) sample application

The user interface uses
1. [Notification API](https://developer.mozilla.org/en-US/docs/Web/API/notification) to display the badges
2. [Push API](https://developer.mozilla.org/en-US/docs/Web/API/Push_API) to receive the messages and send a subscription to the server.
3. [Service Workers API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API) to listen for changes in the background
4. [Custom Elements](https://developer.mozilla.org/en-US/docs/Web/API/Window/customElements) for structuring the application
5. [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) to send the subscription to the server or to unsubscribe.
