import { Component, OnInit, ViewChild } from '@angular/core';
import { OfferService } from 'src/app/services/offer.service';
import { RentalOffer } from 'src/app/interfaces/offer.interface';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rental-offer',
  templateUrl: './list-offres.component.html',
  providers: [MessageService, ConfirmationService]
})
export class RentalOfferComponent implements OnInit {
  rentalOffers: RentalOffer[] = [];
  rentalOfferDialog: boolean = false;
  rentalOffer: RentalOffer = {
    id: 0, 
    dateOffer: (new Date()).toISOString(), 
    rentalStatus: '', 
    code: '', 
    landLord: {
      id: null,
      dateCreated: null,
      lastUpdated: null,
      ifu: '',
      typeLandLord: '',
      status: '',
      qualityApplicant: '',
      firstname: '',
      lastname: '',
      companyName: '',
      bp: '',
      phoneNumber: '',
      whatsapp: '',
      emailAdress: '',
      residencePlace: ''
    }, 
    buildingDtos: []
  };

  @ViewChild('dt') table!: Table;

  constructor(
    private offerService: OfferService,
    private messageService: MessageService,
    private router: Router,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadRentalOffers();
  }

  onGlobalFilter(event: Event) {
    this.table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  loadRentalOffers(): void {
    this.offerService.getOffers().subscribe(data => this.rentalOffers = data);
  }

  openNew(): void {
    this.router.navigate(['/offres/new']);
  }

  editRentalOffer(rentalOffer: RentalOffer): void {
    this.router.navigate(['/offres/edit', rentalOffer.id]);
  }

  deleteRentalOffer(rentalOffer: RentalOffer): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer cette offre de location?',
      accept: () => {
        this.offerService.deleteOffer(rentalOffer.id).subscribe(() => {
          this.loadRentalOffers();
          this.messageService.add({severity:'success', summary: 'Succès', detail: 'Offre de location supprimée', life: 3000});
        });
      }
    });
  }
}
