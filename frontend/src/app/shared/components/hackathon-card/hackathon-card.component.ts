import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Hackathon } from '../../../core/models/hackathon.model';

@Component({
  selector: 'app-hackathon-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div [routerLink]="['/hackathons', hackathon.id]" class="cursor-pointer bg-slate-800 rounded-xl overflow-hidden border border-slate-700 hover:border-indigo-500 transition-all group flex flex-col h-full transform hover:-translate-y-1 hover:shadow-xl hover:shadow-indigo-500/10">
      
      <!-- Image de l'affiche (si elle existe) -->
      @if (hackathon.imageUrl) {
        <div class="h-48 w-full overflow-hidden">
          <img [src]="hackathon.imageUrl" alt="Affiche" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300">
        </div>
      }

      <div class="p-6 flex-grow">
        <span class="inline-block px-3 py-1 rounded-full text-xs font-semibold bg-indigo-900/50 text-indigo-300 border border-indigo-700/50 mb-4">
          {{ hackathon.categorieNom }}
        </span>
        <h3 class="text-xl font-bold text-white mb-2 group-hover:text-indigo-400 transition-colors line-clamp-2">
          {{ hackathon.titre }}
        </h3>
        <p class="text-slate-400 text-sm mb-4 line-clamp-3">
          {{ hackathon.description }}
        </p>
      </div>
      <div class="bg-slate-900/50 p-4 border-t border-slate-700 flex justify-between items-center text-sm">
        <span class="text-slate-300 flex items-center gap-1">📍 {{ hackathon.lieu }}</span>
        <span class="text-slate-400">{{ hackathon.dateDebut | date:'dd MMM yyyy' }}</span>
      </div>
    </div>
  `
})
export class HackathonCardComponent {
  @Input({ required: true }) hackathon!: Hackathon;
}
