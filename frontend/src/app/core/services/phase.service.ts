import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Phase {
  id?: string;
  nom: string;
  ordre: number;
  dateDebut?: string;
  dateFin?: string;
  statut?: string;
}

@Injectable({ providedIn: 'root' })
export class PhaseService {
  private http = inject(HttpClient);
  private apiUrl = '/api/hackathons';

  getPhases(hackathonId: string): Observable<Phase[]> {
    return this.http.get<Phase[]>(`${this.apiUrl}/${hackathonId}/phases`);
  }

  ajouterPhase(hackathonId: string, phase: Phase): Observable<Phase> {
    return this.http.post<Phase>(`${this.apiUrl}/${hackathonId}/phases`, phase);
  }

  activerPhase(hackathonId: string, phaseId: string): Observable<Phase> {
    return this.http.put<Phase>(`${this.apiUrl}/${hackathonId}/phases/${phaseId}/activer`, {});
  }

  supprimerPhase(hackathonId: string, phaseId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${hackathonId}/phases/${phaseId}`);
  }
}
