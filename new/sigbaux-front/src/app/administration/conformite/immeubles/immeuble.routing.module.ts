import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { BuildingListComponent } from './identification/building-list.component';

const routes: Routes = [
  { path: '', component: BuildingListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ImmeubleRoutingModule {}