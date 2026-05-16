import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Livro } from '../models/livro.model';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class LivroService {
  private baseUrl = 'http://localhost:8080/livros';
  private livrosSubject: BehaviorSubject<Livro[]> = new BehaviorSubject<Livro[]>([]);
  public livros$: Observable<Livro[]> = this.livrosSubject.asObservable();

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {
    this.carregarLivros();
  }

  private getHeaders(): HttpHeaders {
    const user = this.authService.currentUserValue;

    if (!user || !user.nome || !user.senha) {
      console.error('Usuário não autenticado ou credenciais incompletas');
      return new HttpHeaders({
        'Content-Type': 'application/json'
      });
    }

    // Garantindo que as credenciais estão corretas
    const credentials = btoa(`${user.nome}:${user.senha}`);
    console.log('Credenciais codificadas:', credentials); // Para debug

    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Basic ${credentials}`
    });
  }

  private carregarLivros() {
    const user = this.authService.currentUserValue;

    if (user && user.nome && user.senha) {
      console.log('Carregando livros com usuário:', user.nome); // Debug

      this.http.get<Livro[]>(`${this.baseUrl}/meusLivros`, { headers: this.getHeaders() }).subscribe({
        next: (livros) => {
          console.log('Livros carregados:', livros); // Debug
          this.livrosSubject.next(livros);
        },
        error: (error) => {
          console.error('Erro ao carregar livros do usuário:', error);
          if (error.status === 401) {
            console.error('Autenticação falhou. Verifique usuário e senha.');
            // Opcional: fazer logout
            // this.authService.logout();
          }
          this.livrosSubject.next([]);
        }
      });
    } else {
      console.log(user?.email);
      console.log(user?.senha);

      console.log('Nenhum usuário logado');
      this.livrosSubject.next([]);
    }
  }

  obterTodos(): Observable<Livro[]> {
    return this.livros$;
  }

  obterPorId(id: string): Observable<Livro | undefined> {
    console.log('Buscando livro por ID:', id); // Debug
    return this.http.get<Livro>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() }).pipe(
      catchError(error => {
        console.error('Erro ao obter livro por ID:', error);
        throw error;
      })
    );
  }

  adicionarItem(livro: Livro): Observable<Livro> {
    const user = this.authService.currentUserValue;
    if (!user || !user.nome || !user.senha) {
      throw new Error('Usuário não autenticado');
    }

    const livroParaEnviar = { ...livro, usuarioId: user.id || user.nome };

    console.log('Adicionando livro:', livroParaEnviar); // Debug

    return this.http.post<Livro>(this.baseUrl, livroParaEnviar, { headers: this.getHeaders() }).pipe(
      tap((livroCriado) => {
        console.log('Livro criado com sucesso:', livroCriado); // Debug
        const livrosAtuais = this.livrosSubject.value;
        this.livrosSubject.next([...livrosAtuais, livroCriado]);
      }),
      catchError(error => {
        console.error('Erro ao adicionar livro:', error);
        throw error;
      })
    );
  }

  atualizarItem(livroEditado: Livro): Observable<Livro> {
    console.log('Atualizando livro:', livroEditado); // Debug

    return this.http.patch<Livro>(`${this.baseUrl}/${livroEditado.id}`, livroEditado, { headers: this.getHeaders() }).pipe(
      tap((livroAtualizado) => {
        console.log('Livro atualizado:', livroAtualizado); // Debug
        const livrosAtuais = this.livrosSubject.value;
        const index = livrosAtuais.findIndex(l => l.id === livroAtualizado.id);

        if (index !== -1) {
          const livrosAtualizados = [...livrosAtuais];
          livrosAtualizados[index] = livroAtualizado;
          this.livrosSubject.next(livrosAtualizados);
        }
      }),
      catchError(error => {
        console.error('Erro ao atualizar livro:', error);
        throw error;
      })
    );
  }

  removerItem(id: string): Observable<void> {
    console.log('Removendo livro:', id); // Debug

    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() }).pipe(
      tap(() => {
        console.log('Livro removido com sucesso'); // Debug
        const livrosAtuais = this.livrosSubject.value;
        const livrosFiltrados = livrosAtuais.filter(l => l.id !== id);
        this.livrosSubject.next(livrosFiltrados);
      }),
      catchError(error => {
        console.error('Erro ao remover livro:', error);
        throw error;
      })
    );
  }

  atualizarLista(): void {
    this.carregarLivros();
  }
}
