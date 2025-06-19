import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MeoComponent } from './meo.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: MeoComponent }]),
    ],
    exports: [RouterModule],
})
export class MeoRoutingModule { }
