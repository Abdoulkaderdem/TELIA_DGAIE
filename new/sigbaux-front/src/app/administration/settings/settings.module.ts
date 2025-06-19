import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsRoutingModule } from './settings-routing.module';
import { MinistryList } from './ministry-list/ministry-list.component';
import { StructureList } from './structure-list/structure-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdministrationModule } from '../administration.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { SettingsComponent } from './settings.component';
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
import { MappingComponent } from './mapping/mapping.component';
import { SplitButtonModule } from 'primeng/splitbutton';
import { AccordionModule } from 'primeng/accordion';
import { TabViewModule } from 'primeng/tabview';
import { FieldsetModule } from 'primeng/fieldset';
import { MenuModule } from 'primeng/menu';
import { DividerModule } from 'primeng/divider';
import { SplitterModule } from 'primeng/splitter';
import { PanelModule } from 'primeng/panel';
import { CharacteristicsComponent } from './characteristics-usage-list/characteristics.component';
import { TypeUsageComponent } from './type-usage/type-usage.component';
import { ValidationCommitteeComponent } from './validation-committee/validation-committee.component';

@NgModule({
	declarations: [
    StructureList, 
	MinistryList,
	ValidationCommitteeComponent,
    CharacteristicsComponent,
    TypeUsageComponent,
    SettingsComponent,
	MappingComponent
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
    SettingsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AdministrationModule,
    NgxPaginationModule,

		SplitButtonModule,
		AccordionModule,
		TabViewModule,
		FieldsetModule,
		MenuModule,
		DividerModule,
		SplitterModule,
		PanelModule,

		AdministrationModule
	]
})

export class SettingsModule { }
