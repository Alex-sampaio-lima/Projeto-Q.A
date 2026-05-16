import { AuthService } from './../../../services/auth';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../../models/usuario.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit {
  currentUser: Observable<Usuario | null>;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser = this.authService.currentUser;
  }


  ngOnInit(): void { }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  saudacao(user: Usuario | null): string {
    if (user && user.email) {
      console.log(user.email);

      const primeiroNome = user.email.split('@')[0];
      return `Olá, ${primeiroNome}`;
    }
    return 'Olá, Visitante';
  }
}
