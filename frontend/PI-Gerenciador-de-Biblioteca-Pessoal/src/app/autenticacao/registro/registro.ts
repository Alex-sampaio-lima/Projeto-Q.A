import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './registro.html',
  styleUrl: './registro.scss',
})
export class RegistroComponent implements OnInit {
  registroForm: FormGroup;
  error: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registroForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    if (this.authService.currentUserValue) {
      this.router.navigate(['/livros']);
    }
  }

  passwordMatchValidator(g: AbstractControl): ValidationErrors | null {
    const senha = g.get('senha')?.value;
    const confirmarSenha = g.get('confirmarSenha')?.value;
    return senha === confirmarSenha ? null : { mismatch: true };
  }

  onSubmit() {
    if (this.registroForm.invalid) {
      return;
    }
    
    const { nome, email, senha } = this.registroForm.value;
    const success = this.authService.registrar({ nome, email, senha });
    
    if (success) {
      // Auto-login after registration
      this.authService.login(email, senha);
      this.router.navigate(['/livros']);
    } else {
      this.error = 'O e-mail informado já está em uso.';
    }
  }
}
