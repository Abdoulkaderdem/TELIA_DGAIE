import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
    title = 'Sig-Baux';

    constructor(
        private translate: TranslateService,
        private primengConfig: PrimeNGConfig
    ) {
        this.translate.setDefaultLang('fr');
    }

    ngOnInit(): void {
        this.primengConfig.ripple = true;
    }
}