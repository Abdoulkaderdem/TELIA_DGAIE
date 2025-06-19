import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { InvoiceDTO } from 'src/app/interfaces/invoice';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ContractService } from 'src/app/services/contrat.service';
import { ContractDto,ContractResponse } from 'src/app/interfaces/contrat';
import { PrintService } from 'src/app/services/print.service';
import { TranslateService } from '@ngx-translate/core';
import { InvoiceService } from 'src/app/services/invoice.service';
import * as moment from 'moment';

@Component({
  selector: 'app-invoice-form',
  templateUrl: './invoice-form.component.html',
  providers: [MessageService, ConfirmationService]
})
export class InvoiceForm {
  invoiceForm: FormGroup;
  contractSearchForm: FormGroup;
  invoice: InvoiceDTO | null = null;
  selectedFile: File | null = null;
  searched: boolean = false;
  contract: ContractResponse | null = null;
  totalRent: number = 0;
  totalMonth: number = 0;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private contractService: ContractService,
    private invoiceService: InvoiceService,
    private messageService: MessageService,
    private translate: TranslateService,
    private confirmationService: ConfirmationService
  ) {
    this.translate.setDefaultLang('fr');
    this.invoiceForm = this.fb.group({
      contractId: [null, Validators.required],
      amount: [null, Validators.required],
      dueDate: [null, Validators.required],
      startInterval: [null, Validators.required],
      endInterval: [null, Validators.required],
      description: ['', Validators.required],
      invoiceReference: ['', Validators.required],
    });

    this.contractSearchForm = this.fb.group({
      contractId: [null, Validators.required]
    });

    this.invoiceForm.get('amount')?.disable();
  }

  searchContract(){
    if (this.contractSearchForm.invalid) {
      return;
    }
    this.searched = true;
    const id = this.contractSearchForm.get('contractId')?.value
    this.contractService.getContract(id).subscribe((data) => {
      this.contract = data;
      this.invoiceForm.patchValue({"contractId" : this.contract.id});
      console.log(this.contract);
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] || null;
  }

  onSubmit(): void {
    if (this.invoiceForm.invalid || !this.selectedFile) {
      this.messageService.add({severity:'error', summary: 'Echec', detail: "Veuillez remplir tous les champs du formulaire !"});
      return;
    }

    const formData = new FormData();
    formData.append('contractId', this.invoiceForm.get('contractId')?.value);
    formData.append('amount', this.invoiceForm.get('amount')?.value);
    formData.append('dueDate', this.formatDateToISO(this.invoiceForm.get('dueDate')?.value));
    formData.append('startInterval', this.formatDateToISO(this.invoiceForm.get('startInterval')?.value));
    formData.append('endInterval', this.formatDateToISO(this.invoiceForm.get('endInterval')?.value));
    formData.append('description', this.invoiceForm.get('description')?.value);
    formData.append('invoiceReference', this.invoiceForm.get('invoiceReference')?.value);
    formData.append('file', this.selectedFile);

    this.invoiceService.createInvoice(formData).subscribe(response => {
      console.log('Invoice submitted successfully', response);
      this.messageService.add({severity:'success', summary: 'Success', detail: 'Facture enregistrée avec succès !'});
    }, error => {
      console.log(error);
        this.messageService.add({severity:'error', summary: 'Echec', detail: 'Echec de téléchargement de la facture !'});
    });
  }

  formatDate(dateString: string | null): string {
    //if(!!dateString) return '-';
    const date = new Date(dateString!);
    return date.toLocaleDateString();
  }

  formatDateToISO(dateString: string): string {
    const [year, month, day] = dateString.split('-').map(Number);
    const date = new Date(year, month - 1, day);
    return date.toISOString();
  }

  onDateChange() {
    this.calculateRent();
  }
  

  calculateRent() {
    if(this.contract){
      const startDate = this.invoiceForm.get('startInterval')?.value;
      const endDate = this.invoiceForm.get('endInterval')?.value;
      const monthlyRent = this.contract.rentAmount;

      if (startDate && endDate) {
        const start = moment(startDate);
        const end = moment(endDate);
        const months = end.diff(start, 'months', true);
        this.totalMonth = Math.ceil(months);
        this.totalRent = this.totalMonth * monthlyRent;
        this.invoiceForm.patchValue({
          amount: this.totalRent
        });
      }
    }
  }
}