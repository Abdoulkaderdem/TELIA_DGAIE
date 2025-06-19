import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";

@Component({
    selector: 'reset-input',
    templateUrl: './reset.html'
})
export class ResetInput implements OnChanges {
    @Input() formId!: any
    @Input() reset: boolean = false
    input: HTMLInputElement
    constructor() {
        this.input = this.buildInput
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['formId']?.currentValue != undefined) {
            this.appendInput()
        }
        if (changes['reset']?.currentValue) {
            this.input.click()
        }
    }

    private appendInput() {
        const form = document.getElementById(this.formId)!
        form?.append(this.input)

    }

    private get key() {
        let i = 0
        let r = ''
        do {
            r += Math.random().toString()
            i++
        } while (i < 10)
        return r
    }

    private get buildInput() {
        const input = document.createElement('input')
        input.hidden = true
        input.type = 'reset'
        input.id = this.key
        return input
    }

}