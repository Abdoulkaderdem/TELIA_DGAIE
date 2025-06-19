// building-list.component.ts
import { Component, OnInit, Input } from '@angular/core';
import { BuildingService } from 'src/app/services/building.service';
import { Building } from 'src/app/models/building.model';
import { MessageService, ConfirmationService } from 'primeng/api';
import { SelectItem } from 'primeng/api';
import { DataService } from 'src/app/services/data-services';


@Component({
  selector: 'app-building-estimate',
  templateUrl: './building-estimate.component.html',
  providers: [MessageService, ConfirmationService]
})
export class BuildingestimateComponent implements OnInit {
  @Input() requestID: number = 0;
  @Input() callbackFunction!: (loyer: number) => void;

  buildings: Building[] = [];
  building: Building = {} as Building;
  selectedBuildings: Building[] = [];
  buildingDialog: boolean = false;
  deleteBuildingDialog: boolean = false;
  deleteBuildingsDialog: boolean = false;
  submitted: boolean = false;
  loading: boolean = false;
  cols: any[] = [];
  loyer!: number;

  constructor(
    private buildingService: BuildingService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private dataService: DataService
  ) {}

  ngOnInit() {
    this.buildingService.getConformBuildings(this.requestID).subscribe(data => (this.buildings = data));
    this.cols = [
      { field: 'id', header: 'No' },
      { field: 'typeBuilding', header: 'Type Building' },
      { field: 'buildingValue', header: 'Value' },
      { field: 'region', header: 'Region' },
      { field: 'province', header: 'Province' },
      { field: 'commune', header: 'Commune' }
    ];
  }

  openNew() {
    this.building = {} as Building;
    this.submitted = false;
    this.buildingDialog = true;
  }

  deleteSelectedBuildings() {
    this.deleteBuildingsDialog = true;
  }

  editBuilding(building: Building) {
    this.building = { ...building };
    this.buildingDialog = true;
  }

  deleteBuilding(building: Building) {
    this.deleteBuildingDialog = true;
    this.building = { ...building };
  }

  confirmDeleteSelected() {
    this.deleteBuildingsDialog = false;
    this.buildings = this.buildings.filter(val => !this.selectedBuildings.includes(val));
    this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Buildings Deleted', life: 3000 });
    this.selectedBuildings = [];
  }

  confirmDelete() {
    this.deleteBuildingDialog = false;
    this.buildings = this.buildings.filter(val => val.id !== this.building.id);
    this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Building Deleted', life: 3000 });
    this.building = {} as Building;
  }

  hideDialog() {
    this.buildingDialog = false;
    this.submitted = false;
  }

  saveBuilding() {
    this.submitted = true;

    if (this.building.code?.trim()) {
      if (this.building.id) {
        this.buildingService.updateBuilding(this.building).subscribe(() => {
          this.buildings[this.findIndexById(this.building.id!)] = this.building;
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Building Updated', life: 3000 });
        });
      } else {
        this.buildingService.createBuilding(this.building).subscribe(data => {
          this.buildings.push(data);
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Building Created', life: 3000 });
        });
      }

      this.buildings = [...this.buildings];
      this.buildingDialog = false;
      this.building = {} as Building;
    }
  }

  findIndexById(id: number): number {
    return this.buildings.findIndex(req => req.id === id);
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.buildings = this.buildings.filter(req => req.code!.toLowerCase().includes(value.toLowerCase()));
  }

  onButtonClick(id: number) {
    //const loyerCalculé = this.calculerLoyer(this.requestID);
    this.loading = true;
    this.buildingService.getRentPrice(id).subscribe(data => {this.loyer = data; this.loading = false;});
  }

  calculerLoyer(requestID: number): number {
    return 2000000;
  }
}
