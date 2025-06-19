import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class PrintService {

  api = environment.api;
  private apiUrl = `${environment.api}/print`;

  constructor(private http: HttpClient) { }

  /*printContract(idContract: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${idContract}/generate-contract`);
  }*/

  printContract(idContract: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${idContract}/generate-contract`, { responseType: 'blob' });
  }

  printRequest(idRequest: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${idRequest}/rental-requests`, { responseType: 'blob' });
  }
}