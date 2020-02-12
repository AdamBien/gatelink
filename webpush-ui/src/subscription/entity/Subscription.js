const isSubscribed = async () => { 
    const registration = await navigator.serviceWorker.ready;
    let subscription =  await registration.pushManager.getSubscription();
    if (subscription) {
        return true;
    } else { 
        return false;
    }
}

export { isSubscribed }

