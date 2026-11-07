import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UtilisateurAdmin {
  id: string;
  nomComplet: string;
  email: string;
  etablissement: string;
  estActif: boolean;
  statutAccreditation: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly apiUrl = `${environment.apiUrl}/admin/utilisateurs`;

  constructor(private http: HttpClient) { }

  getAllUtilisateurs(): Observable<UtilisateurAdmin[]> {
    return this.http.get<UtilisateurAdmin[]>(this.apiUrl);
  }

  changerRole(id: string, role: string): Observable<UtilisateurAdmin> {
    return this.http.put<UtilisateurAdmin>(`${this.apiUrl}/${id}/role`, { role });
  }

  toggleStatus(id: string): Observable<UtilisateurAdmin> {
    return this.http.put<UtilisateurAdmin>(`${this.apiUrl}/${id}/status`, {});
  }
}
