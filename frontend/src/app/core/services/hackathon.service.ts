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

  getHackathons(page: number = 0, size: number = 10): Observable<PageResponse<Hackathon>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<PageResponse<Hackathon>>(this.apiUrl, { params });
  }

  getHackathonById(id: string): Observable<Hackathon> {
    return this.http.get<Hackathon>(`${this.apiUrl}/${id}`);
  }

  getVainqueurs(): Observable<Hackathon[]> {
    return this.http.get<Hackathon[]>(`${this.apiUrl}/vainqueurs`);
  }

  // Nouvelle méthode pour uploader un fichier
  uploadFile(file: File): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{url: string}>(this.uploadUrl, formData);
  }

  createHackathon(hackathonData: any): Observable<Hackathon> {
    return this.http.post<Hackathon>(this.secureApiUrl, hackathonData);
  }
}
