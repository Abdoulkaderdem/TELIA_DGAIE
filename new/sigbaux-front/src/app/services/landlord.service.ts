import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LandLord } from '../interfaces/landlord';
import { RentalStatus } from '../interfaces/enumerations';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})

export class LandlordService {
    api = environment.api;

    constructor(private http: HttpClient) { }

    private get_url(endpoint: string): string {
        return `${this.api}/landlord/${endpoint}`;
    }

  getAllLandLords(): Observable<DataListResponse<LandLord>> {
    return this.http.get<DataListResponse<LandLord>>(this.get_url('all'));
  }

  getLandLordById(id: number): Observable<DataResponse<LandLord>> {
    return this.http.get<DataResponse<LandLord>>(this.get_url(`${id}`));
  }

  getLandLordByIfu(ifu: string): Observable<LandLord> {
    //const params = new HttpParams().set('ifu', ifu);
    //return this.http.get<DataResponse<LandLord>>(this.get_url(`search`), { params });
    return this.http.get<LandLord>(this.get_url(`${ifu}/search`));
  }

  updateLandLord(id: number, landLord: LandLord): Observable<DataResponse<LandLord>> {
    return this.http.put<DataResponse<LandLord>>(this.get_url(`${id}`), landLord, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  changeLandLordStatus(id: number, status: RentalStatus): Observable<void> {
    const params = new HttpParams().set('status', status.toString());
    return this.http.patch<void>(this.get_url(`${id}/status`), null, { params });
  }
}
