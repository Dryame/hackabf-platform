import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Champ {
  label: string;
  type: string;
  obligatoire: boolean;
  options?: string;
}

@Injectable({ providedIn: 'root' })
export class FormulaireService {
  private http = inject(HttpClient);
  private apiUrl = '/api/hackathons';

  configurerExterne(hackathonId: string, lien: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${hackathonId}/formulaire/externe`, { lienFormulaire: lien });
  }

  configurerInterne(hackathonId: string, champs: Champ[]): Observable<any> {
    return this.http.put(`${this.apiUrl}/${hackathonId}/formulaire/interne`, champs);
  }
}
