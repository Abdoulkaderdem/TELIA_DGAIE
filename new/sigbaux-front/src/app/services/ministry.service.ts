import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
    providedIn: 'root',
})

export class MinistryService {

    api = environment.api;

    constructor(private http: HttpClient) { }

    private get_url(endpoint: string): string {
        return `${this.api}/ministry/${endpoint}`;
    }

    // GET: Obtenir la liste de tous les ministères
    public getAll(): Observable<DataListResponse<Ministry>> {
        return this.http.get<DataListResponse<Ministry>>(this.get_url('all'));
    }

    // GET: Obtenir un ministère par ID
    public getById(id: number): Observable<DataResponse<Ministry>> {
        return this.http.get<DataResponse<Ministry>>(this.get_url(`${id}`));
    }

    // POST: Créer un nouveau ministère
    public create(ministry: Ministry): Observable<DataResponse<Ministry>> {
        return this.http.post<DataResponse<Ministry>>(this.get_url('create'), ministry, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    // PUT: Mettre à jour un ministère existant
    public update(id: number, ministry: Ministry): Observable<DataResponse<Ministry>> {
        return this.http.put<DataResponse<Ministry>>(this.get_url(`${id}`), ministry, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    // DELETE: Supprimer un ministère par ID
    public delete(id: number): Observable<DataResponse<Ministry>> {
        return this.http.delete<DataResponse<Ministry>>(this.get_url(`delete/${id}`));
    }
}