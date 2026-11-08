import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { CategorieService } from '../../../core/services/categorie.service';
import { Categorie } from '../../../core/models/categorie.model';

@Component({
  selector: 'app-create-hackathon',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './create-hackathon.component.html',
  styleUrl: './create-hackathon.component.scss'
})
export class CreateHackathonComponent implements OnInit {
  vueActive: 'CHOIX' | 'HACKATHON' = 'CHOIX';
  hackathonForm: FormGroup;
  categories: Categorie[] = [];
  isSubmitting = false;
  message = '';

  selectedFile: File | null = null;
  imagePreviewUrl: string | null = null;

  showConfirmModal = false;
  secretCodeInput = '';

  constructor(
    private fb: FormBuilder,
    private hackathonService: HackathonService,
    private categorieService: CategorieService,
    private router: Router
  ) {
    this.hackathonForm = this.fb.group({
      titre: [''], description: [''], dateDebut: [''], dateFin: [''], lieu: [''],
      format: ['PRESENTIEL'], categorieId: [''], typeInscription: ['EXTERNE'],
      lienInscription: [''], champsPersonnalises: this.fb.array([])
    });
  }

  ngOnInit() {
    this.categorieService.getAllCategories().subscribe(data => this.categories = data);
  }

  get champsPersonnalises() {
    return this.hackathonForm.get('champsPersonnalises') as FormArray;
  }

  ajouterChamp() {
    this.champsPersonnalises.push(this.fb.group({ label: [''], type: ['text'] }));
  }

  supprimerChamp(index: number) {
    this.champsPersonnalises.removeAt(index);
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
    if (!this.selectedFile) {
      this.message = "L'affiche du hackathon est obligatoire. Veuillez sélectionner une image.";
      return;
    }
    this.showConfirmModal = true;
  }

  confirmerPublication() {
    if (!this.selectedFile) {
      this.message = "L'affiche est manquante.";
      return;
    }
    if (this.secretCodeInput.length < 4) {
      this.message = "Le code secret doit comporter au moins 4 caractères.";
      return;
    }

    this.isSubmitting = true;
    this.message = '';

    this.hackathonService.uploadFile(this.selectedFile).subscribe({
      next: (response) => this.creerHackathon(`http://localhost:8080${response.url}`),
      error: () => {
        this.message = "Erreur lors de l'upload de l'image.";
        this.isSubmitting = false;
      }
    });
  }

  creerHackathon(imageUrl: string) {
    const data = this.hackathonForm.value;
    data.imageUrl = imageUrl;
    data.codeSecretHex = this.secretCodeInput;
    
    if (data.champsPersonnalises && data.champsPersonnalises.length > 0) {
      data.formulaireConfig = JSON.stringify(data.champsPersonnalises);
    }
    delete data.champsPersonnalises;

    if (!data.dateDebut) data.dateDebut = null;
    if (!data.dateFin) data.dateFin = null;
    if (!data.categorieId) data.categorieId = null;

    this.hackathonService.createHackathon(data).subscribe({
      next: () => {
        this.showConfirmModal = false;
        alert('Hackathon publié avec succès !');
        this.router.navigate(['/hackathons']);
      },
      error: (err) => {
        this.message = "Erreur : " + (err.error?.erreur || "Vérifiez le code secret ou vos droits.");
        this.isSubmitting = false;
      }
    });
  }
}
