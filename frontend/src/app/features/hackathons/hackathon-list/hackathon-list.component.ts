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
  allHackathons: Hackathon[] = [];
  featuredHackathon: Hackathon | null = null;
  otherHackathons: Hackathon[] = [];
  isLoading = true;

  // Filtres
  searchTerm: string = '';
  selectedCategorie: string = 'Toutes';
  categories: string[] = ['Toutes', 'Data & IA', 'Cybersécurité', 'Dev & Ingénierie', 'Cloud & DevOps', 'UX/UI', 'Tech Émergente', 'Tech Business'];

  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  constructor(private hackathonService: HackathonService) {}

  ngOnInit(): void {
    this.chargerHackathons();
  }

  chargerHackathons(): void {
    this.isLoading = true;
    this.hackathonService.getHackathons(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.allHackathons = response.content;
        this.totalPages = response.totalPages;
        
        // Séparation intelligente : le premier est mis en vedette, les autres dans la grille
        if (this.allHackathons.length > 0) {
          this.featuredHackathon = this.allHackathons[0];
          this.otherHackathons = this.allHackathons.slice(1);
        } else {
          this.featuredHackathon = null;
          this.otherHackathons = [];
        }
        
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  get filteredOtherHackathons() {
    return this.otherHackathons.filter(h => {
      const matchSearch = h.titre.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
                          (h.createurNom && h.createurNom.toLowerCase().includes(this.searchTerm.toLowerCase()));
      const matchCat = this.selectedCategorie === 'Toutes' || h.categorieNom === this.selectedCategorie;
      return matchSearch && matchCat;
    });
  }

  setCategorie(cat: string) {
    this.selectedCategorie = cat;
  }

  pageSuivante(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.chargerHackathons();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  pagePrecedente(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.chargerHackathons();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }
}
