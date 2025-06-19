import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

export interface RentalRequestDto {
  id: number;
  dateRequest: string;
  description: string;
  status: string;
  legalStatus: string;
  motivationRequest: string;
  structureCurrentPosition: string;
  agentsNumber: number;
  managersNumber: number;
  desiredGeographicalLocation: string;
  leasePortfolioMinistry: string;
  buildingsOccupancyStatus: string;
  listBuildingUsageDto: { id: number; libCourt: string; libLong: string; }[];
  structure: {
    id: number;
    name: string;
    domain: string;
    phone: string;
    email: string;
    ministerInCharge: string;
    nameMinistry: string | null;
    code: string;
  };
  listCharacteristicsDto: { id: number; dataType: string; values: number; }[];
}

@Injectable({
  providedIn: 'root'
})
export class RentalRequestService {
  private apiUrl = environment.api;

  constructor(private http: HttpClient) { }

  createRequest(dto: RentalRequestDto): Observable<RentalRequestDto> {
    return this.http.post<RentalRequestDto>(`${this.apiUrl}/request_rental/create`, dto);
  }

  getRequestById(id: number): Observable<RentalRequestDto> {
    return this.http.get<RentalRequestDto>(`${this.apiUrl}/request_rental/${id}`);
  }

  getAllRequests(): Observable<RentalRequestDto[]> {
    return this.http.get<RentalRequestDto[]>(`${this.apiUrl}/request_rental/all`);
  }

  updateRequest(id: number, dto: RentalRequestDto): Observable<RentalRequestDto> {
    return this.http.put<RentalRequestDto>(`${this.apiUrl}/request_rental/${id}`, dto);
  }

  changeRequestStatus(id: number, status: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/request_rental/${id}/status`, { status });
  }
}
