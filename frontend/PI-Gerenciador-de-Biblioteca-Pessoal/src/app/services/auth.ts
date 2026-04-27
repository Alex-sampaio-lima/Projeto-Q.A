import { Usuario } from './../models/usuario.model';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  // private usuario: Usuario;
  private apiUrl = 'http://localhost:8080';
  private currentUserSubject: BehaviorSubject<Usuario | null>;
  public currentUser: Observable<Usuario | null>;

  private usersKey = 'biblioteca_users';
  private currentUserKey = 'biblioteca_currentUser';

  constructor(private http: HttpClient) {
    // Tenta recuperar o usuário do localStorage
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<Usuario | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): Usuario | null {
    return this.currentUserSubject.value;
  }


  login(email: string, senha: string): Observable<Usuario> {
    // Cria o header de autenticação Basic
    const authHeader = 'Basic ' + btoa(`${email}:${senha}`);
    const headers = new HttpHeaders({ 'Authorization': authHeader });

    // Faz uma requisição GET para um endpoint protegido
    return this.http.get<Usuario>(`${this.apiUrl}/usuarios`, { headers }).pipe(
      map(response => {
        // Cria o objeto usuário
        const usuario: Usuario = {
          id: response.id, // Ajuste conforme seu backend
          email: response.email,
          nome: response.nome // Ou busque o nome em outro endpoint
        };

        // Salva no localStorage
        localStorage.setItem('currentUser', JSON.stringify(usuario));
        localStorage.setItem('authToken', btoa(`${email}:${senha}`)); // Salva credenciais
        this.currentUserSubject.next(usuario);

        return usuario;
      })
    );
  }


  // Verifica se o usuário está autenticado
  isAuthenticated(): boolean {
    return this.currentUserValue !== null;
  }

  // Faz logout
  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
    this.currentUserSubject.next(null);
  }

  // Retorna o token para ser usado nos headers
  getAuthHeader(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Authorization': `Basic ${token}`
    });
  }
};
