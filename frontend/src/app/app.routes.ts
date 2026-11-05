import { Routes } from '@angular/router';
import { HomePageComponent } from './features/home/home-page/home-page.component';
import { HackathonListComponent } from './features/hackathons/hackathon-list/hackathon-list.component';
import { HackathonDetailComponent } from './features/hackathons/hackathon-detail/hackathon-detail.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { CreateHackathonComponent } from './features/dashboard/create-hackathon/create-hackathon.component';
import { ProjetListComponent } from './features/projets/projet-list/projet-list.component';
import { CreateProjetComponent } from './features/projets/create-projet/create-projet.component';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'hackathons', component: HackathonListComponent },
  { path: 'hackathons/:id', component: HackathonDetailComponent }, // NOUVELLE ROUTE
  { path: 'projets', component: ProjetListComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'publier', component: CreateHackathonComponent, canActivate: [AuthGuard] },
  { path: 'publier-projet', component: CreateProjetComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];
