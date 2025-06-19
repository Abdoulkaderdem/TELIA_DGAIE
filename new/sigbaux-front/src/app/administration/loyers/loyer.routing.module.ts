import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { InvoiceList } from './invoice-list/invoice-list.component';
import { InvoiceForm } from './invoice-form/invoice-form.component';

const routes: Routes = [
  //{ path: '', redirectTo: 'list-demandes', pathMatch: 'full' },
  { path: 'invoice-list', component: InvoiceList },
  { path: 'invoice-form', component: InvoiceForm }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InvoiceRoutingModule {}
