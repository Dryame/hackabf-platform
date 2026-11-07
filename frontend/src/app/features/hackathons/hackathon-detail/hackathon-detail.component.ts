import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HackathonService } from '../../../core/services/hackathon.service';
import { InscriptionService } from '../../../core/services/inscription.service';
import { AuthService } from '../../../core/services/auth.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-hackathon-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './hackathon-detail.component.html',
  styleUrl: './hackathon-detail.component.scss'
})
export class HackathonDetailComponent implements OnInit {
  hackathon: Hackathon | null = null;
  isLoading = true;
  errorMessage = '';
  
  // Gestion de l'inscription
  isInscrit = false;
  messageInscription = '';
  isLoadingInscription = false;
  
  // Gestion du formulaire dynamique
  customFields: any[] = [];
  dynamicForm: FormGroup;
  showFormModal = false;

  constructor(
    private route: ActivatedRoute,
    private hackathonService: HackathonService,
    private inscriptionService: InscriptionService,
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
        // On crée les contrôles du formulaire en fonction de la config JSON
        this.customFields.forEach(field => {
          this.dynamicForm.addControl(field.label, this.fb.control('', Validators.required));
        });
      } catch (e) {
        console.error('Erreur de parsing du formulaire', e);
      }
    }
  }

  verifierInscription(): void {
    if (this.authService.isLoggedIn() && this.hackathon) {
      this.inscriptionService.getMesInscriptions().subscribe({
        next: (inscriptions) => {
          const trouve = inscriptions.find(i => i.hackathon.id === this.hackathon?.id);
          if (trouve) this.isInscrit = true;
        }
      });
    }
  }

  // Déclenche l'ouverture de la modale si formulaire dynamique
  ouvrirInscription(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    
    if (this.hackathon?.typeInscription === 'INTERNE' && this.customFields.length > 0) {
      this.showFormModal = true;
    } else {
      this.participer(); // Si pas de champs custom, on s'inscrit direct
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
        this.messageInscription = "Inscription réussie ! Préparez-vous pour l'événement.";
      },
      error: (err) => {
        this.isLoadingInscription = false;
        this.messageInscription = err.error?.erreur || "Erreur lors de l'inscription.";
      }
    });
  }

  partagerWhatsApp(): void {
    if (!this.hackathon) return;
    const url = window.location.href;
    const text = `🚀 Découvre le hackathon : ${this.hackathon.titre} sur HackaBF !\n${url}`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
  }
}
