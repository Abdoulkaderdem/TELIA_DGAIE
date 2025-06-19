import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

export interface DashboardCountsResponse {
  buildingCount: number;
  rentalRequestCount: number;
  requestOfferCount: number;
  contractCount: number;
  invoiceCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private api = environment.api;

  constructor(private http: HttpClient) { }

  getDashboardCounts(): Observable<DashboardCountsResponse> {
    return this.http.get<DashboardCountsResponse>(`${this.api}/dashboard/get`);
  }
}
