import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { LivroService } from '../../../services/livro';
import { Livro } from '../../../models/livro.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-detalhe-livro',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detalhe-livro.html',
  styleUrl: './detalhe-livro.scss'
})
export class DetalheLivroComponent implements OnInit, OnDestroy {
  livro: Livro | undefined;
  loading = true;
  error: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private livroService: LivroService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.carregarLivro(id);
      } else {
        this.router.navigate(['/livros']);
      }
    });
    this.subscriptions.add(routeSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private carregarLivro(id: string): void {
    this.loading = true;
    this.error = null;

    const livroSub = this.livroService.obterPorId(id).subscribe({
      next: (livro) => {
        if (livro) {
          this.livro = livro;
          this.loading = false;
        } else {
          this.loading = false;
          this.router.navigate(['/livros']);
        }
      },
      error: (err) => {
        console.error('Erro ao carregar livro:', err);
        this.error = 'Erro ao carregar os detalhes do livro';
        this.loading = false;
        // Opcional: redirecionar após alguns segundos
        setTimeout(() => this.router.navigate(['/livros']), 3000);
      }
    });
    this.subscriptions.add(livroSub);
  }

  deletar(): void {
    if (this.livro && confirm('Tem certeza que deseja remover este livro?')) {
      this.loading = true;

      const deleteSub = this.livroService.removerItem(this.livro.id).subscribe({
        next: () => {
          this.router.navigate(['/livros']);
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

  getEstrelas(nota: number | null): number[] {
    if (!nota) return [];
    return Array(nota).fill(0);
  }
}
