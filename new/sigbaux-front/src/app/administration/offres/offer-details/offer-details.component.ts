import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RentalOffer } from 'src/app/interfaces/offer.interface';
import { OfferService } from 'src/app/services/offer.service';

@Component({
  selector: 'app-offer-details',
  templateUrl: './offer-details.component.html',
  styleUrls: ['./offer-details.component.css']
})
export class OfferDetailsComponent implements OnInit {
  offer: RentalOffer | null = null;

  constructor(private route: ActivatedRoute, private offerService: OfferService) { }

  ngOnInit(): void {
    const offerId = +this.route.snapshot.paramMap.get('id')!;
    this.loadOfferDetails(offerId);
  }

  loadOfferDetails(id: number): void {
    this.offerService.getOfferById(id).subscribe(
      (data: RentalOffer) => {
        this.offer = data;
      },
      error => {
        console.error('Error loading offer details', error);
      }
    );
  }

  formatDate(date: string): string {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(date).toLocaleDateString(undefined, options);
  }
}
