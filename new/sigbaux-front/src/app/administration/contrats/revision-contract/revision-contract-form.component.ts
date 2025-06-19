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
import { catchError, finalize, tap, of } from 'rxjs';

@Component({
  selector: 'app-revision-contract-form',
  templateUrl: './revision-contract-form.component.html',
  providers: [MessageService, ConfirmationService]
})
export class RevisionContractForm {
  resiliationForm: FormGroup;
  contractSearchForm: FormGroup;
  invoice: InvoiceDTO | null = null;
  selectedFile: File | null = null;
  searched: boolean = false;
  contract: ContractResponse | null = null;

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

    this.resiliationForm = this.fb.group({
      contractId: [null, Validators.required],
      description: ['', Validators.required]
    });

    this.contractSearchForm = this.fb.group({
      contractId: [null, Validators.required]
    });
  }

  searchContract(){
    if (this.contractSearchForm.invalid) {
      return;
    }
    this.searched = true;
    const id = this.contractSearchForm.get('contractId')?.value
    this.contractService.getContract(id).subscribe(
      (data) => {
      this.contract = data;
      this.resiliationForm.patchValue({"contractId" : this.contract.id});
    });

    this.contractService.getContract(id)
    .pipe(
      tap((response: any) => this.searchSuccess(response)),
      catchError((error: any) => this.searchError(error)),
      finalize(() => {})
    ).subscribe();
  }

  searchSuccess(response: any): void {
    this.contract = response;

    if(this.contract?.status != 'ENABLE'){
      this.messageService.add({
        severity:'warn', 
        summary: 'Echec', 
        detail: 'Ce contrat n\'est pas actif !', 
        life: 3000
      });
    }
  }

  searchError(error: any): never[] {
    console.log(error);
    this.contract = null;
    this.messageService.add({
      severity:'error', 
      summary: 'Echec', 
      detail: 'Ce contrat est introuvable !', 
      life: 3000
    });
    return [];
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] || null;
  }

  onSubmit(): void {
    if (this.resiliationForm.invalid) {
      this.messageService.add({severity:'error', summary: 'Echec', detail: "Veuillez remplir tous les champs du formulaire !"});
      return;
    }

    const contractId = this.resiliationForm.get('contractId')?.value;
    const motif = this.resiliationForm.get('description')?.value;

    this.contractService.revisionContract(contractId,motif)
    .subscribe(response => {
      //console.log('revision enregistrée avec succès !', response);
      this.messageService.add({severity:'success', summary: 'Success', detail: 'revision enregistrée avec succès !'});
    }, error => {
      console.log(error);
        this.messageService.add({severity:'error', summary: 'Echec', detail: 'Echec d\'enregistrement de la revision !'});
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

  getErrorMessage(controlName: string): string {
    const control = this.resiliationForm.get(controlName);
    if (control && control.invalid && (control.dirty || control.touched)) {
      if (control.errors?.['required']) {
        return 'Ce champ est requis';
      }
    }
    return '';
  }
}