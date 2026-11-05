import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HackathonService } from '../../../core/services/hackathon.service';
import { Hackathon } from '../../../core/models/hackathon.model';
import { HackathonCardComponent } from '../../../shared/components/hackathon-card/hackathon-card.component';

@Component({
  selector: 'app-hackathon-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HackathonCardComponent],
  templateUrl: './hackathon-list.component.html'
})
export class HackathonListComponent implements OnInit {
  hackathons: Hackathon[] = [];
  filteredHackathons: Hackathon[] = [];
  isLoading: boolean = true;
  
  categories: string[] = ['Toutes', 'Data & IA', 'Cybersécurité', 'Dev & Ingénierie', 'Cloud & DevOps', 'UX/UI', 'Tech Émergente', 'Tech Business'];
  selectedCategorie: string = 'Toutes';
  searchTerm: string = '';

  constructor(private hackathonService: HackathonService) {}

  ngOnInit(): void {
    this.hackathonService.getHackathons(0, 50).subscribe({
      next: (response) => {
        this.hackathons = response.content;
        this.filteredHackathons = this.hackathons;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  filterData() {
    this.filteredHackathons = this.hackathons.filter(h => {
      const matchCat = this.selectedCategorie === 'Toutes' || h.categorieNom === this.selectedCategorie;
      const matchSearch = h.titre.toLowerCase().includes(this.searchTerm.toLowerCase());
      return matchCat && matchSearch;
    });
  }

  setCategorie(cat: string) {
    this.selectedCategorie = cat;
    this.filterData();
  }
}
