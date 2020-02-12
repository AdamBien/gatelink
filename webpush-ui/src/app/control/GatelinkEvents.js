const notificationEvent =  'g-notification';

const registerMessageListener = _ => { 
    navigator.serviceWorker.addEventListener('message', event => {
        console.dir(event);
        const { data } = event;
        fireNotificationEvent(data.message);
    });  
}

const fireNotificationEvent = async event => { 
    
    const customEvent = new CustomEvent(notificationEvent, {
        detail: {
            notification: event
        },
        bubbles:true

    });
    document.dispatchEvent(customEvent);
}

export { fireNotificationEvent, notificationEvent, registerMessageListener };