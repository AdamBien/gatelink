const askForNotification = async () => { 
    const permission = await Notification.requestPermission();
    console.log(permission);
    return permission;
}

const showNotification = async () => { 
    let permission = Notification.permission;
    if (permission === 'default') { 
        permission =  await askForNotification();
    }

    if (permission === 'denied') { 
        console.error('Cannot show notification with denied permission'); 
        return false;
    }
}

const areNotificationsGranted =  _ => { 
    const permission = Notification.permission;
    return permission === 'granted';
}


export { askForNotification,showNotification,areNotificationsGranted};