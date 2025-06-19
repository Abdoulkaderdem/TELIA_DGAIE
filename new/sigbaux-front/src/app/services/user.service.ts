import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';
import { DataListResponse, DataResponse } from '../interfaces/request-response';
import { environment } from 'src/environments/environment.development';
import { createGenericObserver } from '../observers/generics';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  api = environment.api;

  constructor(private http: HttpClient) {}

  private get_url(endpoint: string): string {
    return `${this.api}/user/${endpoint}`;
  }

  getUsers(callback: (data: User[]) => void): void {
    this.http.get<User[]>(this.get_url('all'))
      .subscribe(createGenericObserver(callback));
  }

  createUser(user: User, callback: (data: User) => void): void {
    this.http.post<User>(this.get_url('inscription'), user)
      .subscribe(createGenericObserver(callback));
  }

  updateUser(user: User, callback: (data: User) => void): void {
    this.http.put<User>(this.get_url('update'), user)
      .subscribe(createGenericObserver(callback));
  }

  // POST: Inscription
  public create(user: User): Observable<DataResponse<User>> {
    return this.http.post<DataResponse<User>>(this.get_url('inscription'), user, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
  
  // GET: Obtenir la liste de tous les utilisateurs
  public getAll(): Observable<DataListResponse<User>> {
    return this.http.get<DataListResponse<User>>(this.get_url('all'));
  }

  // GET: Obtenir un utilisateur par ID
  public getById(id: number): Observable<DataResponse<User>> {
    return this.http.get<DataResponse<User>>(this.get_url(`${id}`));
  }

  // PUT: Mettre à jour le profil utilisateur
  public update(id: number, user: User): Observable<DataResponse<User>> {
    return this.http.put<DataResponse<User>>(this.get_url(`${id}`), user, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
}
