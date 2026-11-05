import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProjetService } from '../../../core/services/projet.service';
import { CategorieService } from '../../../core/services/categorie.service';
import { Categorie } from '../../../core/models/categorie.model';

@Component({
  selector: 'app-create-projet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './create-projet.component.html',
  styleUrl: './create-projet.component.scss'
})
export class CreateProjetComponent implements OnInit {
  projetForm: FormGroup;
  categories: Categorie[] = [];
  isSubmitting = false;
  message = '';

  selectedFile: File | null = null;
  imagePreviewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private projetService: ProjetService,
    private categorieService: CategorieService,
    private router: Router
  ) {
    this.projetForm = this.fb.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      typeProjet: ['PFA', Validators.required],
      niveauRequis: ['L3', Validators.required],
      anneeAcademique: ['2024-2025', Validators.required],
      categorieId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.categorieService.getAllCategories().subscribe(data => this.categories = data);
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => this.imagePreviewUrl = e.target.result;
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.projetForm.invalid) return;
    this.isSubmitting = true;

    if (this.selectedFile) {
      this.projetService.uploadFile(this.selectedFile).subscribe({
        next: (response) => this.creerProjet(`http://localhost:8080${response.url}`),
        error: () => {
          this.message = "Erreur lors de l'upload de l'image.";
          this.isSubmitting = false;
        }
      });
    } else {
      this.creerProjet(null);
    }
  }

  creerProjet(imageUrl: string | null) {
    const data = this.projetForm.value;
    if (imageUrl) data.imageUrl = imageUrl;

    this.projetService.createProjet(data).subscribe({
      next: () => {
        alert('Projet publié avec succès !');
        this.router.navigate(['/projets']);
      },
      error: () => {
        this.message = "Erreur lors de la création. Vérifiez vos droits.";
        this.isSubmitting = false;
      }
    });
  }
}
