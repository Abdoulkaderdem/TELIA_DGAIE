import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ValidationCommittee } from '../interfaces/comitte';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ValidationCommitteeService {

    api = environment.api;

    constructor(private http: HttpClient) { }

    private get_url(endpoint: string): string {
        return `${this.api}/committee/${endpoint}`;
    }

    getAllCommittees(): Observable<DataListResponse<ValidationCommittee>> {
        return this.http.get<DataListResponse<ValidationCommittee>>(this.get_url('all'));
    }

    getCommitteeById(id: number): Observable<DataResponse<ValidationCommittee>> {
        return this.http.get<DataResponse<ValidationCommittee>>(this.get_url(`${id}`));
    }

    createCommittee(committee: ValidationCommittee): Observable<DataResponse<ValidationCommittee>> {
        return this.http.post<DataResponse<ValidationCommittee>>(this.get_url('create'), committee);
    }

    updateCommittee(committee: ValidationCommittee): Observable<DataResponse<ValidationCommittee>> {
        return this.http.put<DataResponse<ValidationCommittee>>(this.get_url('update'), committee);
    }
}