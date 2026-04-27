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

  // Método alternativo: login com POST (se preferir)
  loginWithPost(email: string, senha: string): Observable<any> {
    const body = { email, senha };
    return this.http.post(`${this.apiUrl}/auth/login`, body).pipe(
      tap((response: any) => {
        if (response.token) {
          localStorage.setItem('authToken', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response.usuario));
          this.currentUserSubject.next(response.usuario);
        }
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
