import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private readonly apiUrl = `${environment.apiUrl}/inscriptions`;

  constructor(private http: HttpClient) { }

  sInscrire(hackathonId: string, reponses: any = null): Observable<any> {
    return this.http.post(`${this.apiUrl}/${hackathonId}`, reponses || {});
  }

  getMesInscriptions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mes-inscriptions`);
  }

  // LA MÉTHODE QUI MANQUAIT POUR LE COCKPIT
  getInscriptionsHackathon(hackathonId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/hackathon/${hackathonId}`);
  }
}
