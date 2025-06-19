import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { BuildingService } from 'src/app/services/building.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { catchError, finalize, tap, of } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-validation-conseil',
  templateUrl: './validation-conseil.component.html',
  providers: [MessageService, ConfirmationService]
})
export class ValidationConseilComponent {
  requests: Request[] = [];
  request: Request = {} as Request;
  rentalRequest: RentalRequest = {} as RentalRequest;
  selectedRequests: Request[] = [];
  showDialog: boolean = false;
  validateRequestDialog: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];
  loyer: number | null = null;
  noBatiment: number | null = null;

  constructor(
    private requestService: RequestService,
    private buildingService: BuildingService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.requestService.getRequestsSatisfactoryNotValidated().subscribe(data => (this.requests = data));

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

  valideRequestConfirmation(request: Request) {
    this.request = { ...request };
    this.buildingService.getRequestBuilding(this.request.id)
      .subscribe(
        data => {
          this.noBatiment = data.idBuilding;
          this.loyer = data.provisionalRentAmount;
        }
      );
    this.validateRequestDialog = true;
  }

  validateRequest() {
    this.buildingService.validerConseil(this.noBatiment!)
    .pipe(
      tap((response: any) => this.handleSuccess(response)),
      catchError((error: any) => this.handleError(error)),
      finalize(() => this.validateRequestDialog = false)
    ).subscribe();
  } 

  handleSuccess(response: any): void {
    this.requestService.getRequestsSatisfactoryNotValidated().subscribe(data => (this.requests = data));
    this.request = {} as Request;
    this.messageService.add({ 
      severity: 'success', 
      summary: 'Succès', 
      detail: 'Demande validée avec succès !', 
      life: 3000 
    });
  }

  handleError(error: any): never[] {
    console.log(error);
    this.messageService.add({
      severity:'error', 
      summary: 'Echec', 
      detail: 'Echec de validation de la demande !', 
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
}
