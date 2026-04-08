import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Livro } from '../models/livro.model';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class LivroService {
  private livrosKey = 'biblioteca_livros';
  private livrosSubject: BehaviorSubject<Livro[]> = new BehaviorSubject<Livro[]>([]);
  public livros$: Observable<Livro[]> = this.livrosSubject.asObservable();

  constructor(private authService: AuthService) {
    this.carregarLivros();
  }

  private initMockDataSeVazio() {
    if (!localStorage.getItem(this.livrosKey)) {
      const mockLivros: Livro[] = [
        {
          id: '1', titulo: 'O Senhor dos Anéis: A Sociedade do Anel', autor: 'J.R.R. Tolkien', genero: 'Fantasia', ano: 1954, status: 'Lido', nota: 5, resumo: 'A jornada para destruir o Um Anel.', capaUrl: 'https://m.media-amazon.com/images/I/81h8J2uT+hL._AC_UF1000,1000_QL80_.jpg', usuarioId: 'mock'
        },
        {
          id: '2', titulo: 'Duna', autor: 'Frank Herbert', genero: 'Ficção Científica', ano: 1965, status: 'Não Lido', nota: null, resumo: 'Intrigas no planeta deserto de Arrakis.', capaUrl: 'https://m.media-amazon.com/images/I/81zN7udGRUL._AC_UF1000,1000_QL80_.jpg', usuarioId: 'mock'
        },
        {
          id: '3', titulo: '1984', autor: 'George Orwell', genero: 'Ficção Distópica', ano: 1949, status: 'Lido', nota: 4, resumo: 'Um mundo dominado pelo Grande Irmão.', capaUrl: 'https://m.media-amazon.com/images/I/819js3EQwbL._AC_UF1000,1000_QL80_.jpg', usuarioId: 'mock'
        }
      ];
      localStorage.setItem(this.livrosKey, JSON.stringify(mockLivros));
    }
  }

  private carregarLivros() {
    this.initMockDataSeVazio();
    const todosLivros: Livro[] = JSON.parse(localStorage.getItem(this.livrosKey) || '[]');
    const user = this.authService.currentUserValue;

    if (user) {
      // Pega os livros do usuário logado E os de 'mock' que distribuímos como demo, se quisermos.
      // Vamos pegar só os do user logado e mock data para não ficar vazio.
      const livrosUsuario = todosLivros.filter(l => l.usuarioId === user.id || l.usuarioId === 'mock');
      this.livrosSubject.next(livrosUsuario);
    } else {
      this.livrosSubject.next([]);
    }
  }

  obterTodos(): Livro[] {
    return this.livrosSubject.value;
  }

  obterPorId(id: string): Livro | undefined {
    return this.livrosSubject.value.find(l => l.id === id);
  }

  adicionarItem(livro: Livro) {
    const user = this.authService.currentUserValue;
    if (!user) return; // Segurança

    livro.id = crypto.randomUUID ? crypto.randomUUID() : Date.now().toString();
    livro.usuarioId = user.id;

    const todosLivros: Livro[] = JSON.parse(localStorage.getItem(this.livrosKey) || '[]');
    todosLivros.push(livro);
    localStorage.setItem(this.livrosKey, JSON.stringify(todosLivros));
    this.carregarLivros(); // Atualiza a lista
  }

  atualizarItem(livroEditado: Livro) {
    let todosLivros: Livro[] = JSON.parse(localStorage.getItem(this.livrosKey) || '[]');
    const index = todosLivros.findIndex(l => l.id === livroEditado.id);

    if (index !== -1) {
      todosLivros[index] = { ...livroEditado };
      localStorage.setItem(this.livrosKey, JSON.stringify(todosLivros));
      this.carregarLivros(); // Atualiza a lista via RxJS
    }
  }

  removerItem(id: string) {
    let todosLivros: Livro[] = JSON.parse(localStorage.getItem(this.livrosKey) || '[]');
    todosLivros = todosLivros.filter(l => l.id !== id);
    localStorage.setItem(this.livrosKey, JSON.stringify(todosLivros));
    this.carregarLivros();
  }
}
