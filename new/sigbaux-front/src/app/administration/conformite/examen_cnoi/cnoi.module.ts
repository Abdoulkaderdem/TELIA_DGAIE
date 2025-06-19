import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CnoiRoutingModule } from './cnoi-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';
import { CnoiComponent } from './cnoi.component';
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
import { RetenueComponent } from './retenue/retenue.component';
import { ValidationConseilComponent } from './validation-conseil/validation-conseil.component';
import { ApprouvalComponent } from './approuval/approuval.component';
import { CalculLoyerComponent } from './calcul-loyer/calcul-loyer.component';
import { BuildingestimateComponent } from './building-estimate/building-estimate.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ProgressBarModule } from 'primeng/progressbar';
import { RequestValidatedComponent } from './request-validated/request-validated.component';

@NgModule({
	declarations: [
		CnoiComponent,
		RetenueComponent,
		ValidationConseilComponent,
		ApprouvalComponent,
		CalculLoyerComponent,
		BuildingestimateComponent,
		RequestValidatedComponent
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
		CnoiRoutingModule,
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
		PanelModule,
		ProgressSpinnerModule,
		ProgressBarModule
	]
})

export class CnoiModule { }
