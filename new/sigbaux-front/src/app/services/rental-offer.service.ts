import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RentalOffer } from '../interfaces/rental-offer';
import { RentalStatus } from '../interfaces/enumerations';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class RentalOfferService {
  api = environment.api;

  constructor(private http: HttpClient) {}

  private get_url(endpoint: string): string {
    return `${this.api}/rental/${endpoint}`;
  }

  // POST: Créer une nouvelle offre de location
  public createOffer(offer: RentalOffer): Observable<DataResponse<RentalOffer>> {
    return this.http.post<DataResponse<RentalOffer>>(this.get_url('create'), offer, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // GET: Obtenir la liste de toutes les offres de location
  public findAllOffer(): Observable<DataListResponse<RentalOffer>> {
    return this.http.get<DataListResponse<RentalOffer>>(this.get_url('all'));
  }

  // GET: Obtenir une offre de location par ID
  public findOfferById(id: number): Observable<DataResponse<RentalOffer>> {
    return this.http.get<DataResponse<RentalOffer>>(this.get_url(`${id}`));
  }

  // PUT: Mettre à jour une offre de location existante
  public updateRentalOffer(id: number, offer: RentalOffer): Observable<DataResponse<RentalOffer>> {
    return this.http.put<DataResponse<RentalOffer>>(this.get_url(`${id}`), offer, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // PATCH: Changer le statut d'une offre de location
  public changeRentalOfferStatus(id: number, status: RentalStatus): Observable<void> {
    return this.http.patch<void>(this.get_url(`${id}/status`), { status }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
}
