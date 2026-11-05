import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { CategorieService } from '../../../core/services/categorie.service';
import { Categorie } from '../../../core/models/categorie.model';

@Component({
  selector: 'app-create-hackathon',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-hackathon.component.html',
  styleUrl: './create-hackathon.component.scss'
})
export class CreateHackathonComponent implements OnInit {
  hackathonForm: FormGroup;
  categories: Categorie[] = [];
  isSubmitting = false;
  message = '';

  // Variables pour l'image
  selectedFile: File | null = null;
  imagePreviewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private hackathonService: HackathonService,
    private categorieService: CategorieService,
    private router: Router
  ) {
    this.hackathonForm = this.fb.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      lieu: ['', Validators.required],
      format: ['PRESENTIEL', Validators.required],
      lienInscription: ['', Validators.required],
      categorieId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.categorieService.getAllCategories().subscribe(data => this.categories = data);
  }

  // Méthode déclenchée quand on choisit une image
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      // Créer un aperçu de l'image
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreviewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.hackathonForm.invalid) return;
    this.isSubmitting = true;

    // Si une image a été sélectionnée, on l'upload d'abord
    if (this.selectedFile) {
      this.hackathonService.uploadFile(this.selectedFile).subscribe({
        next: (response) => {
          // On ajoute l'URL de l'image renvoyée par le backend au formulaire
          this.creerHackathon(`http://localhost:8080${response.url}`);
        },
        error: (err) => {
          this.message = "Erreur lors de l'upload de l'image.";
          this.isSubmitting = false;
        }
      });
    } else {
      // Si pas d'image, on crée directement
      this.creerHackathon(null);
    }
  }

  creerHackathon(imageUrl: string | null) {
    const data = this.hackathonForm.value;
    if (imageUrl) {
      data.imageUrl = imageUrl;
    }

    this.hackathonService.createHackathon(data).subscribe({
      next: () => {
        alert('Hackathon créé avec succès !');
        this.router.navigate(['/hackathons']);
      },
      error: (err) => {
        this.message = "Erreur lors de la création. Vérifiez vos droits (Éditeur/Admin).";
        this.isSubmitting = false;
      }
    });
  }
}
