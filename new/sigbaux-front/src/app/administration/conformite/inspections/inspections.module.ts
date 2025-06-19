import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InspectionsRoutingModule } from './inspections-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';
import { InspectionsComponent } from './inspections.component';
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

import { SplitButtonModule } from 'primeng/splitbutton';
import { AccordionModule } from 'primeng/accordion';
import { TabViewModule } from 'primeng/tabview';
import { FieldsetModule } from 'primeng/fieldset';
import { MenuModule } from 'primeng/menu';
import { DividerModule } from 'primeng/divider';
import { SplitterModule } from 'primeng/splitter';
import { PanelModule } from 'primeng/panel';
import { BuildingAttenteComponent } from './inspection-attente/attente.component';
import { DemandeExamineeComponent } from './demande-examinee/demande.component';
import { ValidationConseilComponent } from './demande-validation-conseil/demande.component';
import { InspectionBuildingComponent } from './inspection-building/inspection-building.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
	declarations: [
		InspectionsComponent,
		BuildingAttenteComponent,
		DemandeExamineeComponent,
		ValidationConseilComponent,
		InspectionBuildingComponent
	],
	imports: [
		CommonModule,
		TableModule,
		FileUploadModule,
		FormsModule,
		ButtonModule,
		RippleModule,
		ToastModule,
		ToolbarModule,
		RatingModule,
		InputTextModule,
		InputTextareaModule,
		DropdownModule,
		RadioButtonModule,
		InputNumberModule,
		DialogModule,
		FormsModule,
		CommonModule,
		InspectionsRoutingModule,
		FormsModule,
		ReactiveFormsModule,
		NgxPaginationModule,
		SplitButtonModule,
		AccordionModule,
		TabViewModule,
		FieldsetModule,
		MenuModule,
		DividerModule,
		SplitterModule,
		TranslateModule,
		PanelModule
	]
})

export class InspectionsModule { }
