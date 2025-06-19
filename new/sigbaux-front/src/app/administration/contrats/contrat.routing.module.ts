import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ContractComponent } from './contract-list/contract.component';
import { TraitementControleComponent } from './traitement-controle/traitement-controle.component';
import { ResiliationContractForm } from './resiliation-contract/resiliation-contract-form.component';
import { RevisionContractForm } from './revision-contract/revision-contract-form.component';
import { IndemnisationContract } from './indemnisation/indemnisation-contract.component';
import { PvRemiseClee } from './pv-remise/pv-remise-form.component';

const routes: Routes = [
  { path: '', component: ContractComponent },
  { path: 'control', component: TraitementControleComponent },
  { path: 'resiliation-contract-form', component: ResiliationContractForm },
  { path: 'revision-contract-form', component: RevisionContractForm },
  { path: 'pv-remise-clee-form', component: PvRemiseClee },
  { path: 'indemnisation-contract', component: IndemnisationContract },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ContratRoutingModule {}
