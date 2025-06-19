import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdministrationModule } from '../administration.module';
import { DemandeRoutingModule } from './demande.routing.module';
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
import { DemandeForm } from './demande-form/demande-form.component';
import { DemandeList } from './demande-list/demande-list.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { RegistreComponent } from './registre/registre.component';
import { RentalRequestsComponent } from './demande-crud/demande.component';
import { TabViewModule } from 'primeng/tabview';
import { NouvelleComponent } from './nouvelle/nouvelle.component';
import { ValidateComponent } from './validate/validate.component';
import { SendedComponent } from './sended/sended.component';
import { ApprouvalComponent } from './approuval/approuval.component';
import { RetenueComponent } from './retenue/retenue.component';
import { SearchBuildingComponent } from './search-building/search-building.component';
import { RatacheeComponent } from './ratachee/ratachee.component';
import { AllComponent } from './all/all.component';
import { TranslateModule } from '@ngx-translate/core';
import {StepsModule} from 'primeng/steps';
import { MenuModule } from 'primeng/menu';
import { RequestDataGrid } from './request-data-grid/request-data-grid.component';

@NgModule({
	declarations: [
		RequestDataGrid,
		DemandeList,
		DemandeForm,
		RentalRequestsComponent,
		RegistreComponent,
		NouvelleComponent,
		ValidateComponent,
		SendedComponent,
		ApprouvalComponent,
		RetenueComponent,
		SearchBuildingComponent,
		RatacheeComponent,
		AllComponent
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
		DemandeRoutingModule,
		ReactiveFormsModule,
		FormsModule,
		CalendarModule,
		AdministrationModule,
		TranslateModule,
		MenuModule,
		StepsModule,
	]
})

export class DemandeModule {}