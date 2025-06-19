import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommitteeMember } from '../interfaces/comitte';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})

export class CommitteeMemberService {
    api = environment.api;

    constructor(private http: HttpClient) { }

    private get_url(endpoint: string): string {
        return `${this.api}/committee/${endpoint}`;
    }

    getAllCommitteeMembers(): Observable<DataListResponse<CommitteeMember>> {
        return this.http.get<DataListResponse<CommitteeMember>>(this.get_url('all'));
    }

    getCommitteeMemberById(id: number): Observable<DataResponse<CommitteeMember>> {
        return this.http.get<DataResponse<CommitteeMember>>(this.get_url(`${id}`));
    }

    updateCommitteeMember(committeeMember: CommitteeMember): Observable<DataResponse<CommitteeMember>> {
        return this.http.put<DataResponse<CommitteeMember>>(this.get_url(`update`), committeeMember);
    }

    createCommitteeMember(committeeMember: CommitteeMember): Observable<DataResponse<CommitteeMember>> {
        return this.http.post<DataResponse<CommitteeMember>>(this.get_url(`create`), committeeMember);
    }
}