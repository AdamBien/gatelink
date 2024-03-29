import { render } from '../../lit-html/lit-html.js';
export default class GElement extends HTMLElement { 

    connectedCallback() { 
        this.postConstruct();
        this.viewChanged();
    }

    postConstruct() { }

    view() { }

    viewChanged() { 
        render(this.view(),this);
    }
}