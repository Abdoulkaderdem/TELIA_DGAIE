// offer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { RentalOffer } from '../interfaces/offer.interface';

@Injectable({
  providedIn: 'root'
})
export class OfferService {

  private apiUrl = environment.api + '/rental';;

  constructor(private http: HttpClient) { }

  getOfferById(id: number): Observable<RentalOffer> {
    return this.http.get<RentalOffer>(`${this.apiUrl}/${id}`);
  }

  getOffers(): Observable<RentalOffer[]> {
    return this.http.get<RentalOffer[]>(`${this.apiUrl}/all`);
  }

  getOffer(id: number): Observable<RentalOffer> {
    return this.http.get<RentalOffer>(`${this.apiUrl}/${id}`);
  }

  createOffer(formData: FormData): Observable<RentalOffer> {
    return this.http.post<RentalOffer>(`${this.apiUrl}/create`, formData);
  }

  createOffer2(offer: RentalOffer): Observable<RentalOffer> {
    return this.http.post<RentalOffer>(`${this.apiUrl}/create`, offer);
  }

  updateOffer(offer: RentalOffer): Observable<RentalOffer> {
    return this.http.put<RentalOffer>(`${this.apiUrl}/${offer.id}`, offer);
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}



/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Offer } from 'src/app/models/offer.model';

@Injectable({
  providedIn: 'root'
})
export class OfferService {
  private apiUrl = 'http://45.14.194.123:8085/rental';

  constructor(private http: HttpClient) {}

  getOffers(): Observable<RentalOffer[]> {
    return this.http.get<RentalOffer[]>(`${this.apiUrl}/all`);
  }

  getOffer(id: number): Observable<RentalOffer> {
    return this.http.get<RentalOffer>(`${this.apiUrl}/${id}`);
  }

  createOffer(offer: Offer): Observable<RentalOffer> {
    return this.http.post<RentalOffer>(`${this.apiUrl}/create`, offer);
  }

  updateOffer(offer: Offer): Observable<RentalOffer> {
    return this.http.put<RentalOffer>(`${this.apiUrl}/${offer.id}`, offer);
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
*/