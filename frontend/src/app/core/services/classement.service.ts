import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Classement {
  equipeId: string;
  nomEquipe: string;
  moyenne: number;
}

@Injectable({ providedIn: 'root' })
export class ClassementService {
  private http = inject(HttpClient);
  private apiUrl = '/api/hackathons';

  getClassement(hackathonId: string): Observable<Classement[]> {
    return this.http.get<Classement[]>(`${this.apiUrl}/${hackathonId}/classement`);
  }
}
