import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { InscriptionService } from '../../../core/services/inscription.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-mon-espace',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './mon-espace.component.html',
  styleUrl: './mon-espace.component.scss'
})
export class MonEspaceComponent implements OnInit {
  activeTab: string = 'inscriptions';

  mesInscriptions: any[] = [];
  mesPublications: Hackathon[] = [];
  isLoading = true;
  
  // Statistiques pour le ministère
  totalInscriptions = 0;
  totalPublications = 0;
  totalVainqueurs = 0;

  selectedHackathon: Hackathon | null = null;
  nomVainqueur: string = '';
  imageVainqueur: string = '';

  constructor(
    private hackathonService: HackathonService,
    private inscriptionService: InscriptionService
  ) {}

  ngOnInit() {
    this.chargerDonnees();
  }

  chargerDonnees() {
    this.isLoading = true;
    
    this.inscriptionService.getMesInscriptions().subscribe({
      next: (data) => {
        this.mesInscriptions = data;
        this.totalInscriptions = data.length;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });

    this.hackathonService.getMesHackathons().subscribe({
      next: (data) => { 
        this.mesPublications = data; 
        this.totalPublications = data.length;
        this.totalVainqueurs = data.filter(h => h.vainqueur).length;
      },
      error: () => {}
    });
  }

  setTab(tab: string) { this.activeTab = tab; }

  ouvrirModaleVainqueur(hackathon: Hackathon) {
    this.selectedHackathon = hackathon;
    this.nomVainqueur = hackathon.vainqueur || '';
    this.imageVainqueur = hackathon.imageVainqueur || '';
  }

  fermerModale() { this.selectedHackathon = null; }

  enregistrerVainqueur() {
    if (!this.selectedHackathon || !this.nomVainqueur) return;
    this.hackathonService.designerVainqueur(this.selectedHackathon.id, this.nomVainqueur, this.imageVainqueur).subscribe({
      next: () => {
        alert('Vainqueur désigné avec succès !');
        this.fermerModale();
        this.chargerDonnees();
      },
      error: (err) => alert("Erreur : " + (err.error?.erreur || "Impossible de modifier"))
    });
  }
}
