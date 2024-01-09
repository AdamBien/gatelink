import GElement from "../../app/boundary/GElement.js";
import { html } from "../../lit-html/lit-html.js";
import { askForNotification,areNotificationsGranted} from '../control/Notifications.js';
class NotificationManagement extends GElement {
    
    async postConstruct() {
        if (navigator.permissions && navigator.permissions.query) { 
            let status = await navigator.permissions.query({ 'name': "notifications" });
            console.dir(status);
        }
        console.log('browser does not support permissions');
    }

    view() { 
        
        if (areNotificationsGranted()) {
            return html`<div class="info">notification request granted</div>`;
        } else {
            return html`<button @click=${_ => this.onNotificationRequest()} > ask for notification</button>`;
        }

    }

    async onNotificationRequest() { 
        await askForNotification();
        super.viewChanged();
    }

}

customElements.define("g-notification-management",NotificationManagement);