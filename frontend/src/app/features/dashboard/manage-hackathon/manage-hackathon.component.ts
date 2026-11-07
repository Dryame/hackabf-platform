import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HackathonService } from '../../../core/services/hackathon.service';
import { InscriptionService } from '../../../core/services/inscription.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-manage-hackathon',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './manage-hackathon.component.html',
  styleUrl: './manage-hackathon.component.scss'
})
export class ManageHackathonComponent implements OnInit {
  hackathon: any = null;
  participants: any[] = [];
  isLoading = true;
  
  // Navigation interne du cockpit
  activeTab: string = 'overview'; // overview, participants, settings

  // Modales
  showDeleteModal = false;
  showStatusModal = false;
  showExportModal = false;
  
  codeSecretInput = '';
  nouveauStatut = '';
  errorMessage = '';

  // Export
  exportFormat = 'PDF';
  colonnesDisponibles = ['Nom', 'Email', 'Établissement', 'Date d\'inscription', 'Réponses Custom'];
  colonnesSelectionnees: string[] = ['Nom', 'Email'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hackathonService: HackathonService,
    private inscriptionService: InscriptionService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.hackathonService.getHackathonById(id).subscribe(data => {
        this.hackathon = data;
        this.chargerParticipants(id);
      });
    }
  }

  chargerParticipants(hackathonId: string) {
    this.inscriptionService.getInscriptionsHackathon(hackathonId).subscribe({
      next: (data) => { this.participants = data; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }

  setTab(tab: string) { this.activeTab = tab; }

  // Calcule les jours restants avant l'événement
  getJoursRestants(): number {
    if (!this.hackathon) return 0;
    const diff = new Date(this.hackathon.dateDebut).getTime() - new Date().getTime();
    return Math.max(0, Math.ceil(diff / (1000 * 3600 * 24)));
  }

  // --- ACTIONS SENSIBLES ---
  confirmerChangementStatut() {
    if (!this.codeSecretInput) return;
    this.hackathonService.changerStatut(this.hackathon.id, this.nouveauStatut, this.codeSecretInput).subscribe({
      next: (res) => {
        this.hackathon.statut = res.statut;
        this.showStatusModal = false;
        this.codeSecretInput = '';
        this.errorMessage = '';
      },
      error: (err) => this.errorMessage = err.error?.erreur || "Code invalide."
    });
  }

  confirmerSuppression() {
    if (!this.codeSecretInput) return;
    this.hackathonService.supprimerHackathon(this.hackathon.id, this.codeSecretInput).subscribe({
      next: () => {
        alert('Hackathon supprimé définitivement.');
        this.router.navigate(['/mon-espace']);
      },
      error: (err) => this.errorMessage = err.error?.erreur || "Code invalide."
    });
  }

  // --- EXPORTATION ---
  exporter() {
    const doc = new jsPDF();
    doc.text(`Participants : ${this.hackathon.titre}`, 14, 15);
    
    const body = this.participants.map(p => {
      const row = [];
      if (this.colonnesSelectionnees.includes('Nom')) row.push(p.etudiant.nomComplet);
      if (this.colonnesSelectionnees.includes('Email')) row.push(p.etudiant.email);
      if (this.colonnesSelectionnees.includes('Établissement')) row.push(p.etudiant.etablissement || '-');
      if (this.colonnesSelectionnees.includes('Date d\'inscription')) row.push(new Date(p.dateInscription).toLocaleDateString());
      if (this.colonnesSelectionnees.includes('Réponses Custom')) row.push(p.reponses ? 'Oui' : 'Non');
      return row;
    });

    autoTable(doc, {
      head: [this.colonnesSelectionnees],
      body: body,
      startY: 25,
      styles: { fontSize: 9 },
      headStyles: { fillColor: [79, 70, 229] }
    });

    doc.save(`participants_${this.hackathon.titre.replace(/ /g, '_')}.pdf`);
    this.showExportModal = false;
  }

  toggleColonne(col: string) {
    if (this.colonnesSelectionnees.includes(col)) {
      this.colonnesSelectionnees = this.colonnesSelectionnees.filter(c => c !== col);
    } else {
      this.colonnesSelectionnees.push(col);
    }
  }
}
