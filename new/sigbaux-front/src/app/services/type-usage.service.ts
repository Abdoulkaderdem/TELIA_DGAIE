import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TypeUsage } from '../interfaces/type-usage';
import { environment } from 'src/environments/environment.development';
import { createGenericObserver } from '../observers/generics';

@Injectable({
  providedIn: 'root'
})
export class TypeUsageService {

  private apiUrl = `${environment.api}/typeusages`;

  constructor(private http: HttpClient) { }

  createTypeUsage(typeUsage: TypeUsage, callback: (data: TypeUsage) => void): void {
    this.http.post<TypeUsage>(`${this.apiUrl}/create`, typeUsage)
      .subscribe(createGenericObserver(callback));
  }

  // Read
  getTypeUsages(callback: (data: TypeUsage[]) => void): void {
    this.http.get<TypeUsage[]>(`${this.apiUrl}/all`)
      .subscribe(createGenericObserver(callback));
  }

  getTypeUsageById(id: number, callback: (data: TypeUsage) => void): void {
    this.http.get<TypeUsage>(`${this.apiUrl}/${id}`)
      .subscribe(createGenericObserver(callback));
  }

  // Update
  updateTypeUsage(id: number, typeUsage: TypeUsage, callback: (data: TypeUsage) => void): void {
    this.http.put<TypeUsage>(`${this.apiUrl}/${id}`, typeUsage)
      .subscribe(createGenericObserver(callback));
  }
}
