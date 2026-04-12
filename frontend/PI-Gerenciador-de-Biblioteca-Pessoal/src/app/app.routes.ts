import { Routes } from '@angular/router';
// import { LoginComponent } from './autenticacao/login/login';
import { LoginComponent } from './components/autenticacao/login/login';
import { RegistroComponent } from './components/autenticacao/registro/registro';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  {
    path: 'livros',
    loadChildren: () => import('./components/livros/livros-module').then(m => m.LivrosModule),
    canActivate: [AuthGuard]
  },
  { path: '', redirectTo: '/livros', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
