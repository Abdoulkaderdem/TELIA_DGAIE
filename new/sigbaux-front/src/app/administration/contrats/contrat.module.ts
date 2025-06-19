import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdministrationModule } from '../administration.module';
import { CascadeSelectModule } from 'primeng/cascadeselect';
import { MultiSelectModule } from 'primeng/multiselect';
import { TableModule } from 'primeng/table';
import { FileUploadModule } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { TabViewModule } from 'primeng/tabview';
import { ContratRoutingModule } from './contrat.routing.module';
import { TraitementControleComponent } from './traitement-controle/traitement-controle.component';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { TranslateModule } from '@ngx-translate/core';
import { ResiliationContractForm } from './resiliation-contract/resiliation-contract-form.component';
import { ContractComponent } from './contract-list/contract.component';
import { IndemnisationContract } from './indemnisation/indemnisation-contract.component';
import { RevisionContractForm } from './revision-contract/revision-contract-form.component';
import { PvRemiseClee } from './pv-remise/pv-remise-form.component';

@NgModule({
	declarations: [
		ContractComponent,
		TraitementControleComponent,
		ResiliationContractForm,
		IndemnisationContract,
		RevisionContractForm,
		PvRemiseClee
	],
	imports: [
		CommonModule,
		TableModule,
		TabViewModule,
		FileUploadModule,
		FormsModule,
		ButtonModule,
		RippleModule,
		ToastModule,
		ToolbarModule,
        CascadeSelectModule,
        MultiSelectModule,
		RatingModule,
		InputTextModule,
		InputTextareaModule,
		DropdownModule,
		RadioButtonModule,
		InputNumberModule,
		DialogModule,
		ContratRoutingModule,
		ReactiveFormsModule,
		FormsModule,
		CalendarModule,
		AdministrationModule,
		InputGroupModule,
		InputGroupAddonModule,
		TranslateModule
	]
})

export class ContratModule {}