import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService, UtilisateurAdmin } from '../../../core/services/admin.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {
  activeTab: 'UTILISATEURS' | 'HACKATHONS' = 'UTILISATEURS';
  
  utilisateurs: UtilisateurAdmin[] = [];
  hackathons: Hackathon[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.chargerDonnees();
  }

  chargerDonnees() {
    this.isLoading = true;
    this.errorMessage = '';
    
    if (this.activeTab === 'UTILISATEURS') {
      this.adminService.getAllUtilisateurs().subscribe({
        next: (data) => { this.utilisateurs = data; this.isLoading = false; },
        error: () => { this.isLoading = false; this.errorMessage = "Impossible de charger les utilisateurs."; }
      });
    } else {
      this.adminService.getAllHackathons().subscribe({
        next: (data) => { this.hackathons = data; this.isLoading = false; },
        error: () => { this.isLoading = false; this.errorMessage = "Impossible de charger les hackathons."; }
      });
    }
  }

  setTab(tab: 'UTILISATEURS' | 'HACKATHONS') {
    this.activeTab = tab;
    this.chargerDonnees();
  }

  // Actions Utilisateurs
  changerRole(id: string, event: any) {
    this.adminService.changerRole(id, event.target.value).subscribe({
      next: () => this.chargerDonnees(),
      error: (err) => alert("Erreur : " + err.message)
    });
  }

  toggleStatus(id: string) {
    if (confirm("Changer le statut de cet utilisateur ?")) {
      this.adminService.toggleStatus(id).subscribe({
        next: () => this.chargerDonnees(),
        error: (err) => alert("Erreur : " + err.message)
      });
    }
  }

  // Actions Hackathons
  supprimerHackathon(id: string) {
    if (confirm("Voulez-vous vraiment supprimer ce hackathon ? Cette action est irréversible.")) {
      this.adminService.supprimerHackathon(id).subscribe({
        next: () => this.chargerDonnees(),
        error: (err) => alert("Erreur : " + err.message)
      });
    }
  }

  changerStatutHackathon(id: string, event: any) {
    this.adminService.changerStatutHackathon(id, event.target.value).subscribe({
      next: () => this.chargerDonnees(),
      error: (err) => alert("Erreur : " + err.message)
    });
  }
}
