import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {
  prochainsHackathons: Hackathon[] = [];
  vainqueurs: Hackathon[] = [];
  isLoading: boolean = true;
  
  // Statistiques
  stats = { hackathons: 0, utilisateurs: 0, partenaires: 0 };

  constructor(private hackathonService: HackathonService, private http: HttpClient) {}

  ngOnInit(): void {
    // 1. Récupérer les prochains hackathons
    this.hackathonService.getHackathons(0, 3).subscribe({
      next: (response) => {
        this.prochainsHackathons = response.content;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });

    // 2. Récupérer les vainqueurs
    this.hackathonService.getVainqueurs().subscribe({
      next: (data) => { this.vainqueurs = data; }
    });

    // 3. Récupérer les statistiques
    this.http.get<any>(`${environment.apiUrl}/public/stats`).subscribe({
      next: (data) => { this.stats = data; }
    });
  }
}
