import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LivroService } from '../../../services/livro';
import { Livro } from '../../../models/livro.model';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-lista-livros',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './lista-livros.html',
  styleUrl: './lista-livros.scss'
})
export class ListaLivrosComponent implements OnInit, OnDestroy {
  livros$: Observable<Livro[]>;
  loading = false;
  error: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(private livroService: LivroService) {
    this.livros$ = this.livroService.livros$;
  }

  ngOnInit(): void {
    // Força o carregamento da lista se necessário
    this.carregarLivros();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  carregarLivros(): void {
    this.loading = true;
    this.error = null;
    // O carregamento já é feito pelo serviço, apenas controlamos o estado
    const sub = this.livros$.subscribe({
      next: () => {
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar livros:', err);
        this.error = 'Erro ao carregar a lista de livros';
        this.loading = false;
      }
    });
    this.subscriptions.add(sub);
  }

  deletar(id: string, event: Event): void {
    event.stopPropagation();

    if (!id) {
      console.error('ID do livro não fornecido');
      return;
    }

    if (confirm('Tem certeza que deseja remover este livro?')) {
      this.loading = true;

      const deleteSub = this.livroService.removerItem(id).subscribe({
        next: () => {
          console.log('Livro removido com sucesso');
          this.loading = false;
          // A lista é atualizada automaticamente pelo BehaviorSubject no serviço
        },
        error: (err) => {
          console.error('Erro ao deletar livro:', err);
          this.error = 'Erro ao remover o livro. Tente novamente.';
          this.loading = false;
        }
      });
      this.subscriptions.add(deleteSub);
    }
  }

  // Gera array para as estrelas
  getEstrelas(nota: number | null): number[] {
    if (!nota) return [];
    return Array(nota).fill(0);
  }

  // Método para tentar novamente carregar os livros
  tentarNovamente(): void {
    this.error = null;
    this.livroService.atualizarLista();
  }
}
