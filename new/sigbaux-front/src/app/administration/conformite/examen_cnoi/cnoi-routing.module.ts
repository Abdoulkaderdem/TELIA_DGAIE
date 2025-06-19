import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CnoiComponent } from './cnoi.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: CnoiComponent }]),
    ],
    exports: [RouterModule],
})
export class CnoiRoutingModule { }
