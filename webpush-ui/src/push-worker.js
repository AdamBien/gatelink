const showNotification = async (title, body) => { 
    let permission = Notification.permission;

    if (permission === 'denied') { 
        console.error('Cannot show notification with denied permission'); 
        return;
    }
    if (permission === 'granted') {
        console.log('granted, sending notification');
        return await self.registration.showNotification(title, { body: body });
    }
    
    return;
}

self.addEventListener('push', event => {
    const messageContent = event.data.text();
    self.clients.matchAll().then((clients) => { 
        clients.forEach(client => client.postMessage({message:messageContent}));
    });    
    event.waitUntil(showNotification("background notification", messageContent));
});

self.addEventListener('install', event => { 
    self.skipWaiting();

});

self.addEventListener('activate', event => { 
    event.waitUntil(clients.claim());
});