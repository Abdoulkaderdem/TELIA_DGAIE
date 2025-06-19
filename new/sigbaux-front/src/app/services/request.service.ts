// request.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Request } from '../models/request.model';
import { environment } from 'src/environments/environment.development';
import { Building } from '../interfaces/building';
import { AttachBuildingsToRequestDto } from '../interfaces/attach-buildings-request';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  private apiUrl = environment.api + '/request_rental';

  constructor(private http: HttpClient) {}

  getRequests(): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.apiUrl}/all`);
  }

  getRequestsByStatus(status: string): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.apiUrl}/by-status/${status}`);
  }

  getRequestsSatisfactoryNotValidated(): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.apiUrl}/status/satisfactory-not-validated`);
  }

  getRequestsSatisfactoryValidated(): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.apiUrl}/status/satisfactory-validated`);
  }

  searchBuildings(idRequest: number): Observable<any> {
    return this.http.get<any>(`${environment.api}/building/filter-by-rentalRequest/${idRequest}`);
  }

  attachBuildings(data: AttachBuildingsToRequestDto): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/attach-buildings`,data);
  }

  changeRequestStatus(status: string, id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/status/${id}/${status}`,null);
  }

  getRequest(id: number): Observable<Request> {
    return this.http.get<Request>(`${this.apiUrl}/${id}`);
  }

  createRequest(request: Request): Observable<Request> {
    return this.http.post<Request>(`${this.apiUrl}/create`, request);
  }

  updateRequest(request: Request): Observable<Request> {
    return this.http.put<Request>(`${this.apiUrl}/update`, request);
  }

  rejectRequest(idRentalRequest: number,rejectionReason: string): Observable<Request> {
    return this.http.post<Request>(`${this.apiUrl}/status/${idRentalRequest}/reject`, rejectionReason);
  }

  deleteRequest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
