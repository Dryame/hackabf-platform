import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Inscription {
  id: string;
  dateInscription: string;
  reponses: string;
  hackathon: {
    id: string;
    titre: string;
    description: string;
    dateDebut: string;
    dateFin: string;
    lieu: string;
    statut: string;
    imageUrl: string;
    categorie: { nom: string };
  };
  equipe?: { id: string; nom: string };
}

@Injectable({ providedIn: 'root' })
export class EspaceParticipantService {
  private http = inject(HttpClient);
  private apiUrl = '/api/espace-participant';

  getMesInscriptions(): Observable<Inscription[]> {
    return this.http.get<Inscription[]>(`${this.apiUrl}/mes-inscriptions`);
  }
}
