import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Building,SendRentalPrice } from 'src/app/models/building.model';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { BuildingForInspection } from 'src/app/interfaces/building-for-fnspection';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {
  private apiUrl = environment.api + '/building';

  constructor(private http: HttpClient) {}

  getBuildings(): Observable<Building[]> {
    return this.http.get<Building[]>(`${this.apiUrl}/all`);
  }

  getConformBuildings(idRequest: number): Observable<Building[]> {
    return this.http.get<Building[]>(`${this.apiUrl}/rental-request/${idRequest}/conform`);
  }

  getBuildingsToInspect(): Observable<Building[]> {
    return this.http.get<Building[]>(`${this.apiUrl}/all/for-inspection`);
  }

  getBuilding(id: number): Observable<Building> {
    return this.http.get<Building>(`${this.apiUrl}/${id}`);
  }

  getRentPrice(id: number): Observable<any> {
    return this.http.get<any>(`${environment.api}/rentPrices/${id}/estimate-rent`);
  }

  inspectionBuilding(report: BuildingForInspection){
    let rapport : BuildingForInspection[] = [ report ];
    return this.http.patch<void>(`${this.apiUrl}/inspection-finish/report`,rapport);
  }

  validerConseil(id: number): Observable<any> {
    return this.http.patch<void>(`${this.apiUrl}/provisional-rent-amount/${id}/validate`,null);
  }

  getRequestBuilding(idRequest: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/rental-request/${idRequest}/no-examaminated-council`);
  }

  getContratDetails(idRequest: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/rental-request/${idRequest}/provisional-amount`);
  }

  createBuilding(building: Building): Observable<Building> {
    return this.http.post<Building>(`${this.apiUrl}/create`, building);
  }

  updateBuilding(building: Building): Observable<Building> {
    return this.http.put<Building>(`${this.apiUrl}/update`, building);
  }

  deleteBuilding(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
