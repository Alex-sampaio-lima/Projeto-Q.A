// login.ts (atualizado)
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  error: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    if (this.authService.currentUserValue) {
      this.router.navigate(['/livros']);
    }
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const { email, senha } = this.loginForm.value;

    // Agora usa Observable
    this.authService.login(email, senha).subscribe({
      next: (usuario) => {
        console.log('Login bem-sucedido:', usuario);
        this.router.navigate(['/livros']);
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro no login:', err);
        this.error = 'E-mail ou senha inválidos. Tente novamente.';
        this.loading = false;
      }
    });
  }
}
