import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InspectionsComponent } from './inspections.component';
import { InspectionBuildingComponent } from './inspection-building/inspection-building.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: InspectionsComponent },
            { path: 'inspection-building/:id', component: InspectionBuildingComponent },
        ]
        ),
    ],
    exports: [RouterModule],
})
export class InspectionsRoutingModule { }
