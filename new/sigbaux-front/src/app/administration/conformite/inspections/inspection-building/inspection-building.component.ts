import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Router, ActivatedRoute } from '@angular/router';
import { Building } from 'src/app/models/building.model';
import { BuildingService } from 'src/app/services/building.service';
import { CharacteristicsUsage } from 'src/app/interfaces/characteristics-usage';
import { CharacteristicsService } from 'src/app/services/characteristics.service';
import { BuildingForInspection } from 'src/app/interfaces/building-for-fnspection';
import { catchError, finalize, tap, of } from 'rxjs';
import { SelectItem } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import {
  ZONE,
  LOCALITY,
  BUILDING_STATUS,
  EnumUtils
} from 'src/app/interfaces/enumerations';

@Component({
  selector: 'app-inspection-building',
  templateUrl: './inspection-building.component.html',
  styleUrls: ['./inspection-building.component.css'],
  providers: [MessageService, ConfirmationService]
})

export class InspectionBuildingComponent implements OnInit {
  buildingId: number | null = null;
  building: Building | null = null;
  charac: CharacteristicsUsage[] = [];
  items: SelectItem[] = [];
  zone_list: SelectItem[] = [];
  locality_list: SelectItem[] = [];
  status_list: SelectItem[] = [];
  selected_zone!: string;
  selected_locality!: string;
  selected_status: string = BUILDING_STATUS.MATCHED;
  inspectionForm: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private buildingService: BuildingService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private translate: TranslateService,
    private characteristicsService: CharacteristicsService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.inspectionForm = this.fb.group({
      zone: [null, Validators.required],
      locality: [null, Validators.required],
      buildingArea: [0, Validators.required],
      status: [null, Validators.required],
      typePropertyTitle: [{ value: '', disabled: true }, Validators.required],
      buildingValue: [{ value: '', disabled: true }, Validators.required],
      region: [{ value: '', disabled: true }, Validators.required],
      province: [{ value: '', disabled: true }, Validators.required],
      commune: [{ value: '', disabled: true }, Validators.required],
      city: [{ value: '', disabled: true }, Validators.required],
      district: [{ value: '', disabled: true }, Validators.required],
      sector: [{ value: '', disabled: true }, Validators.required],
      rentPrice: [{ value: '', disabled: true }, Validators.required],
      code: [{ value: '', disabled: true }, Validators.required],
      listOfferAndCharacteristicsDto: this.fb.array([]),
      listLandlordOfferCharacteristics: []
      //listLandlordOfferCharacteristics: this.fb.array([])
    });
  }

  ngOnInit() {
    this.buildingId = +this.route.snapshot.paramMap.get('id')!;
    this.buildingService.getBuilding(this.buildingId).subscribe(data => {
      this.building = data;
      console.log(data);
    });
    this.loadItems();
    this.loadEnumValues();
  }

  get listOfferAndCharacteristicsDto(): FormArray {
    return this.inspectionForm.get('listOfferAndCharacteristicsDto') as FormArray;
  }

  addInspection(): void {
    const inspectionGroup = this.fb.group({
      id: [null],
      characteristics: ['', Validators.required],
      values: ['', Validators.required]
    });
    this.listOfferAndCharacteristicsDto.push(inspectionGroup);
  }

  removeInspection(index: number): void {
    this.listOfferAndCharacteristicsDto.removeAt(index);
  }

  onSubmit(): void {
    //console.log(this.getFormValues());
    if (this.inspectionForm.valid) {
      const report: BuildingForInspection = this.transformFormValue(this.inspectionForm.value);
      console.log(JSON.stringify(report, null, 2));
      this.sendForm(report);
    }
  }

  sendForm(inspection: BuildingForInspection): void {
    this.buildingService.inspectionBuilding(inspection)
    .pipe(
      tap((response: any) => this.handleSuccess(response)),
      catchError((error: any) => this.handleError(error)),
      finalize(() => this.submitting = false)
    ).subscribe();
  }

  handleSuccess(response: any): void {
    this.messageService.add({ 
      severity: 'success', 
      summary: 'Succès !', 
      detail: 'Rapport d\'inspection enregistré avec succès !', 
      life: 3000 
    });
  }

  handleError(error: any): never[] {
    console.log(error);
    this.messageService.add({
      severity:'error', 
      summary: 'Echec', 
      detail: error,//'Echec d\'inspection !', 
      life: 3000
    });
    return [];
  }

  getFormValues(): string {
    const rentalOffer: BuildingForInspection = this.transformFormValue(this.inspectionForm.value);
    return JSON.stringify(rentalOffer, null, 2);
  }

  loadItems(): void {
    this.characteristicsService.getAll((data) => {
      this.charac = data;
      this.items = this.charac.map(item => ({
        label: item.libLong,
        value: item
      }));
    });
  }

  filteredItems(index: number): SelectItem[] {
    const selectedNames = this.listOfferAndCharacteristicsDto.controls.map(control => control.get('name')?.value);
    return this.items.filter(item => !selectedNames.includes(item.value.libLong) || selectedNames[index] === item.value.libLong);
  }

  loadEnumValues(): void {
    this.zone_list = EnumUtils.getEnumValues(ZONE).map(zone => ({
      label: zone,
      value: zone
    }));

    this.locality_list = EnumUtils.getEnumValues(LOCALITY).map(locality => ({
      label: locality,
      value: locality
    }));

    

    this.translate.get("Status").subscribe(translations => {
      this.status_list = EnumUtils.getEnumValues(BUILDING_STATUS).map(status => ({
        label: translations[status],
        value: status
      }));
    });
  }

  transformFormValue(formValue: any): any {
    const transformedValue = {
      ...formValue,
      id: this.building?.id!,
      typePropertyTitle: this.building?.typePropertyTitle!,
      buildingValue: this.building?.buildingValue!,
      otherInformation: this.building?.otherInformation!,
      region: this.building?.region!,
      province: this.building?.province!,
      commune: this.building?.commune!,
      city: this.building?.city!,
      district: this.building?.district!,
      sector: this.building?.sector!,
      section: this.building?.section!,
      ilot: this.building?.ilot!,
      plot: this.building?.plot!,
      street: this.building?.street!,
      doornumber: this.building?.doornumber!,
      geolocation: this.building?.geolocation!,
      rentPrice: this.building?.rentPrice!,
      code: this.building?.code!,
      listBuildingStanding: this.building?.listBuildingStanding!,
      typeBuilding: this.building?.typeBuilding!,
      inspectionObservation: "RAS",
      listLandlordOfferCharacteristics: this.building?.listOfferAndCharacteristicsDto!
    };
    return transformedValue;
  }
}
