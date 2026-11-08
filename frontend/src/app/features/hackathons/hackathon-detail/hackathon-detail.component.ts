import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { HackathonService } from '../../../core/services/hackathon.service';
import { InscriptionService } from '../../../core/services/inscription.service';
import { EquipeService } from '../../../core/services/equipe.service';
import { AuthService } from '../../../core/services/auth.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-hackathon-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, FormsModule],
  templateUrl: './hackathon-detail.component.html',
  styleUrl: './hackathon-detail.component.scss'
})
export class HackathonDetailComponent implements OnInit {
  hackathon: Hackathon | null = null;
  isLoading = true;
  errorMessage = '';
  
  isInscrit = false;
  messageInscription = '';
  isLoadingInscription = false;
  
  customFields: any[] = [];
  dynamicForm: FormGroup;
  showFormModal = false;

  // Gestion des équipes
  showEquipeModal = false;
  nomEquipe = '';
  codeInvitation = '';
  equipeAction: 'CREER' | 'REJOINDRE' | 'INFO' = 'CREER';
  equipeMessage = '';
  monEquipe: any = null;

  constructor(
    private route: ActivatedRoute,
    private hackathonService: HackathonService,
    private inscriptionService: InscriptionService,
    private equipeService: EquipeService,
    public authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.dynamicForm = this.fb.group({});
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.hackathonService.getHackathonById(id).subscribe({
        next: (data) => {
          this.hackathon = data;
          this.isLoading = false;
          this.verifierInscription();
          this.initialiserFormulaireDynamique();
        },
        error: () => {
          this.errorMessage = "Impossible de charger ce hackathon.";
          this.isLoading = false;
        }
      });
    }
  }

  initialiserFormulaireDynamique() {
    if (this.hackathon?.typeInscription === 'INTERNE' && this.hackathon.formulaireConfig) {
      try {
        this.customFields = JSON.parse(this.hackathon.formulaireConfig);
        this.customFields.forEach(field => {
          this.dynamicForm.addControl(field.label, this.fb.control('', Validators.required));
        });
      } catch (e) { console.error('Erreur parsing', e); }
    }
  }

  verifierInscription(): void {
    if (this.authService.isLoggedIn() && this.hackathon) {
      this.inscriptionService.getMesInscriptions().subscribe({
        next: (inscriptions) => {
          const trouve = inscriptions.find(i => i.hackathon.id === this.hackathon?.id);
          if (trouve) {
            this.isInscrit = true;
            if (trouve.equipe) { this.monEquipe = trouve.equipe; }
          }
        }
      });
    }
  }

  ouvrirInscription(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    if (this.hackathon?.typeInscription === 'INTERNE' && this.customFields.length > 0) {
      this.showFormModal = true;
    } else {
      this.participer();
    }
  }

  participer(): void {
    if (!this.hackathon) return;
    this.isLoadingInscription = true;

    this.inscriptionService.sInscrire(this.hackathon.id, this.dynamicForm.value).subscribe({
      next: () => {
        this.isInscrit = true;
        this.isLoadingInscription = false;
        this.showFormModal = false;
        this.messageInscription = "Inscription réussie !";
        // Après l'inscription, on ouvre la modale d'équipe
        this.showEquipeModal = true;
      },
      error: (err) => {
        this.isLoadingInscription = false;
        this.messageInscription = err.error?.erreur || "Erreur lors de l'inscription.";
      }
    });
  }

  creerEquipe() {
    if (!this.hackathon || !this.nomEquipe) return;
    this.equipeService.creerEquipe(this.hackathon.id, this.nomEquipe).subscribe({
      next: (equipe) => {
        this.monEquipe = equipe;
        this.showEquipeModal = false;
        this.equipeMessage = '';
      },
      error: (err) => this.equipeMessage = err.error?.erreur || "Erreur lors de la création."
    });
  }

  rejoindreEquipe() {
    if (!this.codeInvitation) return;
    this.equipeService.rejoindreEquipe(this.codeInvitation).subscribe({
      next: (equipe) => {
        this.monEquipe = equipe;
        this.showEquipeModal = false;
        this.equipeMessage = '';
      },
      error: (err) => this.equipeMessage = err.error?.erreur || "Code invalide."
    });
  }

  partagerWhatsApp(): void {
    if (!this.hackathon) return;
    const url = window.location.href;
    const text = `🚀 Découvre le hackathon : ${this.hackathon.titre} sur HackaBF !\n${url}`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
  }
}
