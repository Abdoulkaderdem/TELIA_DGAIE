// building-list.component.ts
import { Component, OnInit } from '@angular/core';
import { BuildingService } from 'src/app/services/building.service';
import { Building } from 'src/app/models/building.model';
import { MessageService, ConfirmationService } from 'primeng/api';
import { DataService } from 'src/app/services/data-services';
import { SelectItem } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-building-attente-inspection',
  templateUrl: './attente.component.html',
  providers: [MessageService, ConfirmationService]
})
export class BuildingAttenteComponent implements OnInit {
  buildings: Building[] = [];
  building: Building = {} as Building;
  selectedBuildings: Building[] = [];
  buildingDialog: boolean = false;
  deleteBuildingDialog: boolean = false;
  deleteBuildingsDialog: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];

  constructor(
    private buildingService: BuildingService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private dataService: DataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.buildingService.getBuildingsToInspect().subscribe(data => (this.buildings = data));
    this.cols = [
      { field: 'id', header: 'No' },
      { field: 'typeBuilding', header: 'Type Building' },
      { field: 'buildingValue', header: 'Value' },
      { field: 'region', header: 'Region' },
      { field: 'province', header: 'Province' },
      { field: 'commune', header: 'Commune' },
      { field: 'city', header: 'City' },
      { field: 'district', header: 'District' }
    ];
  }

  openNew(building: Building) {
    this.building = building;
    this.buildingDialog = true;
  }

  inspecter(building: Building){
    this.building = { ...building };
    this.router.navigate([`/inspections/inspection-building/${this.building.id}`]);
    this.buildingDialog = false;
  }

  hideDialog() {
    this.buildingDialog = false;
    this.submitted = false;
  }

  findIndexById(id: number): number {
    return this.buildings.findIndex(req => req.id === id);
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.buildings = this.buildings.filter(req => req.code.toLowerCase().includes(value.toLowerCase()));
  }
}
