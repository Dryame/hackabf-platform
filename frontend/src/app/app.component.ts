import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
// On corrige le chemin d'importation ici
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { FooterComponent } from './shared/components/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  // On déclare explicitement les composants utilisés
  imports: [RouterOutlet, NavbarComponent, FooterComponent], 
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'hackabf-frontend';
}
