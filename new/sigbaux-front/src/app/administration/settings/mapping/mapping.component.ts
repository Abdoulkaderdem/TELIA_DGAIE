// mapping.component.ts
import { Component, OnInit } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DataService } from 'src/app/services/data-services';

@Component({
  selector: 'app-mapping',
  templateUrl: './mapping.component.html',
  styleUrls: ['./mapping.component.css']
})
export class MappingComponent implements OnInit {
  mapForm!: FormGroup;

  regions: SelectItem[] = [];
  filteredProvincesOptions: SelectItem[] = [];
  
  provinces: SelectItem[] = [];
  filteredDepartementsOptions: SelectItem[] = [];
  
  departements: SelectItem[] = [];
  filteredSecteursOptions: SelectItem[] = [];
  
  secteurs: SelectItem[] = [];

  submittedData: any = null;

  constructor(
    private fb: FormBuilder,
    private dataService: DataService,
  ) {}

  ngOnInit(): void {
    this.mapForm = this.fb.group({
      region: ['', Validators.required],
      province: ['', Validators.required],
      commune: ['', Validators.required], // Correspond à Département
      city: ['', Validators.required], // Si applicable, sinon à ajuster
      district: ['', Validators.required], // Si applicable, sinon à ajuster
      sector: ['', Validators.required]
    });

    this.loadStaticData();
  }

  getFormValidationErrors(): string[] {
    const invalidControls: string[] = [];
    const controls = this.mapForm.controls;
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
    const errorMessages: string[] = [];
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

  onRegionChange(selectedRegion: any): void {
    if (selectedRegion) {
      this.filteredProvincesOptions = this.provinces.filter(province => province.value["Région"] === selectedRegion["Libéllé"]);
      // Réinitialiser les sélections dépendantes
      this.mapForm.patchValue({
        province: '',
        commune: '',
        sector: ''
      });
      this.filteredDepartementsOptions = [];
      this.filteredSecteursOptions = [];
    } else {
      this.filteredProvincesOptions = [];
      this.mapForm.patchValue({
        province: '',
        commune: '',
        sector: ''
      });
      this.filteredDepartementsOptions = [];
      this.filteredSecteursOptions = [];
    }
  }

  onProvinceChange(selectedProvince: any): void {
    if (selectedProvince) {
      this.filteredDepartementsOptions = this.departements.filter(departement => departement.value["Province"] === selectedProvince["Libéllé long"]);
      // Réinitialiser les sélections dépendantes
      this.mapForm.patchValue({
        commune: '',
        sector: ''
      });
      this.filteredSecteursOptions = [];
    } else {
      this.filteredDepartementsOptions = [];
      this.mapForm.patchValue({
        commune: '',
        sector: ''
      });
      this.filteredSecteursOptions = [];
    }
  }

  onDepartementChange(selectedDepartement: any): void {
    if (selectedDepartement) {
      this.filteredSecteursOptions = this.secteurs.filter(secteur => secteur.value["Departemenent/Commune"] === selectedDepartement["Libéllé long"]);
      // Réinitialiser les sélections dépendantes
      this.mapForm.patchValue({
        sector: ''
      });
    } else {
      this.filteredSecteursOptions = [];
      this.mapForm.patchValue({
        sector: ''
      });
    }
  }

  onSubmit(): void {
    if (this.mapForm.valid) {
      this.submittedData = this.mapForm.value;
      console.log('Données soumises:', this.mapForm.value);
      // Vous pouvez ajouter ici la logique pour traiter les données soumises
    } else {
      this.mapForm.markAllAsTouched();
    }
  }
}
