import GElement from "../../app/boundary/GElement.js";
import { html } from "../../lit-html/lit-html.js";
import { subscribe, unsubscribe } from '../control/SubscriptionService.js';
import { isSubscribed, getSubscription } from '../entity/Subscription.js';
class SubscriptionManagement extends GElement {

    constructor() {
        super();
        this.subscribed = false;
        this.subscription = "";
    }

    view() {
        return html`
            <label for="subscription">subscribed to push notifications:</label>
            <input id="subscription" type="checkbox" ?checked=${this.subscribe} @input=${e => this.handleSubscription(e)}>
            <output>${this.subscription}</output>
            <button @click=${e => this.copyToClipboard(e)}>copy</button>
        `;
    }

    copyToClipboard = (event) => {
        event.preventDefault();
        console.log("attempt to write to the clipboard");
     
    
        navigator.clipboard.writeText(this.subscription)
            .then(function () {
            console.log("clipboard written successfully")
        }, function() {
            console.error("clipboard access failed")
        });
    }


    async handleSubscription({ target }) {
        const { checked } = target;
        console.dir(target, checked);
        if (checked) {
            await this.subscribe();
        } else {
            await this.unsubscribe();
        }
        let subscriptionObject = await getSubscription();
        console.dir(subscriptionObject);
        this.subscription = JSON.stringify(subscriptionObject || {});
        super.viewChanged();

    }


    async subscribe() {
        await subscribe();
        this.subscribed = await isSubscribed();
    }
    async unsubscribe() {
        await unsubscribe();
        this.subscribed = await isSubscribed();
    }
}

customElements.define('g-subscription-management', SubscriptionManagement);
