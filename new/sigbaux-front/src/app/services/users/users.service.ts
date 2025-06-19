import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PasswordChangeInterface } from 'src/app/interfaces/credentials';
import { DataListResponse } from 'src/app/interfaces/data';
import { EUserType } from 'src/app/interfaces/enum-userType';
import { environment } from 'src/environments/environment.development';
import { UserProfileResponse,UserInterface } from 'src/app/interfaces/user_profile_response';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  api = environment.api;

  constructor(private http: HttpClient) { }

  public addUsersAdmin(newUser: UserInterface): Observable<UserInterface> {
    const url = `${this.api}accounts/register/`;
    return this.http.post<UserInterface>(url, newUser);
  }

  public addUsers(newUser: UserInterface): Observable<any> {
    const url = `${this.api}accounts/register/`;
    return this.http.post<any>(url, newUser);
  }

  public updateUser(editUser: UserInterface): Observable<any> {
    const url = `${this.api}accounts/update/`;
    return this.http.put<any>(url, editUser);
  }

  public getAllUsersByRole(role: string): Observable<UserInterface[]> {
    const url = `accounts/users?roles=${role}`;
    return this.http.get<UserInterface[]>(this.api + url);
  }


  public getAllUsers(): Observable<UserInterface[]> {
    const url = `accounts/users?`;
    return this.http.get<UserInterface[]>(this.api + url);
  }

  
  public getUser(id: number): Observable<UserInterface> {
    const url = `accounts/users/${id}`;
    return this.http.get<UserInterface>(this.api + url);
  }

  public getAllUsersNoPaginate(): Observable<any> {
    const url = `accounts/users/all`;
    console.log("URL-ALL: ", this.api + url);
    return this.http.get<any>(this.api + url);
  }

  public getAllUsersByType(type: EUserType, page=1): Observable<any> {
    const url = `accounts/users/?userType=${type}&page=${page}`;
    console.log("URL: ", this.api + url);
    return this.http.get<any>(this.api + url);
  }

  public getAllPublicUsers(): Observable<any> {
    const url = `accounts/users/public`;
    console.log("URL: ", this.api + url);
    return this.http.get<any>(this.api + url);
  }


  public getAllAdmin(): Observable<any> {
    const url = `accounts/users/admin`;
    console.log("URL: ", this.api + url);
    return this.http.get<any>(this.api + url);
  }


  public getAllUsersDesactivated(type: EUserType, page=1): Observable<any> {
    const url = `accounts/users/?userType=${type}&page=${page}`;
    console.log("URL: ", this.api + url);
    return this.http.get<any>(this.api + url);
  }

  
  getUserByCode(codeUsers: string): Observable<UserInterface> {
    const url = `${this.api}accounts/users/?codeUsers=`;
    return this.http.get<UserInterface>(url + codeUsers);
  }


  public updatePassword(token: string, pass: string): Observable<string> {
    const url = `${this.api}accounts/reset-password/`;
    return this.http.post<string>(url, { 'token': token, 'newPassword': pass });
  }


  public resetassword(email: string): Observable<string> {
    const url = `${this.api}accounts/request-reset-password/`;
    return this.http.post<string>(url, { 'email': email });
  }

  public desactivate(id: number) {
    return this.http.get<any>(this.api + '/' + id + '/desactivate')
  }

}
