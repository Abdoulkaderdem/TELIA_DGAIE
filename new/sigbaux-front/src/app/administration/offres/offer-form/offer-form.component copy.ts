import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RentalOffer } from 'src/app/interfaces/offer.interface';
import { OfferService } from 'src/app/services/offer.service';
import { SelectItem, MessageService } from 'primeng/api';
import { Building } from 'src/app/interfaces/building';
import { TranslateService } from '@ngx-translate/core';
import { DataService } from 'src/app/services/data-services';
import { 
    TypeBuilding,
    TypePropertyTitle,
    BuildingStanding,
    TypeLandLord,
    QualityApplicant,
    RentalStatus,
    EnumUtils
} from 'src/app/interfaces/enumerations';

@Component({
  selector: 'app-offer-form',
  templateUrl: './offer-form.component.html',
  styleUrls: ['./offer-form.component.css'],
  providers: [MessageService]
})
export class OfferFormComponent implements OnInit {
  offerForm!: FormGroup;
  isEditMode: boolean = false;
  offerId: number | null = null;

  rental_status: SelectItem[] = [];
  quality_applicant: SelectItem[] = [];
  typePropertyTitles: SelectItem[] = [];
  typeBuildings: SelectItem[] = [];
  typeLandlords: SelectItem[] = [];
  buildingStandings: SelectItem[] = [];
  selected_landlord_type: string = '';
  selected_status: string = '';
  selected_quality: string = '';
  selected_typeBuilding: string= '';
  cols: any[] = [];
  buildingDialog: boolean = false;
  building!: Building;
  selectedBuildings: Building[] = [];
  selectedMulti: any[] = [];

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
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private offerService: OfferService,
    private translate: TranslateService,
    private dataService: DataService,
    private messageService: MessageService
  ) {
    this.loadTranslatedEnumValues();
  }

  ngOnInit(): void {
    this.offerId = +this.route.snapshot.paramMap.get('id')!;
    this.isEditMode = !!this.offerId;
    const currentDate = new Date().toISOString();

    this.offerForm = this.fb.group({
      id: [null],
      dateOffer: [currentDate, Validators.required],
      rentalStatus: ['AVAILABLE', Validators.required],
      code: ['NA', Validators.required],
      landLord: this.fb.group({
        id: [null],
        dateCreated: [''],
        lastUpdated: [null],
        ifu: ['', Validators.required],
        typeLandLord: ['', Validators.required],
        status: ['NEW', Validators.required],
        qualityApplicant: ['', Validators.required],
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
        companyName: [''],
        bp: [''],
        phoneNumber: ['', Validators.required],
        whatsapp: [''],
        emailAdress: ['', [Validators.required, Validators.email]],
        residencePlace: ['', Validators.required],
      }),
      buildingDtos: this.fb.array([]),
    });

    if (this.isEditMode) {
      this.loadOfferDetails(this.offerId!);
    }
    else{
      this.addBuilding();
    }

    this.loadStaticData();
  }

  loadOfferDetails(id: number): void {
    this.offerService.getOfferById(id).subscribe(
      (data: RentalOffer) => {
        this.offerForm.patchValue(data);
        this.setBuildings(data.buildingDtos);
      },
      error => {
        console.error('Error loading offer details', error);
      }
    );
  }

  setBuildings(buildings: any[]): void {
    const buildingFormGroups = buildings.map(building =>
      this.fb.group({
        id: [building.id],
        typeBuilding: [building.typeBuilding, Validators.required],
        typePropertyTitle: [building.typePropertyTitle, Validators.required],
        status: [building.status, Validators.required],
        buildingValue: [building.buildingValue, Validators.required],
        buildingArea: [building.buildingArea],
        otherInformation: [building.otherInformation],
        region: [building.region, Validators.required],
        province: [building.province, Validators.required],
        commune: [building.commune, Validators.required],
        city: [building.city, Validators.required],
        district: [building.district, Validators.required],
        sector: [building.sector, Validators.required],
        section: [building.section],
        ilot: [building.ilot],
        plot: [building.plot],
        street: [building.street],
        doornumber: [building.doornumber],
        geolocation: [building.geolocation],
        rentPrice: [building.rentPrice, Validators.required],
        code: [building.code, Validators.required],
        listOfferAndCharacteristicsDto: this.fb.array([building.listOfferAndCharacteristicsDto]),
        listLandlordOfferCharacteristics: this.fb.array([]),
        listBuildingStanding: [building.listBuildingStanding],
      })
    );

    const buildingFormArray = this.fb.array(buildingFormGroups);
    this.offerForm.setControl('buildingDtos', buildingFormArray);
    
  }

  get buildings(): FormArray {
    return this.offerForm.get('buildingDtos') as FormArray;
  }


  addBuilding(): void {
    const buildingFormGroup = this.fb.group({
      id: [null],
      typeBuilding: ['', Validators.required],
      typePropertyTitle: ['', Validators.required],
      status: ['AVAILABLE', Validators.required],
      buildingValue: ['', Validators.required],
      buildingArea: [null],
      otherInformation: [''],
      region: ['', Validators.required],
      province: ['', Validators.required],
      commune: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      sector: ['', Validators.required],
      section: [''],
      ilot: [''],
      plot: [''],
      street: [''],
      doornumber: [''],
      geolocation: [''],
      rentPrice: ['', Validators.required],
      code: ['NA', Validators.required],
      listOfferAndCharacteristicsDto: [[]],
      listBuildingStanding: [[]],
    });
    this.buildings.push(buildingFormGroup);
  }

  removeBuilding(index: number): void {
    this.buildings.removeAt(index);
  }

  /*addInspection(): void {
    const inspectionGroup = this.fb.group({
      id: [null],
      characteristics: ['', Validators.required],
      values: ['', Validators.required]
    });
    this.listRequestAndCharacteristicsDtos.push(inspectionGroup);
  }

  removeInspection(index: number): void {
    this.listRequestAndCharacteristicsDtos.removeAt(index);
  }*/

  onSubmit(): void {
    if (this.offerForm.valid) {
      const rentalOffer: RentalOffer = this.transformFormValue(this.offerForm.value);
      if (this.isEditMode) {
        this.offerService.updateOffer(rentalOffer).subscribe(
          response => {
            console.log('Offer updated successfully', response);
            this.router.navigate(['/offres/list-offres']);
          },
          error => {
            console.error('Error updating offer', error);
          }
        );
      } else {
        this.offerService.createOffer(rentalOffer).subscribe(
          response => {
            console.log('Offer created successfully', response);
            this.router.navigate(['/offres/list-offres']);
          },
          error => {
            console.error('Error creating offer', error);
          }
        );
      }
    } else {
        const invalidFields = this.getFormValidationErrors();
        const formValues = this.getFormValues();
        console.log(formValues);
        //alert(`Formulaire incomplet !\n\n${invalidFields.join('\n')}\n\nValeurs du formulaire:\n${formValues}`);
        //alert(`Formulaire incomplet !\n\nValeurs du formulaire:\n${formValues}`);
        this.messageService.add({severity:'error', summary: 'Echec', detail: 'Echec d\'enregistrement formulaire incomplet !', life: 3000});
        //alert("echec !")
    }
  }

  getFormValidationErrors(): string[] {
    const invalidControls = [];
    const controls = this.offerForm.controls;
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
    const rentalOffer: RentalOffer = this.transformFormValue(this.offerForm.value);
    return JSON.stringify(rentalOffer, null, 2);
  }

  transformFormValue(formValue: any): any {
    const transformedValue = { ...formValue };
    transformedValue.buildingDtos = formValue.buildingDtos.map((building: any) => ({
      ...building,
      region: building.region["Libéllé"] || building.region,
      province: building.province["Libéllé long"] || building.province,
      commune: building.commune["Libéllé long"] || building.commune,
      city: building.city["Libéllé"] || building.city
    }));
    return transformedValue;
  }

  loadTranslatedEnumValues(): void {
    this.translate.get('RentalStatus').subscribe(translations => {
      this.rental_status = EnumUtils.getEnumValues(RentalStatus).map(status => ({
        label: translations[status],
        value: status
      }));
    });

    this.translate.get('QualityApplicant').subscribe(translations => {
      this.quality_applicant = EnumUtils.getEnumValues(QualityApplicant).map(status => ({
        label: translations[status],
        value: status
      }));  
    });

    this.translate.get('TypeLandLord').subscribe(translations => {
      this.typeLandlords = EnumUtils.getEnumValues(TypeLandLord).map(status => ({
        label: translations[status],
        value: status
      }));
    });

    this.translate.get('TypePropertyTitle').subscribe(translations => {
      this.typePropertyTitles = EnumUtils.getEnumValues(TypePropertyTitle).map(status => ({
        label: translations[status],
        value: status
      }));
    });

    this.translate.get('BuildingStanding').subscribe(translations => {
      this.buildingStandings = EnumUtils.getEnumValues(BuildingStanding).map(status => ({
        label: translations[status],
        value: status
      }));
    });

    this.translate.get('TypeBuilding').subscribe(translations => {
      this.typeBuildings = EnumUtils.getEnumValues(TypeBuilding).map(status => ({
        label: status,
        value: status
      }));
    });
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
