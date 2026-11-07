import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService, UtilisateurAdmin } from '../../../core/services/admin.service';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit {
  utilisateurs: UtilisateurAdmin[] = [];
  hackathonsEnAttente: Hackathon[] = [];
  isLoading = true;

  constructor(private adminService: AdminService, private hackathonService: HackathonService) {}

  ngOnInit() {
    this.chargerDonnees();
  }

  chargerDonnees() {
    this.adminService.getAllUtilisateurs().subscribe({
      next: (data) => { this.utilisateurs = data; this.isLoading = false; }
    });
    this.hackathonService.getHackathonsEnAttente().subscribe({
      next: (data) => { this.hackathonsEnAttente = data; }
    });
  }

  validerHackathon(id: string) {
    if(confirm('Valider cet événement ? Il sera visible sur le site public.')) {
      this.hackathonService.validerHackathon(id).subscribe({
        next: () => { this.chargerDonnees(); }
      });
    }
  }

  changerRole(id: string, event: any) {
    const role = event.target.value;
    this.adminService.changerRole(id, role).subscribe({ next: () => this.chargerDonnees() });
  }

  toggleStatus(id: string) {
    this.adminService.toggleStatus(id).subscribe({ next: () => this.chargerDonnees() });
  }
}
