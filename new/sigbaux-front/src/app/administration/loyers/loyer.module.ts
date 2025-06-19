import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdministrationModule } from '../administration.module';
import { InvoiceRoutingModule } from './loyer.routing.module';
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
import { TranslateModule } from '@ngx-translate/core';
import { InvoiceList } from './invoice-list/invoice-list.component';
import { InvoiceForm } from './invoice-form/invoice-form.component';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ChipsModule } from 'primeng/chips';
import { ChipModule } from 'primeng/chip';
import { InputMaskModule } from 'primeng/inputmask';
import { KnobModule } from 'primeng/knob';
import { ListboxModule } from 'primeng/listbox';
import { SelectButtonModule } from 'primeng/selectbutton';
import { CheckboxModule } from 'primeng/checkbox';
import { InputSwitchModule } from 'primeng/inputswitch';
import { ColorPickerModule } from 'primeng/colorpicker';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { SliderModule } from 'primeng/slider';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';

@NgModule({
	declarations: [
		InvoiceList,
		InvoiceForm
	],
	imports: [
		AutoCompleteModule,
		ChipsModule,
		ChipModule,
		InputMaskModule,
		KnobModule,
		ListboxModule,
		SelectButtonModule,
		CheckboxModule,
		InputSwitchModule,
		ColorPickerModule,
		ToggleButtonModule,
		SliderModule,
		InputGroupModule,
		InputGroupAddonModule,
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
		InvoiceRoutingModule,
		ReactiveFormsModule,
		FormsModule,
		CalendarModule,
		AdministrationModule,
		TranslateModule
	]
})

export class LoyerModule {}