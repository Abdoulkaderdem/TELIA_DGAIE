import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RentalRequest } from '../interfaces/rental-request';
import { RentalStatus } from '../interfaces/enumerations';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class RentalRequestService {
  api = environment.api;

  constructor(private http: HttpClient) { }

  private get_url(endpoint: string): string {
    return `${this.api}/request_rental/${endpoint}`;
  }

  createRentalRequest(dto: RentalRequest): Observable<DataResponse<RentalRequest>> {
    return this.http.post<DataResponse<RentalRequest>>(this.get_url('create'), dto, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  getRentalRequestById(id: number): Observable<DataResponse<RentalRequest>> {
    return this.http.get<DataResponse<RentalRequest>>(this.get_url(`${id}`));
  }

  getAllRentalRequests(): Observable<DataListResponse<RentalRequest>> {
    return this.http.get<DataListResponse<RentalRequest>>(this.get_url('all'));
  }

  updateRentalRequest(id: number, dto: RentalRequest): Observable<DataResponse<RentalRequest>> {
    return this.http.put<DataResponse<RentalRequest>>(this.get_url(`${id}`), dto, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  changeRentalRequestStatus(id: number, status: RentalStatus): Observable<void> {
    const params = new HttpParams().set('status', status.toString());
    return this.http.patch<void>(this.get_url(`${id}/status`), null, { params });
  }

  findApprovalRentalRequestsForStructure(structureId: number): Observable<DataListResponse<RentalRequest>> {
    return this.http.get<DataListResponse<RentalRequest>>(this.get_url(`approval/${structureId}/structure`));
  }

  attachBuildingsToRentalRequest(dto: any): Observable<any> {
    return this.http.post<any>(this.get_url('attach-buildings'), dto, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  validateDemandByOfficer(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/validate`), null);
  }

  sendValidateRentalRequest(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/send`), null);
  }

  needComplementRentalRequest(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/complement`), null);
  }

  approveDemandByDGAIE(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/approval`), null);
  }

  rejectDemandByCNOI(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/reject`), null);
  }

  heldDemandByCNOI(id: number): Observable<any> {
    return this.http.patch<any>(this.get_url(`status/${id}/held`), null);
  }
}
