import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-hackathon-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './hackathon-detail.component.html',
  styleUrl: './hackathon-detail.component.scss'
})
export class HackathonDetailComponent implements OnInit {
  hackathon: Hackathon | null = null;
  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private hackathonService: HackathonService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.hackathonService.getHackathonById(id).subscribe({
        next: (data) => {
          this.hackathon = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Erreur API:', err);
          this.errorMessage = "Impossible de charger ce hackathon. Il n'existe peut-être plus.";
          this.isLoading = false;
        }
      });
    } else {
      this.errorMessage = "ID du hackathon manquant.";
      this.isLoading = false;
    }
  }

  partagerWhatsApp(): void {
    if (!this.hackathon) return;
    const url = window.location.href;
    const text = `🚀 Découvre le hackathon : ${this.hackathon.titre} sur HackaBF !\n${url}`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
  }
}
