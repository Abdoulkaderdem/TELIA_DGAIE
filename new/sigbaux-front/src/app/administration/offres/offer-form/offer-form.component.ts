import { Component, OnInit } from '@angular/core';
import { catchError, finalize, tap, of } from 'rxjs';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LandLord, RentalOffer } from 'src/app/interfaces/offer.interface';
import { OfferService } from 'src/app/services/offer.service';
import { SelectItem, MessageService } from 'primeng/api';
import { Building } from 'src/app/interfaces/building';
import { TranslateService } from '@ngx-translate/core';
import { DataService } from 'src/app/services/data-services';
import { CharacteristicsService } from 'src/app/services/characteristics.service';
import { CharacteristicsUsage } from 'src/app/interfaces/characteristics-usage';
import { LandlordService } from 'src/app/services/landlord.service';
import { TypeUsageService } from 'src/app/services/type-usage.service';
import { MenuItem } from 'primeng/api';
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
  searchForm!: FormGroup;
  isEditMode: boolean = false;
  offerId: number | null = null;

  typeUsages: SelectItem[] = [];
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

  items: SelectItem[] = [];
  charac: CharacteristicsUsage[] = [];
  onglets: MenuItem[] = [];
  activeIndex: number = 0;

  numeroIFU: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private offerService: OfferService,
    private translate: TranslateService,
    private dataService: DataService,
    private typeUsageService: TypeUsageService,
    private characteristicsService: CharacteristicsService,
    private messageService: MessageService,
    private landlordService: LandlordService
  ) {
    this.loadTranslatedEnumValues();
  }

  ngOnInit(): void {
    this.offerId = +this.route.snapshot.paramMap.get('id')!;
    this.fetchUsages();
    this.isEditMode = !!this.offerId;
    const currentDate = new Date().toISOString();
    this.searchForm = this.fb.group({
      ifu: ['', Validators.required]
    });
    this.offerForm = this.fb.group({
      id: [null],
      dateOffer: [currentDate, Validators.required],
      rentalStatus: ['AVAILABLE', Validators.required],
      code: ['NA', Validators.required],
      listBuildingOfferUsage: [[]],
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
      buildingDtos: this.fb.group({
        id: [null],
        typeBuilding: ['', Validators.required],
        typePropertyTitle: ['', Validators.required],
        status: ['AVAILABLE', Validators.required],
        buildingValue: ['0', Validators.required],
        buildingArea: [null],
        otherInformation: ['NA'],
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
        listBuildingStanding: [[]],
        numberOfRoom: [0, Validators.required],
        numberOfRoomMeeting: [0, Validators.required],
        numberOfBathroom: [0, Validators.required],
      }),
      listOfferAndCharacteristicsDto: this.fb.array([])
    });

    const landLordGroup = this.offerForm.get('landLord');
    if (landLordGroup) {
      landLordGroup.get('id')?.disable();
    }

    if (this.isEditMode) {
      this.loadOfferDetails(this.offerId!);
    }

    this.loadStaticData();
    this.loadItems();
  }

  loadOfferDetails(id: number): void {
    this.offerService.getOfferById(id).subscribe(
      (data: RentalOffer) => {
        this.offerForm.patchValue(data);
        //this.setBuildings(data.buildingDtos);
      },
      error => {
        console.error('Error loading offer details', error);
      }
    );
  }
  onBuildingsOccupancyStatusChange(selectedValues: any): void {
    this.offerForm.get('listBuildingOfferUsage')?.setValue(selectedValues);
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

  onSubmitSearch(): void {
    if (this.searchForm.valid) {
    var ifu = this.searchForm.get('ifu')!.value;
    this.landlordService.getLandLordByIfu(ifu)
    .pipe(
      tap((response: any) => {

        if(response!=null){
          this.offerForm.patchValue({'landLord' : response});
          this.messageService.add({ 
            severity: 'success', 
            summary: 'Succès', 
            detail: 'Numéro ifu trouvé !', 
            life: 3000 
          });
          this.disableLandLordForm();
        }
        else{
          this.messageService.add({ 
            severity: 'error', 
            summary: 'Echec', 
            detail: `Le numéro ifu (${ifu}) est introuvable dans la base de données !`, 
            life: 3000 
          });
        }
      }),
      catchError((error: any) => {
        this.messageService.add({ 
          severity: 'error', 
          summary: 'Echec', 
          detail: `Le numéro ifu (${ifu}) est introuvable dans la base de données !`, 
          life: 3000 
        });
        return of(null);
      }),
      finalize(() => {})
    ).subscribe();
  }
  }

  disableLandLordForm(): void {
    const landLordGroup = this.offerForm.get('landLord');
    if (landLordGroup) {
      landLordGroup.disable();
    }
  }

  fetchUsages(): void {
    this.typeUsageService.getTypeUsages((data) => {
      this.typeUsages = data.map(item => ({
        label: item.libLong,
        value: item
      }));
    });
  }

  resetLandLordForm(): void {
    const landLordGroup = this.offerForm.get('landLord');
    if (landLordGroup) {
      landLordGroup.enable();
      landLordGroup.reset({
        id: null,
        dateCreated: '',
        lastUpdated: null,
        ifu: '',
        typeLandLord: '',
        status: 'NEW',
        qualityApplicant: '',
        firstname: '',
        lastname: '',
        companyName: '',
        bp: '',
        phoneNumber: '',
        whatsapp: '',
        emailAdress: '',
        residencePlace: ''
      });
      landLordGroup.get('id')?.disable();
    }
  }

  get listOfferAndCharacteristicsDtos(): FormArray {
    return this.offerForm.get('listOfferAndCharacteristicsDto') as FormArray;
  }

  addDetails(): void {
    const inspectionGroup = this.fb.group({
      id: [null],
      characteristics: ['', Validators.required],
      values: ['', Validators.required]
    });
    this.listOfferAndCharacteristicsDtos.push(inspectionGroup);
  }

  removeDetails(index: number): void {
    this.listOfferAndCharacteristicsDtos.removeAt(index);
  }

  filteredItems(index: number): SelectItem[] {
    const selectedNames = this.listOfferAndCharacteristicsDtos.controls.map(control => control.get('name')?.value);
    return this.items.filter(item => !selectedNames.includes(item.value.libLong) || selectedNames[index] === item.value.libLong);
  }

  onSubmit(): void {
    if (this.offerForm.valid) {
      const rentalOffer: RentalOffer = this.transformFormValue(this.offerForm.value);
      console.log(JSON.stringify(rentalOffer, null, 2));
      if (this.isEditMode) {
        this.offerService.updateOffer(rentalOffer).subscribe(
          response => {
            const currentDate = new Date().toISOString();
            this.offerForm.reset({
              id: null,
              dateOffer: currentDate,
              rentalStatus: 'AVAILABLE',
              code: 'NA',
              listBuildingOfferUsage: [],
              landLord: {
                id: null,
                dateCreated: '',
                lastUpdated: null,
                ifu: '',
                typeLandLord: '',
                status: 'NEW',
                qualityApplicant: '',
                firstname: '',
                lastname: '',
                companyName: '',
                bp: '',
                phoneNumber: '',
                whatsapp: '',
                emailAdress: '',
                residencePlace: '',
              },
              buildingDtos: {
                id: null,
                typeBuilding: '',
                typePropertyTitle: '',
                status: 'AVAILABLE',
                buildingValue: '0',
                buildingArea: null,
                otherInformation: 'NA',
                region: '',
                province: '',
                commune: '',
                city: '',
                district: '',
                sector: '',
                section: '',
                ilot: '',
                plot: '',
                street: '',
                doornumber: '',
                geolocation: '',
                rentPrice: '',
                code: 'NA',
                listBuildingStanding: [],
                numberOfRoom: 0,
                numberOfRoomMeeting: 0,
                numberOfBathroom: 0,
              },
              listOfferAndCharacteristicsDto: this.fb.array([])
            });            
            this.messageService.add({ 
              severity: 'success', 
              summary: 'Succès', 
              detail: 'Offre mise à jour avec succès !', 
              life: 3000 
            });
            console.log('Offre enregistrée avec succès !', response);
            this.router.navigate(['/offres/list-offres']);
          },
          error => {
            console.error('Error updating offer', error);
            this.messageService.add({ 
              severity: 'error', 
              summary: 'Echec', 
              detail: 'Echec de mise à jour !', 
              life: 3000
            });
          }
        );
      } else {
        //const formData = this.convertRentalOfferToFormData(rentalOffer);
        this.offerService.createOffer2(rentalOffer).subscribe(
          response => {
            this.messageService.add({ 
              severity: 'success', 
              summary: 'Succès', 
              detail: 'Offre enregistrée avec succès !', 
              life: 3000 
            });
            console.log('Offer created successfully', response);
            //this.router.navigate(['/offres/list-offres']);
          },
          error => {
            this.messageService.add({ 
              severity: 'error', 
              summary: 'Echec', 
              detail: 'Echec de l\'enregistrement !', 
              life: 3000
            });
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
    const transformedValue = { 
      ...formValue
    };
    const b = { 
      ...transformedValue.buildingDtos,
      region: transformedValue.buildingDtos.region["Libéllé"] || transformedValue.buildingDtos.region,
      province: transformedValue.buildingDtos.province["Libéllé long"] || transformedValue.buildingDtos.province,
      commune: transformedValue.buildingDtos.commune["Libéllé long"] || transformedValue.buildingDtos.commune,
      city: transformedValue.buildingDtos.city["Libéllé"] || transformedValue.buildingDtos.city
    };
    transformedValue.buildingDtos = [ b ];
    /*transformedValue.buildingDtos = formValue.buildingDtos.map((building: any) => ({
      ...building,
      region: building.region["Libéllé"] || building.region,
      province: building.province["Libéllé long"] || building.province,
      commune: building.commune["Libéllé long"] || building.commune,
      city: building.city["Libéllé"] || building.city
    }));*/
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

  /*setBuildings(buildings: any[]): void {
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
    
  }*/

  getErrorMessage(controlName: string): string {
    const control = this.offerForm.get(controlName);
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

  convertRentalOfferToFormData(rentalOffer: RentalOffer): FormData {
    const formData = new FormData();

    // Ajoute les champs simples
    formData.append('id', rentalOffer.id?.toString() || '');
    formData.append('dateOffer', rentalOffer.dateOffer);
    formData.append('rentalStatus', rentalOffer.rentalStatus);
    formData.append('code', rentalOffer.code);

    // Ajoute les champs de LandLord
    formData.append('landLord.id', rentalOffer.landLord.id?.toString() || '');
    formData.append('landLord.dateCreated', rentalOffer.landLord.dateCreated || '');
    formData.append('landLord.lastUpdated', rentalOffer.landLord.lastUpdated || '');
    formData.append('landLord.ifu', rentalOffer.landLord.ifu);
    formData.append('landLord.typeLandLord', rentalOffer.landLord.typeLandLord);
    formData.append('landLord.status', rentalOffer.landLord.status);
    formData.append('landLord.qualityApplicant', rentalOffer.landLord.qualityApplicant);
    formData.append('landLord.firstname', rentalOffer.landLord.firstname);
    formData.append('landLord.lastname', rentalOffer.landLord.lastname);
    formData.append('landLord.companyName', rentalOffer.landLord.companyName);
    formData.append('landLord.bp', rentalOffer.landLord.bp);
    formData.append('landLord.phoneNumber', rentalOffer.landLord.phoneNumber);
    formData.append('landLord.whatsapp', rentalOffer.landLord.whatsapp);
    formData.append('landLord.emailAdress', rentalOffer.landLord.emailAdress);
    formData.append('landLord.residencePlace', rentalOffer.landLord.residencePlace);

    // Ajoute les BuildingDtos (il est nécessaire de gérer les tableaux)
    rentalOffer.buildingDtos.forEach(
        (building, index) => {
          formData.append(`buildingDtos[${index}].id`, building.id?.toString() || '');
          formData.append(`buildingDtos[${index}].typeBuilding`, building.typeBuilding);
          formData.append(`buildingDtos[${index}].typePropertyTitle`, building.typePropertyTitle);
          formData.append(`buildingDtos[${index}].status`, building.status);
          formData.append(`buildingDtos[${index}].buildingValue`, building.buildingValue);
          formData.append(`buildingDtos[${index}].buildingArea`, building.buildingArea || '');
          formData.append(`buildingDtos[${index}].otherInformation`, building.otherInformation);
          formData.append(`buildingDtos[${index}].region`, building.region);
          formData.append(`buildingDtos[${index}].province`, building.province);
          formData.append(`buildingDtos[${index}].commune`, building.commune);
          formData.append(`buildingDtos[${index}].city`, building.city);
          formData.append(`buildingDtos[${index}].district`, building.district);
          formData.append(`buildingDtos[${index}].sector`, building.sector);
          formData.append(`buildingDtos[${index}].section`, building.section);
          formData.append(`buildingDtos[${index}].ilot`, building.ilot);
          formData.append(`buildingDtos[${index}].plot`, building.plot);
          formData.append(`buildingDtos[${index}].street`, building.street);
          formData.append(`buildingDtos[${index}].doornumber`, building.doornumber);
          formData.append(`buildingDtos[${index}].geolocation`, building.geolocation);
          formData.append(`buildingDtos[${index}].rentPrice`, building.rentPrice);
          formData.append(`buildingDtos[${index}].code`, building.code);
      });

      return formData;
  }

}
