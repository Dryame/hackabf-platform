import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Projet } from '../models/projet.model';
import { PageResponse } from '../models/page-response.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjetService {
  private readonly apiUrl = `${environment.apiUrl}/public/projets`;
  private readonly secureApiUrl = `${environment.apiUrl}/projets`;
  private readonly uploadUrl = `${environment.apiUrl}/files/upload`;

  constructor(private http: HttpClient) { }

  getProjets(page: number = 0, size: number = 10): Observable<PageResponse<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<PageResponse<Projet>>(this.apiUrl, { params });
  }

  uploadFile(file: File): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{url: string}>(this.uploadUrl, formData);
  }

  createProjet(projetData: any): Observable<Projet> {
    return this.http.post<Projet>(this.secureApiUrl, projetData);
  }
}
