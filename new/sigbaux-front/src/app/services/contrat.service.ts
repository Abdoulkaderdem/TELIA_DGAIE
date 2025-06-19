import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContractDto,ContractResponse,ContractRevision,ContractRevisionResponse } from '../interfaces/contrat';
import { InvoiceDTO,InvoiceResponse } from '../interfaces/invoice';
import { BuildingWithProvisionalRentAmount   } from '../interfaces/building-with-provisional-rent-amount';
import { environment } from 'src/environments/environment.development';
import { SendRentalPrice } from 'src/app/models/building.model';

@Injectable({
  providedIn: 'root'
})
export class ContractService {

  api = environment.api;
  private apiUrl = `${environment.api}/contract`;

  constructor(private http: HttpClient) { }

  provisionalRentAmount(idBuilding:number,amount:number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${idBuilding}/provisional-rent-amount?provisionalRentAmount=${amount}`,null);
  }

  getBuildingsWithProvisionalRentAmount(): Observable<BuildingWithProvisionalRentAmount[]> {
    return this.http.get<BuildingWithProvisionalRentAmount[]>(`${this.apiUrl}/request-rental/make-contract`);
  }

  createContract(contractDto: ContractDto): Observable<ContractResponse> {
    return this.http.post<ContractResponse>(`${this.apiUrl}/create`, contractDto);
  }

  getContract(id: number): Observable<ContractResponse> {
    return this.http.get<ContractResponse>(`${this.apiUrl}/${id}`);
  }

  getIndemnity(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}/indemnity`);
  }

  updateContract(id: number, contractDto: ContractDto): Observable<ContractResponse> {
    return this.http.put<ContractResponse>(`${this.apiUrl}/${id}`, contractDto);
  }

  uploadContractFile(contractId: number, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.put<any>(`${this.apiUrl}/${contractId}/attach-document`, formData, { headers });
  }

  disableContract(id: number): Observable<ContractResponse> {
    return this.http.put<ContractResponse>(`${this.apiUrl}/${id}/disable`, {});
  }

  getAllContracts(): Observable<ContractResponse[]> {
    return this.http.get<ContractResponse[]>(`${this.apiUrl}/all`);
  }

  addContractRevision(contractId: number, revisionDetails: string): Observable<ContractRevisionResponse> {
    return this.http.post<ContractRevisionResponse>(`${this.apiUrl}/${contractId}/revisions`, { revisionDetails });
  }

  getContractRevisions(contractId: number): Observable<ContractRevisionResponse[]> {
    return this.http.get<ContractRevisionResponse[]>(`${this.apiUrl}/${contractId}/revisions`);
  }

  revisionContract(contractId: number,reasonForRevision: string): Observable<ContractResponse> {
    const formData = new FormData();
    formData.append('revisionDetails', reasonForRevision);
    return this.http.put<ContractResponse>(`${this.apiUrl}/renew/${contractId}/rental-amount`, formData);
  }

  terminateContract(contractId: number,reasonForTermination: string): Observable<ContractResponse> {
    const formData = new FormData();
    formData.append('reasonForTermination', reasonForTermination);
    return this.http.put<ContractResponse>(`${this.apiUrl}/${contractId}/terminate`, formData);
  }
}
