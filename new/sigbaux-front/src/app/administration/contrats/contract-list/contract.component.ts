import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ContractService } from 'src/app/services/contrat.service';
import { ContractDto, ContractResponse, ContractRevisionResponse } from 'src/app/interfaces/contrat';
import { PrintService } from 'src/app/services/print.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-contract',
  templateUrl: './contract.component.html',
  providers: [MessageService, ConfirmationService]
})
export class ContractComponent implements OnInit {
  contracts: ContractResponse[] = [];
  contractDialog: boolean = false;
  contract: ContractDto = { 
    buildingId: 0, 
    rentAmount: 0, 
    startDate: '', 
    endDate: '', 
    terms: '',
    contractPeriodicity: '',
    bankAccountNumber: '',
    president: '',
    reporter: '',
    contractingAuthority: ''
  };
  selectedContracts: ContractResponse[] = [];
  selectedContract: ContractResponse | null = null;
  cols: any[] = [];
  fileToUpload: File | null = null;

  constructor(
    private contractService: ContractService,
    private printService: PrintService,
    private messageService: MessageService,
    private translate: TranslateService,
    private confirmationService: ConfirmationService
  ) {
    this.translate.setDefaultLang('fr');
  }

  ngOnInit(): void {
    this.loadContracts();

    this.cols = [
      { field: 'id', header: 'ID' },
      { field: 'buildingId', header: 'ID du Bâtiment' },
      { field: 'rentAmount', header: 'Montant du Loyer' },
      { field: 'startDate', header: 'Date de Début' },
      { field: 'endDate', header: 'Date de Fin' },
      { field: 'contractPeriodicity', header: 'Périodicité' },
      { field: 'status', header: 'Statut' }
    ];
  }

  loadContracts(): void {
    this.contractService.getAllContracts().subscribe((data) => {
      this.contracts = data;
    });
  }

  printContract(idContract: number): void {
    this.printService.printContract(idContract).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      const currentDate = new Date().toISOString();
      a.download = `contrat_${idContract}_${currentDate}.pdf`;
      a.click();
      URL.revokeObjectURL(objectUrl);
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Contrat téléchargé !', life: 3000 });
    }, error => {
      console.log(error);
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Echec de téléchargement du contrat', life: 3000 });
    });
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.fileToUpload = file;
    }
  }

  uploadFile(contratId: number): void {
    if (this.fileToUpload && this.selectedContract) {
      this.contractService.uploadContractFile(contratId, this.fileToUpload).subscribe(response => {
        this.loadContracts();
         this.messageService.add({severity:'success', summary: 'Success', detail: 'File uploaded successfully'});
         this.hideDialog();
      }, error => {
        console.log(error);
         this.messageService.add({severity:'error', summary: 'Error', detail: error});
      });
      console.log('File uploaded:', this.fileToUpload.name);
      this.hideDialog();
    }
  }

  viewContractDetails(contract: ContractResponse): void {
    this.selectedContract = contract;
    this.contractDialog = true;
  }

  hideDialog(): void {
    this.contractDialog = false;
    this.selectedContract = null;
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.contracts = this.contracts.filter(req => req.id!.toString().toLowerCase().includes(value.toLowerCase()));
  }
}
