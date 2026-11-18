import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.removeToken();
        router.navigate(['/login']);
      } else if (error.status === 403) {
        document.dispatchEvent(new CustomEvent('app-toast', {
          detail: { message: 'Accès refusé.', tone: 'error' }
        }));
      } else if (error.status >= 500) {
        document.dispatchEvent(new CustomEvent('app-toast', {
          detail: { message: 'Erreur interne du serveur.', tone: 'error' }
        }));
      }
      return throwError(() => error);
    })
  );
};
