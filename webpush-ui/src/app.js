import './notifications/boundary/NotificationManagement.js';
import './subscription/boundary/SubscriptionManagement.js';
import './notifications/boundary/NotificationOutput.js';
import { registerMessageListener } from './app/control/GatelinkEvents.js';

navigator.serviceWorker.
    register('push-worker.js').
    then(reg => {
        console.log("PushWorker registered: ", reg);
        registerMessageListener();
    });




  



