import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { DemandeList } from './demande-list/demande-list.component';
import { RegistreComponent } from './registre/registre.component';
import { DemandeForm } from './demande-form/demande-form.component';
import { RentalRequestsComponent } from './demande-crud/demande.component';
import { SearchBuildingComponent } from './search-building/search-building.component';

const routes: Routes = [
  //{ path: '', redirectTo: 'list-demandes', pathMatch: 'full' },
  { path: 'list-demandes', component: RegistreComponent },
  { path: 'form-demande', component: DemandeForm },
  { path: '', component: RentalRequestsComponent },
  { path: 'search-building/:id', component: SearchBuildingComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DemandeRoutingModule {}
