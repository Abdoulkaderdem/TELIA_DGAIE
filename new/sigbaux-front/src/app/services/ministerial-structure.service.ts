import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MinisterialStructure } from 'src/app/interfaces/ministerial-structure';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
    providedIn: 'root',
})

export class StructureService {

    api = environment.api;

    constructor(private http: HttpClient) { }

    private get_url(endpoint: string): string {
        return `${this.api}/structure/${endpoint}`;
    }

    // GET: Obtenir la liste de tous les ministères
    public getAll(): Observable<DataListResponse<MinisterialStructure>> {
        return this.http.get<DataListResponse<MinisterialStructure>>(this.get_url('all'));
    }

    // GET: Obtenir un ministère par ID
    public getById(id: number): Observable<DataResponse<MinisterialStructure>> {
        return this.http.get<DataResponse<MinisterialStructure>>(this.get_url(`${id}`));
    }

    // PUT: Obtenir un ministère par ID
    public desactivate(id: number): Observable<DataResponse<MinisterialStructure>> {
        return this.http.get<DataResponse<MinisterialStructure>>(this.get_url(`${id}`));
    }

    // POST: Créer un nouveau ministère
    public create(MinisterialStructure: MinisterialStructure): Observable<DataResponse<MinisterialStructure>> {
        return this.http.post<DataResponse<MinisterialStructure>>(this.get_url('create'), MinisterialStructure, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    // PUT: Mettre à jour un ministère existant
    public update(id: number, MinisterialStructure: MinisterialStructure): Observable<DataResponse<MinisterialStructure>> {
        return this.http.put<DataResponse<MinisterialStructure>>(this.get_url(`${id}`), MinisterialStructure, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    // DELETE: Supprimer un ministère par ID
    public delete(id: number): Observable<DataResponse<MinisterialStructure>> {
        return this.http.delete<DataResponse<MinisterialStructure>>(this.get_url(`delete/${id}`));
    }
}