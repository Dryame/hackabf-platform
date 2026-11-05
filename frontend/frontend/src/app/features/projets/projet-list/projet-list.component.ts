import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjetService } from '../../../core/services/projet.service';
import { Projet } from '../../../core/models/projet.model';

@Component({
  selector: 'app-projet-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="max-w-7xl mx-auto py-8 px-4">
      <div class="mb-10 text-center">
        <h1 class="text-4xl font-bold text-white mb-4">Catalogue des Projets</h1>
        <p class="text-slate-400">Découvrez les PFA, PFE et projets de perfectionnement.</p>
      </div>

      <div *ngIf="isLoading" class="text-center py-20 text-slate-400">Chargement...</div>

      <div *ngIf="!isLoading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for (projet of projets; track projet.id) {
          <div class="bg-slate-800 rounded-xl p-6 border border-slate-700 hover:border-indigo-500 transition-all flex flex-col h-full">
            <div class="flex justify-between items-start mb-4">
              <span class="px-3 py-1 rounded-full text-xs font-semibold bg-cyan-900/50 text-cyan-300 border border-cyan-700/50">
                {{ projet.typeProjet }}
              </span>
              <span class="text-xs text-slate-400">{{ projet.anneeAcademique }}</span>
            </div>
            <h3 class="text-xl font-bold text-white mb-2">{{ projet.titre }}</h3>
            <p class="text-slate-400 text-sm mb-4 line-clamp-3 flex-grow">{{ projet.description }}</p>
            <div class="mt-4 pt-4 border-t border-slate-700 flex justify-between items-center">
              <span class="text-sm font-medium text-indigo-400">{{ projet.categorieNom }}</span>
              <span class="text-xs text-slate-500">Par {{ projet.createurNom }}</span>
            </div>
          </div>
        } @empty {
          <div class="col-span-full text-center py-16 bg-slate-800/50 rounded-xl border border-slate-700 border-dashed">
            <p class="text-slate-400">Aucun projet n'est disponible pour le moment.</p>
          </div>
        }
      </div>
    </div>
  `
})
export class ProjetListComponent implements OnInit {
  projets: Projet[] = [];
  isLoading = true;

  constructor(private projetService: ProjetService) {}

  ngOnInit() {
    this.projetService.getProjets().subscribe({
      next: (res) => { this.projets = res.content; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }
}
