import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProjetService } from '../../../core/services/projet.service';
import { Projet } from '../../../core/models/projet.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-projet-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './projet-list.component.html'
})
export class ProjetListComponent implements OnInit {
  projets: Projet[] = [];
  filteredProjets: Projet[] = [];
  isLoading = true;
  
  // Filtres
  categories: string[] = ['Toutes', 'Data & IA', 'Cybersécurité', 'Dev & Ingénierie', 'Cloud & DevOps', 'UX/UI', 'Tech Émergente', 'Tech Business'];
  selectedCategorie: string = 'Toutes';
  searchTerm: string = '';

  constructor(private projetService: ProjetService, public authService: AuthService) {}

  ngOnInit() {
    this.projetService.getProjets(0, 50).subscribe({
      next: (res) => { 
        this.projets = res.content; 
        this.filteredProjets = this.projets;
        this.isLoading = false; 
      },
      error: () => { this.isLoading = false; }
    });
  }

  filterData() {
    this.filteredProjets = this.projets.filter(p => {
      const matchCat = this.selectedCategorie === 'Toutes' || p.categorieNom === this.selectedCategorie;
      const matchSearch = p.titre.toLowerCase().includes(this.searchTerm.toLowerCase());
      return matchCat && matchSearch;
    });
  }

  setCategorie(cat: string) {
    this.selectedCategorie = cat;
    this.filterData();
  }
}
