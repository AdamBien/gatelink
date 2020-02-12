import GElement from "../../app/boundary/GElement.js";
import { html } from "../../lit-html/lit-html.js";
import { notificationEvent } from '../../app/control/GatelinkEvents.js';
class NotificationOutput extends GElement {
    
    constructor() { 
        super();
        this.notificationMessage = "";
    }

    postConstruct() { 
        document.addEventListener(notificationEvent,e => this.showPushNotification(e));
    }


    view() { 
        return html`
            <output>${this.notificationMessage}</output>
        `;

    }

    showPushNotification(customMessageEvent) { 
        const { detail } = customMessageEvent;
        this.notificationMessage = detail.notification;
        super.viewChanged();
    }
}

customElements.define("g-notification-output",NotificationOutput);