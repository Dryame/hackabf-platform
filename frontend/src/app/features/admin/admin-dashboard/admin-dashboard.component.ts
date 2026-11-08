import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService, UtilisateurAdmin } from '../../../core/services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {
  utilisateurs: UtilisateurAdmin[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.chargerUtilisateurs();
  }

  chargerUtilisateurs() {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.adminService.getAllUtilisateurs().subscribe({
      next: (data) => { 
        this.utilisateurs = data; 
        this.isLoading = false; 
      },
      error: (err) => { 
        console.error('Erreur API:', err);
        this.isLoading = false; 
        this.errorMessage = "Impossible de charger les utilisateurs. Vérifiez vos droits d'accès.";
      }
    });
  }

  changerRole(id: string, event: any) {
    const nouveauRole = event.target.value;
    this.adminService.changerRole(id, nouveauRole).subscribe({
      next: () => {
        this.chargerUtilisateurs(); // Recharger pour confirmer
      },
      error: (err) => alert("Erreur lors du changement de rôle : " + (err.error?.erreur || err.message))
    });
  }

  toggleStatus(id: string) {
    if (confirm("Voulez-vous vraiment changer le statut de cet utilisateur ?")) {
      this.adminService.toggleStatus(id).subscribe({
        next: () => {
          this.chargerUtilisateurs(); // Recharger pour confirmer
        },
        error: (err) => alert("Erreur : " + (err.error?.erreur || err.message))
      });
    }
  }
}
