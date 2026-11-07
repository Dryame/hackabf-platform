import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
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
      categorieId: ['', Validators.required],
      typeInscription: ['EXTERNE', Validators.required],
      codeSecretHex: ['', [Validators.required, Validators.pattern('^[0-9A-Fa-f]{4,8}$')]],
      lienInscription: ['', Validators.required],
      // Le FormArray pour gérer les champs dynamiques
      champsPersonnalises: this.fb.array([])
    });
  }

  ngOnInit() {
    this.categorieService.getAllCategories().subscribe(data => this.categories = data);

    this.hackathonForm.get('typeInscription')?.valueChanges.subscribe(value => {
      const lienControl = this.hackathonForm.get('lienInscription');
      if (value === 'INTERNE') {
        lienControl?.clearValidators();
        lienControl?.setValue('');
      } else {
        lienControl?.setValidators([Validators.required]);
      }
      lienControl?.updateValueAndValidity();
    });
  }

  // --- GESTION DES CHAMPS DYNAMIQUES ---
  get champsPersonnalises() {
    return this.hackathonForm.get('champsPersonnalises') as FormArray;
  }

  ajouterChamp() {
    const champGroup = this.fb.group({
      label: ['', Validators.required],
      type: ['text', Validators.required] // text, number, url, email
    });
    this.champsPersonnalises.push(champGroup);
  }

  supprimerChamp(index: number) {
    this.champsPersonnalises.removeAt(index);
  }

  // --- UPLOAD ET SOUMISSION ---
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
    if (this.hackathonForm.invalid) return;
    this.isSubmitting = true;

    if (this.selectedFile) {
      this.hackathonService.uploadFile(this.selectedFile).subscribe({
        next: (response) => this.creerHackathon(`http://localhost:8080${response.url}`),
        error: () => {
          this.message = "Erreur lors de l'upload de l'image.";
          this.isSubmitting = false;
        }
      });
    } else {
      this.creerHackathon(null);
    }
  }

  creerHackathon(imageUrl: string | null) {
    const data = this.hackathonForm.value;
    
    if (imageUrl) data.imageUrl = imageUrl;

    // On convertit les champs personnalisés en JSON pour le Backend
    if (data.champsPersonnalises && data.champsPersonnalises.length > 0) {
      data.formulaireConfig = JSON.stringify(data.champsPersonnalises);
    } else {
      data.formulaireConfig = null;
    }
    delete data.champsPersonnalises; // On envoie que le JSON

    this.hackathonService.createHackathon(data).subscribe({
      next: () => {
        alert('Hackathon créé avec succès !');
        this.router.navigate(['/hackathons']);
      },
      error: () => {
        this.message = "Erreur lors de la création. Vérifiez vos droits.";
        this.isSubmitting = false;
      }
    });
  }
}
