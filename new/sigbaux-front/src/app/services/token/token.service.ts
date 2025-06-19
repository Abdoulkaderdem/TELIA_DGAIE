import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private tokenKey = 'token'; // Clé pour le stockage du token

  constructor(private router: Router) {}

  // Sauvegarde et remplace le token
  saveToken(token: string): void {
    this.clearToken();
    localStorage.setItem(this.tokenKey, token);
  }

  // Sauvegarde et remplace le token
  setToken(token: string): void { // Renommée ici
    this.clearToken();
    localStorage.setItem(this.tokenKey, token);
  }

  // Méthodes pour ajouter et récupérer un élément local supplémentaire
  addLocalItem(key: string, value: string): void {
    localStorage.setItem(key, value);
  }

  getLocalItem(key: string): string | null {
    return localStorage.getItem(key);
  }

  // Vérifie si l'utilisateur est connecté
  isLogged(): boolean {
    const token = this.getToken();
    return Boolean(token);
  }

  // Déconnecte l'utilisateur
  logout(): void {
    this.clearToken();
    this.router.navigate(['auth']);
  }

  // Efface uniquement le token
  clearToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  // Récupère le token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Vérifie si le token est expiré (pour JWT avec date d'expiration)
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    const tokenPayload = JSON.parse(atob(token.split('.')[1]));
    const expiry = tokenPayload.exp * 1000;
    return Date.now() > expiry;
  }

  // Garde l'utilisateur connecté tant que le token est valide
  userAlwaysConnected(): void {
    if (!this.isLogged() || this.isTokenExpired()) {
      this.logout();
    }
  }
}
