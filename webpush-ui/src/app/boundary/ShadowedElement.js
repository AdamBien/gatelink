import { render } from '../../lit-html/lit-html.js';
export default class ShadowedElement extends HTMLElement { 
    constructor() { 
        super();
        this.root = this.attachShadow({mode:'open'});
    }

    connectedCallback() { 
        this.viewChanged();
    }

    viewChanged() { 
        render(this.view(),this.root);
    }
}