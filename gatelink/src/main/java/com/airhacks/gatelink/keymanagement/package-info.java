/*
* https://developers.google.com/web/fundamentals/push-notifications/subscribing-a-user
* The applicationServerKey option passed into the subscribe() call is the application's public key. The browser passes this onto a push service when subscribing the user, meaning the push service can tie your application's public key to the user's PushSubscription.
* When you later want to send a push message, you'll need to create an Authorization header which will contain information signed with your application server's private key. When the push service receives a request to send a push message, it can validate this signed Authorization header by looking up the public key linked to the endpoint receiving the request. If the signature is valid the push service knows that it must have come from the application server with the matching private key. It's basically a security measure that prevents anyone else sending messages to an application's users.
*/
package com.airhacks.gatelink.keymanagement;
