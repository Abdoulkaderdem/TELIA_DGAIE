import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { RentalOfferComponent } from './list-offres/list-offres.component';
import { OfferDetailsComponent } from './offer-details/offer-details.component';
import { OfferFormComponent } from './offer-form/offer-form.component';

const routes: Routes = [
  //{ path: '', redirectTo: 'list-offres', pathMatch: 'full' },
  { path: 'list-offres', component: RentalOfferComponent },
  { path: 'new', component: OfferFormComponent },
  { path: 'details/:id', component: OfferDetailsComponent },
  { path: 'edit/:id', component: OfferFormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OffresRoutingModule {}
