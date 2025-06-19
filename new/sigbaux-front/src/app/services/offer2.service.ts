import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Offer } from 'src/app/models/offer.model';

@Injectable({
  providedIn: 'root'
})
export class OfferService {
  private apiUrl = 'http://45.14.194.123:8085/rental';

  constructor(private http: HttpClient) {}

  getOffers(): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.apiUrl}/all`);
  }

  getOffer(id: number): Observable<Offer> {
    return this.http.get<Offer>(`${this.apiUrl}/${id}`);
  }

  createOffer(offer: Offer): Observable<Offer> {
    return this.http.post<Offer>(`${this.apiUrl}/create`, offer);
  }

  updateOffer(offer: Offer): Observable<Offer> {
    return this.http.put<Offer>(`${this.apiUrl}/${offer.id}`, offer);
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}