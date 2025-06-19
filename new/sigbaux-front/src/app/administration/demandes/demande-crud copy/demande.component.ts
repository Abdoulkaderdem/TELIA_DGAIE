import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RentalRequestService, RentalRequestDto } from './demande.service';

@Component({
  selector: 'app-rental-requests',
  templateUrl: './demande.component.html',
  styleUrls: ['./demande.component.css']
})
export class RentalRequestsComponent implements OnInit {
  rentalForm: FormGroup;
  rentalRequests: RentalRequestDto[] = [];

  constructor(
    private fb: FormBuilder,
    private rentalRequestService: RentalRequestService
  ) {
    this.rentalForm = this.fb.group({
      dateRequest: ['', Validators.required],
      description: ['', Validators.required],
      status: ['', Validators.required],
      legalStatus: ['', Validators.required],
      motivationRequest: ['', Validators.required],
      structureCurrentPosition: ['', Validators.required],
      agentsNumber: ['', Validators.required],
      managersNumber: ['', Validators.required],
      desiredGeographicalLocation: ['', Validators.required],
      leasePortfolioMinistry: ['', Validators.required],
      buildingsOccupancyStatus: ['', Validators.required],
      listBuildingUsageDto: this.fb.array([]),
      structure: this.fb.group({
        id: [null, Validators.required],
        name: ['', Validators.required],
        domain: ['', Validators.required],
        phone: ['', Validators.required],
        email: ['', Validators.required],
        ministerInCharge: ['', Validators.required],
        nameMinistry: [''],
        code: ['', Validators.required],
      }),
      listCharacteristicsDto: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadRentalRequests();
  }

  loadRentalRequests(): void {
    this.rentalRequestService.getAllRequests().subscribe(
      (data) => {
        this.rentalRequests = data;
      },
      (error) => {
        console.error('Error loading rental requests', error);
      }
    );
  }

  onSubmit(): void {
    if (this.rentalForm.valid) {
      this.rentalRequestService.createRequest(this.rentalForm.value).subscribe(
        (data) => {
          this.rentalRequests.push(data);
          this.rentalForm.reset();
        },
        (error) => {
          console.error('Error creating rental request', error);
        }
      );
    }
  }
}
