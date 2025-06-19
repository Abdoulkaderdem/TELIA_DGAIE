import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { BuildingService } from 'src/app/services/building.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ContractService } from 'src/app/services/contrat.service';
import { ContractDto, ContractResponse, ContractRevisionResponse } from 'src/app/interfaces/contrat';
import { catchError, finalize, tap, of } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-request-canceled',
  templateUrl: './request-canceled.component.html',
  providers: [MessageService, ConfirmationService]
})
export class RequestCanceledComponent {
  requests: Request[] = [];
  request: Request = {} as Request;
  rentalRequest: RentalRequest = {} as RentalRequest;
  selectedRequests: Request[] = [];
  showDialog: boolean = false;
  showContratForm: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];
  loyer: number | null = null;
  noBatiment: number | null = null;
  contract: ContractDto = { 
    buildingId: 0, 
    rentAmount: 0, 
    startDate: '', 
    endDate: null, 
    terms: '', 
    contractPeriodicity: '',
    bankAccountNumber: '',
    president: '',
    reporter: '',
    contractingAuthority: ''
  };

  constructor(
    private requestService: RequestService,
    private buildingService: BuildingService,
    private contratService: ContractService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.requestService.getRequestsSatisfactoryValidated()
      .subscribe(data => {
        console.log(data);
        this.requests = data;
      });

    this.cols = [
      { field: 'dateRequest', header: 'Date' },
      { field: 'structure', header: 'Structure' },
      { field: 'agentsNumber', header: 'Agents' },
      { field: 'managersNumber', header: 'Managers' }
    ];
  }

  editRequest(request: RentalRequest) {
    //this.request = { ...request };
    this.rentalRequest= { ...request };
    this.showDialog = true;
  }

  contratForm(request: Request) {
    this.request = { ...request };
    this.buildingService.getContratDetails(this.request.id)
      .subscribe(
        data => {
          this.noBatiment = data.idBuilding;
          this.loyer = data.provisionalRentAmount;
        }
      );
    this.showContratForm = true;
  }

  validateRequest() {
    this.contract.buildingId = this.noBatiment!;
    this.contract.rentAmount = this.loyer!;
    this.contract.startDate = this.formatDateToISO(this.contract.startDate);
    this.contract.endDate = this.contract.startDate;

    this.contratService.createContract(this.contract)
    .pipe(
      tap((response: any) => this.handleSuccess(response)),
      catchError((error: any) => this.handleError(error)),
      finalize(() => this.showContratForm = false)
    ).subscribe();
  }

  handleSuccess(response: any): void {
    this.requestService.getRequestsSatisfactoryValidated().subscribe(data => (this.requests = data));
    this.request = {} as Request;
    this.messageService.add({ 
      severity: 'success', 
      summary: 'Succès', 
      detail: 'Contrat généré avec succès !', 
      life: 3000 
    });
  }

  handleError(error: any): never[] {
    console.log(error);
    this.messageService.add({
      severity:'error', 
      summary: 'Echec', 
      detail: 'Echec de génération du contrat !', 
      life: 3000
    });
    return [];
  }

  hideDialog() {
    this.showDialog = false;
    this.submitted = false;
  }

  findIndexById(id: number): number {
    return this.requests.findIndex(req => req.id === id);
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.requests = this.requests.filter(req => req.description.toLowerCase().includes(value.toLowerCase()));
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  formatDateToISO(dateString: string): string {
    const [year, month, day] = dateString.split('-').map(Number);
    const date = new Date(year, month - 1, day);
    return date.toISOString();
  }
}
