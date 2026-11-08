import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Equipe } from '../models/equipe.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EquipeService {
  private readonly apiUrl = `${environment.apiUrl}/equipes`;

  constructor(private http: HttpClient) { }

  creerEquipe(hackathonId: string, nom: string): Observable<Equipe> {
    return this.http.post<Equipe>(`${this.apiUrl}/${hackathonId}`, { nom });
  }

  rejoindreEquipe(code: string): Observable<Equipe> {
    return this.http.post<Equipe>(`${this.apiUrl}/rejoindre`, { code });
  }

  getEquipesParHackathon(hackathonId: string): Observable<Equipe[]> {
    return this.http.get<Equipe[]>(`${this.apiUrl}/hackathon/${hackathonId}`);
  }
}
