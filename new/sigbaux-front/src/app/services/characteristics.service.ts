import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CharacteristicsUsage } from 'src/app/interfaces/characteristics-usage';
import { environment } from 'src/environments/environment.development';
import { createGenericObserver } from '../observers/generics';

@Injectable({
  providedIn: 'root'
})
export class CharacteristicsService {

  private apiUrl = `${environment.api}/rental_characteristics`;

  constructor(private http: HttpClient) { }

  create(typeUsage: CharacteristicsUsage, callback: (data: CharacteristicsUsage) => void): void {
    this.http.post<CharacteristicsUsage>(`${this.apiUrl}/create`, typeUsage)
      .subscribe(createGenericObserver(callback));
  }

  // Read
  getAll(callback: (data: CharacteristicsUsage[]) => void): void {
    this.http.get<CharacteristicsUsage[]>(`${this.apiUrl}/all`)
      .subscribe(createGenericObserver(callback));
  }

  getById(id: number, callback: (data: CharacteristicsUsage) => void): void {
    this.http.get<CharacteristicsUsage>(`${this.apiUrl}/${id}`)
      .subscribe(createGenericObserver(callback));
  }

  // Update
  update(id: number, typeUsage: CharacteristicsUsage, callback: (data: CharacteristicsUsage) => void): void {
    this.http.put<CharacteristicsUsage>(`${this.apiUrl}/${id}`, typeUsage)
      .subscribe(createGenericObserver(callback));
  }
}
