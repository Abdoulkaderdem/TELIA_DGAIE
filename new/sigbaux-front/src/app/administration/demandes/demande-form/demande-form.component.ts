import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { RentalStatus } from 'src/app/interfaces/enumerations';
import { RentalRequestService } from 'src/app/services/rental-request.service';
import { MinisterialStructure } from 'src/app/interfaces/ministerial-structure';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { StructureService } from 'src/app/services/ministerial-structure.service';
import { MinistryService } from 'src/app/services/ministry.service';
import { catchError, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { SelectItem, ConfirmationService, MessageService } from 'primeng/api';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { EnumUtils } from 'src/app/interfaces/enumerations';
import { TypeUsageService } from 'src/app/services/type-usage.service';
import { DataService } from 'src/app/services/data-services';
import { CharacteristicsService } from 'src/app/services/characteristics.service';
import { CharacteristicsUsage } from 'src/app/interfaces/characteristics-usage';
import { MenuItem } from 'primeng/api';


const initialRentalRequest: RentalRequest = {
  id: null,
  dateRequest: '',
  description: '',
  legalStatus: 'Active',
  motivationRequest: '',
  structureCurrentPosition: '',
  agentsNumber: '',
  managersNumber: '',
  region: '',
  province: '',
  commune: '',
  city: '',
  district: '',
  sector: '',
  regionDesired: '',
  provinceDesired: '',
  communeDesired: '',
  cityDesired: '',
  districtDesired: '',
  sectorDesired: '',
  leasePortfolioMinistry: '',
  buildingsOccupancyStatus: '',
  status: '',
  listBuildingUsageDto: [],
  structure: null,
  listRequestAndCharacteristics: [],
  numberOfRoom: 0,
  numberOfRoomMeeting: 0,
  numberOfBathroom: 0
};

@Component({
  selector: 'app-demande-form',
  templateUrl: './demande-form.component.html',
  styleUrls: ['./demande-form.component.css',],
  providers: [MessageService, ConfirmationService]
})
export class DemandeForm implements OnInit {
  rentalRequestForm!: FormGroup;
  ministeresOptions: SelectItem[] = [];
  structuresOptions: SelectItem[] = [];
  filteredStructuresOptions: SelectItem[] = [];
  buildings_occupancy_status: SelectItem[] = [];
  typeUsages: SelectItem[] = [];
  selected_occupancy_status: any;
  structures: MinisterialStructure[] = [];
  ministeres: Ministry[] = [];
  selectedMulti: any[] = [];

  regions: SelectItem[] = [];
  selected_region!: string;
  selected_region2!: string;

  provinces: SelectItem[] = [];
  filteredProvincesOptions: SelectItem[] = [];
  filteredProvincesOptions2: SelectItem[] = [];
  selected_province: string = '';
  selected_province2: string = '';

  departements: SelectItem[] = [];
  filteredDepartementsOptions: SelectItem[] = [];
  filteredDepartementsOptions2: SelectItem[] = [];
  selected_departement: string = '';
  selected_departement2: string = '';

  secteurs: SelectItem[] = [];
  filteredSecteursOptions: SelectItem[] = [];
  filteredSecteursOptions2: SelectItem[] = [];
  selected_secteur: string = '';
  selected_secteur2: string = '';

  items: SelectItem[] = [];
  charac: CharacteristicsUsage[] = [];
  onglets: MenuItem[] = [];
  activeIndex: number = 0;

  constructor(
    private fb: FormBuilder,
    private ministryService: MinistryService,
    private structureService: StructureService,
    private typeUsageService: TypeUsageService,
    private rentalRequestService: RentalRequestService,
    private characteristicsService: CharacteristicsService,
    private dataService: DataService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {

    this.onglets = [{
      label: 'Détails de Demande de Location',
      command: (event: any) => {
        this.activeIndex = 0;
        this.messageService.add({ severity: 'info', summary: 'Etape 1', detail: event.item.label });
      }
    },
    {
      label: 'Localisation géographique actuelle',
      command: (event: any) => {
        this.activeIndex = 1;
        this.messageService.add({ severity: 'info', summary: 'Etape 2', detail: event.item.label });
      }
    },
    {
      label: 'Localisation géographique souhaitée',
      command: (event: any) => {
        this.activeIndex = 2;
        this.messageService.add({ severity: 'info', summary: 'Etape 3', detail: event.item.label });
      }
    },
    {
      label: 'Informations complémentaires',
      command: (event: any) => {
        this.activeIndex = 3;
        this.messageService.add({ severity: 'info', summary: 'Etape 4', detail: event.item.label });
      }
    },

    {
      label: 'Confirmation',
      command: (event: any) => {
        this.activeIndex = 4;
        this.messageService.add({ severity: 'info', summary: 'Etape 5', detail: event.item.label });
      }
    }
    ];

    const currentDate = new Date().toISOString();

    this.rentalRequestForm = this.fb.group({
      id: [null],
      dateRequest: [currentDate, Validators.required],
      legalStatus: ['Public', Validators.required],
      structure: this.fb.group({
        id: [null, Validators.required],
        name: ['', Validators.required],
        domain: ['', Validators.required],
        phone: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        manager: [null],
        code: ['', Validators.required],
        idMinistry: [null, Validators.required]
      }),
      listBuildingUsageDto: [[], Validators.required],
      description: [null, Validators.required],
      motivationRequest: [null, Validators.required],
      structureCurrentPosition: ['NA', Validators.required],

      agentsNumber: [0, Validators.required],
      managersNumber: [0, Validators.required],
      leasePortfolioMinistry: [0, Validators.required],


      numberOfRoom: [0, Validators.required],
      numberOfRoomMeeting: [0, Validators.required],
      numberOfBathroom: [0, Validators.required],

      region: [null, Validators.required],
      province: [null, Validators.required],
      commune: [null, Validators.required],
      city: [null, Validators.required],
      district: [null, Validators.required],
      sector: [null, Validators.required],

      regionDesired: [null, Validators.required],
      provinceDesired: [null, Validators.required],
      communeDesired: [null, Validators.required],
      cityDesired: [null, Validators.required],
      districtDesired: [null, Validators.required],
      sectorDesired: [null, Validators.required],


      buildingsOccupancyStatus: ["Occupied", Validators.required],

      listRequestAndCharacteristicsDtos: this.fb.array([])
    });

    this.fetchMinistries();
    this.fetchStructures();
    this.fetchUsages();
    this.loadStaticData();
    this.loadItems();

    this.buildings_occupancy_status = EnumUtils.getEnumValues(RentalStatus)
      .map(status => ({
        label: status,
        value: status
      }));
  }

  get listRequestAndCharacteristicsDtos(): FormArray {
    return this.rentalRequestForm.get('listRequestAndCharacteristicsDtos') as FormArray;
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

  addInspection(): void {
    const inspectionGroup = this.fb.group({
      id: [null],
      characteristics: ['', Validators.required],
      values: ['', Validators.required]
    });
    this.listRequestAndCharacteristicsDtos.push(inspectionGroup);
  }

  removeInspection(index: number): void {
    this.listRequestAndCharacteristicsDtos.removeAt(index);
  }

  onBuildingsOccupancyStatusChange(selectedValues: any): void {
    this.rentalRequestForm.get('listBuildingUsageDto')?.setValue(selectedValues);
  }

  fetchStructures(): void {
    this.structureService.getAll()
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Error:', error);

          if (error.status === 302) {
            console.warn('Redirection detected, handling specific logic for status 302');
            if (error.error && Array.isArray(error.error)) {
              this.structures = error.error as MinisterialStructure[];
              this.structuresOptions = this.structures.map(structure => ({
                label: structure.name,
                value: structure
              }));
              this.filteredStructuresOptions = this.structuresOptions;
            }
          } else {
            console.error(`Unhandled error status: ${error.status}`);
          }
          return throwError(() => error);
        })
      )
      .subscribe(
        (response: DataListResponse<MinisterialStructure>) => {
          this.structures = response.data;
          this.structuresOptions = this.structures.map(structure => ({
            label: structure.name,
            value: structure
          }));
          this.filteredStructuresOptions = this.structuresOptions;
        },
        (error) => {
          console.error('Subscription error:', error);
        }
      );
  }

  fetchMinistries(): void {
    this.ministryService.getAll()
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Error:', error);

          if (error.status === 302) {
            console.warn('Redirection detected, handling specific logic for status 302');
            if (error.error && Array.isArray(error.error)) {
              this.ministeres = error.error as Ministry[];
              this.ministeresOptions = this.ministeres.map(ministere => ({
                label: ministere.name,
                value: ministere,
              }));
            }
          } else {
            console.error(`Unhandled error status: ${error.status}`);
          }
          return throwError(() => error);
        })
      )
      .subscribe(
        (response: DataListResponse<Ministry>) => {
          this.ministeres = response.data;
          this.ministeresOptions = this.ministeres.map(ministere => ({
            label: ministere.name,
            value: ministere,
          }));
        },
        (error) => {
          console.error('Subscription error:', error);
        }
      );
  }

  fetchUsages(): void {
    this.typeUsageService.getTypeUsages((data) => {
      this.typeUsages = data.map(item => ({
        label: item.libLong,
        value: item
      }));
    });
  }

  onMinistryChange(nameMinistry: any): void {
    if (nameMinistry) {
      this.filteredStructuresOptions = this.structuresOptions.filter(structure => structure.value.idMinistry === nameMinistry.id);
    } else {
      this.filteredStructuresOptions = this.structuresOptions;
    }
  }

  onStructureChange(selectedStructure: MinisterialStructure): void {
    if (selectedStructure) {
      this.rentalRequestForm.patchValue({ structure: selectedStructure });
    }
  }

  onSubmit(): void {

    if (this.rentalRequestForm.valid) {
      const rentalRequest: RentalRequest = this.rentalRequestForm.value;
      this.rentalRequestService.createRentalRequest(rentalRequest).subscribe(
        (response) => {
          console.log('Request submitted successfully', response);
          this.rentalRequestForm.reset();
          this.messageService.add({severity:'success', summary: 'Succès', detail: 'Demande crée avec succès !', life: 3000});
        },
        (error) => {
          console.error('Error submitting request', error);
          const invalidFields = this.getFormValidationErrors();
          const formValues = this.getFormValues();
          this.messageService.add({severity:'error', summary: 'Echec', detail: 'Echec d\'enregistrement de la demande !', life: 3000});
        }
      );
    } else {
      const invalidFields = this.getFormValidationErrors();
      const formValues = this.getFormValues();
      this.messageService.add({ severity: 'error', summary: 'Echec', detail: 'Echec d\'enregistrement formulaire incomplet !', life: 3000 });
    }
  }

  getFormValidationErrors(): string[] {
    const invalidControls = [];
    const controls = this.rentalRequestForm.controls;
    for (const name in controls) {
      if (controls[name].invalid) {
        const errors = this.getControlErrors(controls[name].errors);
        invalidControls.push(`${name}: ${errors}`);
      }
    }
    return invalidControls;
  }

  getControlErrors(errors: any): string {
    if (!errors) return '';
    const errorMessages = [];
    for (const errorName in errors) {
      switch (errorName) {
        case 'required':
          errorMessages.push('Ce champ est requis');
          break;
        case 'email':
          errorMessages.push('Email invalide');
          break;
        case 'minlength':
          errorMessages.push(`Minimum ${errors['minlength'].requiredLength} caractères requis`);
          break;
        default:
          errorMessages.push(`Erreur: ${errorName}`);
      }
    }
    return errorMessages.join(', ');
  }

  getFormValues(): string {
    const values = this.rentalRequestForm.value;
    return JSON.stringify(values, null, 2);
  }

  filteredItems(index: number): SelectItem[] {
    const selectedNames = this.listRequestAndCharacteristicsDtos.controls.map(control => control.get('name')?.value);
    return this.items.filter(item => !selectedNames.includes(item.value.libLong) || selectedNames[index] === item.value.libLong);
  }

  loadStaticData(): void {
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
    this.rentalRequestForm.get('region')?.setValue(nameRegion["Libéllé"]);
  }

  onRegionChange2(nameRegion: any): void {
    if (nameRegion) {
      this.filteredProvincesOptions2 = this.provinces.filter(province => province.value["Région"] == nameRegion["Libéllé"]);
    } else {
      this.filteredProvincesOptions2 = [];
    }
    this.rentalRequestForm.get('regionDesired')?.setValue(nameRegion["Libéllé"]);
  }

  onProvinceChange(nameProvince: any): void {
    if (nameProvince) {
      this.filteredDepartementsOptions = this.departements.filter(departement => departement.value["Province"] == nameProvince["Libéllé long"]);
    } else {
      this.filteredDepartementsOptions = [];
    }
    this.rentalRequestForm.get('province')?.setValue(nameProvince["Libéllé long"]);
  }

  onProvinceChange2(nameProvince: any): void {
    if (nameProvince) {
      this.filteredDepartementsOptions2 = this.departements.filter(departement => departement.value["Province"] == nameProvince["Libéllé long"]);
    } else {
      this.filteredDepartementsOptions2 = [];
    }
    this.rentalRequestForm.get('provinceDesired')?.setValue(nameProvince["Libéllé long"]);
  }

  onDepartementChange(nameDepartement: any): void {
    if (nameDepartement) {
      this.filteredSecteursOptions = this.secteurs.filter(secteur => secteur.value["Departemenent\/Commune"] == nameDepartement["Libéllé long"]);
    } else {
      this.filteredSecteursOptions = [];
    }
    this.rentalRequestForm.get('commune')?.setValue(nameDepartement["Libéllé long"]);
  }

  onDepartementChange2(nameDepartement: any): void {
    if (nameDepartement) {
      this.filteredSecteursOptions2 = this.secteurs.filter(secteur => secteur.value["Departemenent\/Commune"] == nameDepartement["Libéllé long"]);
    } else {
      this.filteredSecteursOptions2 = [];
    }
    this.rentalRequestForm.get('communeDesired')?.setValue(nameDepartement["Libéllé long"]);
  }

  initialRequest() {
    return initialRentalRequest;
  }

  getErrorMessage(controlName: string): string {
    const control = this.rentalRequestForm.get(controlName);
    if (control && control.invalid && (control.dirty || control.touched)) {
      if (control.errors?.['required']) {
        return 'Ce champ est requis';
      }
      if (control.errors?.['email']) {
        return 'Email invalide';
      }
      if (control.errors?.['minlength']) {
        return `Minimum ${control.errors['minlength'].requiredLength} caractères requis`;
      }
    }
    return '';
  }
}
