/**
 * 
 * @returns {Promise<boolean>}
 */
const isSubscribed = async () => { 
    let subscription = await getSubscription();
    if (subscription) {
        return true;
    } else { 
        return false;
    }
}


/**
 * 
 * @returns {Promise<PushSubscription | null>}
 */
const getSubscription = async () => { 
    const registration = await navigator.serviceWorker.ready;
    return await registration.pushManager.getSubscription();

}

export { isSubscribed, getSubscription }

