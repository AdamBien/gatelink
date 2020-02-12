import GElement from "../../app/boundary/GElement.js";
import { html } from "../../lit-html/lit-html.js";
import { subscribe,unsubscribe } from '../control/SubscriptionService.js';
import { isSubscribed } from '../entity/Subscription.js';
class SubscriptionManagement extends GElement {

    constructor() { 
        super();
        this.subscribed = false;
    }

    view() { 
        return html`
            <label for="subscription">subscribed to push notifications:</label>
            <input id="subscription" type="checkbox" ?checked=${this.subscribe} @input=${e => this.handleSubscription(e)}>
        `;
    }

    handleSubscription({ target }) { 
        const { checked } = target;
        console.dir(target,checked);
        if (checked)
            this.subscribe();            
        else
            this.unsubscribe();
            
    } 


    async subscribe() { 
        await subscribe();
        this.subscribed = await isSubscribed();
        super.viewChanged();
    }
    async unsubscribe() { 
        await unsubscribe();
        this.subscribed = await isSubscribed();
        super.viewChanged();
    }
}

customElements.define('g-subscription-management',SubscriptionManagement);
