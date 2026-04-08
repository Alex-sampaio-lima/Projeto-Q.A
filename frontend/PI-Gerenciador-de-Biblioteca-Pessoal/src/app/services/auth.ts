import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private usersKey = 'biblioteca_users';
  private currentUserKey = 'biblioteca_currentUser';

  private currentUserSubject: BehaviorSubject<Usuario | null>;
  public currentUser$: Observable<Usuario | null>;

  constructor(private router: Router) {
    const storedUser = localStorage.getItem(this.currentUserKey);
    this.currentUserSubject = new BehaviorSubject<Usuario | null>(storedUser ? JSON.parse(storedUser) : null);
    this.currentUser$ = this.currentUserSubject.asObservable();

    // Iniciar dados vazios se não existir
    if (!localStorage.getItem(this.usersKey)) {
      localStorage.setItem(this.usersKey, JSON.stringify([]));
    }
  }

  public get currentUserValue(): Usuario | null {
    return this.currentUserSubject.value;
  }

  registrar(usuarioData: Partial<Usuario>): boolean {
    const usuarios: Usuario[] = JSON.parse(localStorage.getItem(this.usersKey) || '[]');

    if (usuarios.find(u => u.email === usuarioData.email)) {
      return false; // Email em uso
    }

    const novoUsuario: Usuario = {
      id: crypto.randomUUID ? crypto.randomUUID() : Date.now().toString(),
      nome: usuarioData.nome!,
      email: usuarioData.email!,
      senha: usuarioData.senha!
    };

    usuarios.push(novoUsuario);
    localStorage.setItem(this.usersKey, JSON.stringify(usuarios));
    return true;
  }

  login(email: string, senha: string): boolean {
    const usuarios: Usuario[] = JSON.parse(localStorage.getItem(this.usersKey) || '[]');
    const user = usuarios.find(u => u.email === email && u.senha === senha);

    if (user) {
      // Remove senha antes de salvar a sessão
      const { senha: _, ...userSemSenha } = user;
      localStorage.setItem(this.currentUserKey, JSON.stringify(userSemSenha));
      this.currentUserSubject.next(userSemSenha as Usuario);
      return true;
    }
    return false;
  }

  logout() {
    localStorage.removeItem(this.currentUserKey);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }
}
