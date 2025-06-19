import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { MessageService, ConfirmationService } from 'primeng/api';
import { SelectItem } from 'primeng/api';
import { catchError, finalize, tap, of } from 'rxjs';
import { BuildingService } from 'src/app/services/building.service';
import { ContractService } from 'src/app/services/contrat.service';
import { Building } from 'src/app/models/building.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-calcul-loyer',
  templateUrl: './calcul-loyer.component.html',
  providers: [MessageService, ConfirmationService]
})
export class CalculLoyerComponent implements OnInit {
  requests: Request[] = [];
  request: Request = {} as Request;
  rentalRequest: RentalRequest = {} as RentalRequest;
  selectedRequests: Request[] = [];
  showDialog: boolean = false;
  showEstimateModal: boolean = false;
  showValiderLoyer: boolean = false;
  validateRequestDialog: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];
  buildings: Building[] = [];
  buildingsOptions: SelectItem[] = [];
  selectedBuilding: SelectItem = { value: 0 };
  buildingId!:number;
  loyer_indicatif!: number;
  loyer_arrete!: number;

  constructor(
    private requestService: RequestService,
    private buildingService: BuildingService,
    private contractService: ContractService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.requestService.getRequestsByStatus("BUILDING_CONFORM").subscribe(
      data => (this.requests = data)
    );

    this.cols = [
      { field: 'id', header: 'No' },
      { field: 'dateRequest', header: 'Date' },
      { field: 'structure', header: 'Structure' },
    ];
  }

  openNew() {
    this.request = {} as Request;
    this.submitted = false;
    this.showDialog = true;
  }

  requestModal(request: RentalRequest) {
    this.rentalRequest = { ...request };
    this.showDialog = true;
  }

  estimateModal(request: RentalRequest) {
    this.rentalRequest = { ...request };
    this.showEstimateModal = true;
  }

  validateModal(request: RentalRequest) {
    this.selectedBuilding = { value: 0 };
    this.loyer_indicatif= 0;
    this.loyer_arrete = 0;
    this.rentalRequest = { ...request };
    this.buildingService.getConformBuildings(request.id!).subscribe(data => {
      this.buildings = data;
      this.buildingsOptions = this.buildings.map(b => ({
        label: `Batiment ${b.id.toString()}`,
        value: b
      }));
    });

    this.showValiderLoyer = true;
  }

  onBuildingChange(building: Building){
    this.buildingId = building.id;
    this.buildingService.getRentPrice(this.buildingId).subscribe(
      data => {
        this.loyer_indicatif = data;
      }
    );
  }

  calculerLoyer(loyer: number) {
    this.loyer_indicatif = loyer;
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

  saveLoyer(): void {
    if(this.buildingId){
      this.contractService.provisionalRentAmount(this.buildingId,this.loyer_arrete!)
      .pipe(
        tap((response: any) => this.handleSuccess(response)),
        catchError((error: any) => this.handleError(error)),
        finalize(() => this.showValiderLoyer = false)
      ).subscribe();
    }
    else{
      this.handleError("echec");
    }
  }

  hideLoyerDialog(): void {
    this.showValiderLoyer = false;
  }

  isSaveEnabled() {
    return this.loyer_arrete <= this.loyer_indicatif && this.loyer_arrete > 0;
  }

  handleSuccess(response: any): void {
    this.requestService.getRequestsByStatus("BUILDING_CONFORM")
      .subscribe(data => (this.requests = data));
    this.messageService.add({ 
      severity: 'success', 
      summary: 'Succès', 
      detail: 'loyer validé avec succès !', 
      life: 3000 
    });
  }

  handleError(error: any): never[] {
    console.log(error);
    this.messageService.add({
      severity:'error', 
      summary: 'Echec', 
      detail: 'Echec de validation loyer !', 
      life: 3000
    });
    return [];
  }
}
