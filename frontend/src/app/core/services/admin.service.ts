import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Hackathon } from '../models/hackathon.model';

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
  private readonly hackathonUrl = `${environment.apiUrl}/admin/hackathons`;

  constructor(private http: HttpClient) { }

  // Utilisateurs
  getAllUtilisateurs(): Observable<UtilisateurAdmin[]> {
    return this.http.get<UtilisateurAdmin[]>(this.apiUrl);
  }

  changerRole(id: string, role: string): Observable<UtilisateurAdmin> {
    return this.http.put<UtilisateurAdmin>(`${this.apiUrl}/${id}/role`, { role });
  }

  toggleStatus(id: string): Observable<UtilisateurAdmin> {
    return this.http.put<UtilisateurAdmin>(`${this.apiUrl}/${id}/status`, {});
  }

  // Modération Hackathons
  getAllHackathons(): Observable<Hackathon[]> {
    return this.http.get<Hackathon[]>(this.hackathonUrl);
  }

  supprimerHackathon(id: string): Observable<any> {
    return this.http.delete(`${this.hackathonUrl}/${id}`);
  }

  changerStatutHackathon(id: string, statut: string): Observable<Hackathon> {
    return this.http.put<Hackathon>(`${this.hackathonUrl}/${id}/statut`, { statut });
  }
}
