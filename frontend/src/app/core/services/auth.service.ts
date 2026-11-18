import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface RegisterRequest {
  nomComplet: string;
  email: string;
  motDePasse: string;
  telephone?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = '/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  // --- AUTHENTIFICATION ---
  login(credentials: LoginRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => { if (response && response.token) this.saveToken(response.token); })
    );
  }

  register(userData: RegisterRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => { if (response && response.token) this.saveToken(response.token); })
    );
  }

  // --- GESTION DU TOKEN ---
  getToken(): string | null { return localStorage.getItem('token'); }
  saveToken(token: string): void { localStorage.setItem('token', token); }
  removeToken(): void { localStorage.removeItem('token'); }
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const payload = this.getDecodedToken();
    if (!payload || typeof payload.exp !== 'number') return false;
    return payload.exp * 1000 > Date.now();
  }

  // --- DECONNEXION ---
  logout(): void {
    this.removeToken();
    this.router.navigate(['/login']);
  }

  // --- GESTION DES RÔLES ET UTILISATEUR ---
  private getDecodedToken(): any {
    const token = this.getToken();
    if (!token) return null;
    try { return JSON.parse(atob(token.split('.')[1])); } catch (e) { return null; }
  }

  getUserRole(): string {
    const payload = this.getDecodedToken();
    if (!payload) return '';
    let roleData = payload.role || payload.roles || payload.authorities;
    if (!roleData) return '';
    if (Array.isArray(roleData) && roleData.length > 0) {
      roleData = roleData[0];
      if (typeof roleData === 'object' && roleData.authority) {
        return roleData.authority;
      }
    }
    return String(roleData);
  }

  getUserRoles(): string[] {
    const payload = this.getDecodedToken();
    if (!payload) return [];
    const roleData = payload.role || payload.roles || payload.authorities || [];
    const values = Array.isArray(roleData) ? roleData : [roleData];
    return values.map(role =>
      typeof role === 'object' && role.authority ? role.authority : String(role)
    );
  }

  isAdmin(): boolean {
    return this.getUserRoles().some(role => role.includes('ROLE_ADMIN') || role === 'ADMIN');
  }

  isOrganizer(): boolean {
    const role = this.getUserRole();
    return role.includes('EDITEUR') || role.includes('ADMIN');
  }

  getUserInitials(): string {
    const payload = this.getDecodedToken();
    if (payload && payload.nomComplet) {
      const parts = payload.nomComplet.split(' ');
      if (parts.length > 1) return (parts[0][0] + parts[1][0]).toUpperCase();
      return parts[0][0].toUpperCase();
    }
    return 'US';
  }
}
