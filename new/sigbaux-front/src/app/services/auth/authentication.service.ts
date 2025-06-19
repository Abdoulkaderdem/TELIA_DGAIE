import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment.development';
import { TokenService } from '../token/token.service';
import { ICredential, PasswordChangeInterface } from 'src/app/interfaces/credentials';
import { TokenStatusInterface, TokenInterface, TokenResponse } from 'src/app/interfaces/token';
import { UserProfileResponse, UserProfileData } from 'src/app/interfaces/user_profile_response';
import { createGenericObserver } from '../../observers/generics';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  api = environment.api;
  public profile!: UserProfileData;

  constructor(
    private http: HttpClient,
    private token: TokenService
  ) {}

  get_url(endpoint: string): string {
    return `${this.api}/auth/${endpoint}`;
  }

  // Connexion et stockage du token
  public login(credential: ICredential): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.get_url('connexion'), credential).pipe(
      tap((response) => {
        if (response.access_token) {
          this.token.setToken(response.access_token);
        }
      })
    );
  }

  // Récupérer l'utilisateur authentifié
  public getAuthenticatedUser(): Observable<UserProfileResponse | null> {
    return this.http.get<UserProfileResponse>(this.get_url('me')).pipe(
      catchError((error) => {
        this.logout(); // Déconnecte si le token n'est plus valide
        return of(null);
      })
    );
  }

  // Vérifie si l'utilisateur est connecté
  public isLoggedIn(): boolean {
    return !!this.token.getToken();
  }

  // Déconnexion
  public logout(): void {
    this.token.clearToken();
    // this.http.post(this.get_url('logout'), {}).subscribe();
  }

  // Méthode de persistance de l'authentification
  public initializeAuthentication(): Observable<UserProfileResponse | null> {
    if (this.isLoggedIn()) {
      return this.getAuthenticatedUser();
    }
    return of(null);
  }

  public getProfile(callback: (data: UserProfileResponse) => void): void {
    this.http.get<UserProfileResponse>(this.get_url('me'))
    .subscribe(createGenericObserver(callback));
  }
}
