import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hackathon } from '../models/hackathon.model';
import { PageResponse } from '../models/page-response.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HackathonService {
  private readonly apiUrl = `${environment.apiUrl}/public/hackathons`;
  private readonly secureApiUrl = `${environment.apiUrl}/hackathons`;
  private readonly uploadUrl = `${environment.apiUrl}/files/upload`;

  constructor(private http: HttpClient) { }

  // 1. Récupérer tous les hackathons (public)
  getHackathons(page: number = 0, size: number = 10): Observable<PageResponse<Hackathon>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<PageResponse<Hackathon>>(this.apiUrl, { params });
  }

  // 2. Récupérer un hackathon par ID (public)
  getHackathonById(id: string): Observable<Hackathon> {
    return this.http.get<Hackathon>(`${this.apiUrl}/${id}`);
  }

  // 3. Récupérer les vainqueurs pour le Hall of Fame (public)
  getVainqueurs(): Observable<Hackathon[]> {
    return this.http.get<Hackathon[]>(`${this.apiUrl}/vainqueurs`);
  }

  // 4. Uploader une image (authentifié)
  uploadFile(file: File): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{url: string}>(this.uploadUrl, formData);
  }

  // 5. Créer un hackathon (authentifié)
  changerStatut(id: string, statut: string, codeSecret: string): Observable<any> {
    return this.http.put(`${this.secureApiUrl}/${id}/statut`, { statut, codeSecret });
  }

  supprimerHackathon(id: string, codeSecret: string): Observable<any> {
    return this.http.delete(`${this.secureApiUrl}/${id}`, { body: { codeSecret } });
  }

  getHackathonsEnAttente(): Observable<Hackathon[]> {
    return this.http.get<Hackathon[]>(`${environment.apiUrl}/admin/hackathons/en-attente`);
  }

  validerHackathon(id: string): Observable<Hackathon> {
    return this.http.put<Hackathon>(`${environment.apiUrl}/admin/hackathons/${id}/valider`, {});
  }

  createHackathon(hackathonData: any): Observable<Hackathon> {
    return this.http.post<Hackathon>(this.secureApiUrl, hackathonData);
  }

  // 6. Récupérer SES propres hackathons (authentifié - POUR MON ESPACE)
  getMesHackathons(): Observable<Hackathon[]> {
    return this.http.get<Hackathon[]>(`${this.secureApiUrl}/mes-publications`);
  }

  // 7. Désigner le vainqueur (authentifié - POUR MON ESPACE)
  designerVainqueur(id: string, vainqueur: string, imageVainqueur: string): Observable<Hackathon> {
    return this.http.put<Hackathon>(`${this.secureApiUrl}/${id}/vainqueur`, { vainqueur, imageVainqueur });
  }
}
