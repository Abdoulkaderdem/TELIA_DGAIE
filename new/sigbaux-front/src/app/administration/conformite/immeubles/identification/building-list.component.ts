// building-list.component.ts
import { Component, OnInit } from '@angular/core';
import { BuildingService } from 'src/app/services/building.service';
import { Building } from 'src/app/models/building.model';
import { MessageService, ConfirmationService } from 'primeng/api';
import { SelectItem } from 'primeng/api';
import { DataService } from 'src/app/services/data-services';

@Component({
  selector: 'app-building-list',
  templateUrl: './building-list.component.html',
  providers: [MessageService, ConfirmationService]
})

export class BuildingListComponent implements OnInit {
  buildings: Building[] = [];
  building: Building = {} as Building;
  selectedBuildings: Building[] = [];
  buildingDialog: boolean = false;
  deleteBuildingDialog: boolean = false;
  deleteBuildingsDialog: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];

  regions: SelectItem[] = [];
  selected_region: string = '';

  provinces: SelectItem[] = [];
  filteredProvincesOptions: SelectItem[] = [];
  selected_province: string = '';
  selected_typePropertyTitle: string = '';

  departements: SelectItem[] = [];
  filteredDepartementsOptions: SelectItem[] = [];
  selected_departement: string = '';

  secteurs: SelectItem[] = [];
  filteredSecteursOptions: SelectItem[] = [];
  selected_secteur: string = '';

  constructor(
    private buildingService: BuildingService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private dataService: DataService
  ) {}

  ngOnInit() {
    this.buildingService.getBuildings().subscribe(data => (this.buildings = data));
    this.loadStaticData();
    this.cols = [
      { field: 'typeBuilding', header: 'Type Building' },
      { field: 'typePropertyTitle', header: 'Property Title' },
      { field: 'buildingValue', header: 'Value' },
      { field: 'region', header: 'Region' },
      { field: 'province', header: 'Province' },
      { field: 'commune', header: 'Commune' },
      { field: 'city', header: 'City' },
      { field: 'district', header: 'District' },
      { field: 'sector', header: 'Sector' },
      { field: 'section', header: 'Section' },
      { field: 'ilot', header: 'Ilot' },
      { field: 'plot', header: 'Plot' },
      { field: 'street', header: 'Street' },
      { field: 'doornumber', header: 'Door Number' },
      { field: 'geolocation', header: 'Geolocation' },
      { field: 'rentPrice', header: 'Rent Price' },
      { field: 'code', header: 'Code' }
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

  loadStaticData(){
    this.dataService.getRegions().then(data => {
      this.regions = data.map(item => ({
        label: item["Libéllé"],
        value: item
      }));
    });

    this.dataService.getProvinces().then(data => {
      this.provinces = data.map(item => ({
        label: item["Libéllé long"],
        value: item
      }));
    });

    this.dataService.getDepartements().then(data => {
      this.departements = data.map(item => ({
        label: item["Libéllé long"],
        value: item
      }));
    });

    this.dataService.getSecteurs().then(data => {
      this.secteurs = data.map(item => ({
        label: item["Libéllé"],
        value: item
      }));
    });
  }

  onRegionChange(nameRegion: any): void {
    if (nameRegion) {
      this.filteredProvincesOptions = this.provinces.filter(province => province.value["Région"] == nameRegion["Libéllé"]);
    } else {
      this.filteredProvincesOptions = [];
    }
  }

  onProvinceChange(nameProvince: any): void {
    if (nameProvince) {
      this.filteredDepartementsOptions = this.departements.filter(departement => departement.value["Province"] == nameProvince["Libéllé long"]);
    } else {
      this.filteredDepartementsOptions = [];
    }
  }

  onDepartementChange(nameDepartement: any): void {
    if (nameDepartement) {
      this.filteredSecteursOptions = this.secteurs.filter(secteur => secteur.value["Departemenent\/Commune"] == nameDepartement["Libéllé long"]);
    } else {
      this.filteredSecteursOptions = [];
    }
  }
}
