import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SettingsComponent } from './settings.component';
import { MappingComponent } from './mapping/mapping.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: SettingsComponent },
            { path: 'mapping', component: MappingComponent }
        ]),
    ],
    exports: [RouterModule],
})

export class SettingsRoutingModule { }