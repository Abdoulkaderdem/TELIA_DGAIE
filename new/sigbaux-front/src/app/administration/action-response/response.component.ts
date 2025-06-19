import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";


@Component({
    selector: 'action-response',
    templateUrl: './response.component.html'
})
export class ActionResponse implements OnChanges { 
    @Input() message!: string
    @Input() title?: string
    @Input() open: boolean = false
    constructor() { }

    ngOnChanges(changes: SimpleChanges) {
        this.open = changes['open']?.currentValue
        if (this.open) {
            this.showMessage()
            this.open = false
        }
    }

    showMessage() {
        const b = document.getElementById('show-message')
        b?.click()
    }
}
