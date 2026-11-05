import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {
  prochainsHackathons: Hackathon[] = [];
  vainqueurs: Hackathon[] = []; // LA VARIABLE MANQUANTE EST ICI
  isLoading: boolean = true;

  constructor(private hackathonService: HackathonService) {}

  ngOnInit(): void {
    // 1. Récupérer les prochains hackathons
    this.hackathonService.getHackathons(0, 3).subscribe({
      next: (response) => {
        this.prochainsHackathons = response.content;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });

    // 2. Récupérer les vainqueurs
    this.hackathonService.getVainqueurs().subscribe({
      next: (data) => {
        this.vainqueurs = data;
      },
      error: (err) => console.error("Erreur vainqueurs", err)
    });
  }
}
