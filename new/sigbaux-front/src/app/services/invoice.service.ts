import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InvoiceDTO,InvoiceResponse } from '../interfaces/invoice';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private apiUrl = `${environment.api}/invoice`;

  constructor(private http: HttpClient) { }

  // Create a new invoice
  createInvoice(formData: FormData): Observable<InvoiceResponse> {
    return this.http.post<InvoiceResponse>(`${this.apiUrl}/create`, formData);
  }

  // Create a new invoice
  createInvoice2(invoiceDto: InvoiceDTO, file: File): Observable<InvoiceResponse> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('invoiceDto', new Blob([JSON.stringify(invoiceDto)], { type: 'application/json' }));

    return this.http.post<InvoiceResponse>(`${this.apiUrl}/create`, formData);
  }

  // Get a specific invoice
  getInvoiceById(id: number): Observable<InvoiceResponse> {
    return this.http.get<InvoiceResponse>(`${this.apiUrl}/${id}`);
  }

  // Get all invoices for a contract
  getInvoicesByContractId(contractId: number): Observable<InvoiceResponse[]> {
    return this.http.get<InvoiceResponse[]>(`${this.apiUrl}/contract/${contractId}`);
  }

  // Update an invoice
  updateInvoice(id: number, invoiceDto: InvoiceDTO): Observable<InvoiceResponse> {
    return this.http.put<InvoiceResponse>(`${this.apiUrl}/${id}`, invoiceDto);
  }

  // Get all invoices
  getAllInvoices(): Observable<InvoiceResponse[]> {
    return this.http.get<InvoiceResponse[]>(`${this.apiUrl}/all`);
  }

  // Print a specific invoice
  printInvoice(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`,{ responseType: 'blob' });
  }
}
