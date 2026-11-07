import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-hackathon-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './hackathon-list.component.html'
})
export class HackathonListComponent implements OnInit {
  hackathons: Hackathon[] = [];
  filteredHackathons: Hackathon[] = [];
  isLoading: boolean = true;
  
  // Filtres
  searchTerm: string = '';
  selectedStatut: string = 'TOUS'; // TOUS, A_VENIR, EN_COURS, TERMINE
  selectedCategorie: string = 'Toutes';
  selectedVille: string = 'Toutes';

  categories: string[] = ['Toutes', 'Data & IA', 'Cybersécurité', 'Dev & Ingénierie', 'Cloud & DevOps', 'UX/UI', 'Tech Émergente', 'Tech Business'];
  villes: string[] = ['Toutes', 'Ouagadougou', 'Bobo-Dioulasso', 'Koudougou', 'En ligne'];

  constructor(private hackathonService: HackathonService) {}

  ngOnInit(): void {
    this.hackathonService.getHackathons(0, 50).subscribe({
      next: (response) => {
        this.hackathons = response.content;
        this.filterData();
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  filterData() {
    this.filteredHackathons = this.hackathons.filter(h => {
      const matchSearch = h.titre.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
                          h.createurNom.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchStatut = this.selectedStatut === 'TOUS' || h.statut === this.selectedStatut;
      const matchCat = this.selectedCategorie === 'Toutes' || h.categorieNom === this.selectedCategorie;
      const matchVille = this.selectedVille === 'Toutes' || h.lieu.includes(this.selectedVille);
      
      return matchSearch && matchStatut && matchCat && matchVille;
    });
  }

  setStatut(statut: string) {
    this.selectedStatut = statut;
    this.filterData();
  }
}
