import { Routes } from '@angular/router';
import { HomePageComponent } from './features/home/home-page/home-page.component';
import { HackathonListComponent } from './features/hackathons/hackathon-list/hackathon-list.component';
import { HackathonDetailComponent } from './features/hackathons/hackathon-detail/hackathon-detail.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { CreateHackathonComponent } from './features/dashboard/create-hackathon/create-hackathon.component';
import { MonEspaceComponent } from './features/dashboard/mon-espace/mon-espace.component';
import { ManageHackathonComponent } from './features/dashboard/manage-hackathon/manage-hackathon.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard/admin-dashboard.component';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'hackathons', component: HackathonListComponent },
  { path: 'hackathons/:id', component: HackathonDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'publier', component: CreateHackathonComponent, canActivate: [AuthGuard] },
  { path: 'mon-espace', component: MonEspaceComponent, canActivate: [AuthGuard] },
  { path: 'gerer/:id', component: ManageHackathonComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];
