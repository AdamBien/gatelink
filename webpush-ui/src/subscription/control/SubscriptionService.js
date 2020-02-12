import { post, get, del } from '../../app/control/MicroService.js';
import { isSubscribed } from '../entity/Subscription.js';

const subscribe = async _ => { 
    const subscribed = await isSubscribed();
    if (subscribed) { 
        console.warn('Already subscribed',subscribed);
        return;
    }
    const key = await loadPublicKey();
    const registrationOptions = {
        userVisibleOnly: true,
        applicationServerKey: key
    };

    const registration = await navigator.serviceWorker.ready;
    const subscription = await registration.pushManager.subscribe(registrationOptions);

    const response = await post('subscriptions', subscription);
    if (!response.ok) {
        console.error('cannot register subscriptions',response.statusText);
        return;
    }
}

const unsubscribe = async _ => { 
    const registration = await navigator.serviceWorker.ready;
    let subscription =  await registration.pushManager.getSubscription();
    if (subscription) {
        const { endpoint } = subscription;
        const encodedEndpoint = btoa(endpoint);
        console.dir(subscription);
        subscription.unsubscribe();
        const response = await del(`subscriptions/${encodedEndpoint}`);
        if (!response.ok) {
            console.error('cannot unsubscribe', response.statusText);
            return;
        } else { 
            console.info(`unsubscribed ${endpoint}`);
        }    
    } else { 
        console.warn('not subscribed');
    }

}

const loadPublicKey = async _ => { 

    const response = await get('keys/public');
    const publicKey = await response.text();
    console.log(publicKey);
    return base64ToUint8Array(publicKey);
}

 const base64ToUint8Array = (encodedString) => {
     const decodedString = window.atob(encodedString);
    return Uint8Array.from(decodedString,c => c.charCodeAt(0));
 }

export { subscribe,unsubscribe }