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
  private subscription: Subscription = new Subscription();

  constructor(
    private livroService: LivroService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        // Agora obterPorId retorna um Observable, então usamos subscribe
        this.livroService.obterPorId(id).subscribe({
          next: (livro) => {
            this.livro = livro;
            if (!this.livro) {
              this.router.navigate(['/livros']);
            }
          },
          error: (error) => {
            console.error('Erro ao carregar livro:', error);
            this.router.navigate(['/livros']);
          }
        });
      }
    });
  }

  ngOnDestroy(): void {
    // Limpa subscriptions para evitar memory leaks
    this.subscription.unsubscribe();
  }

  deletar() {
    if (this.livro && confirm('Tem certeza que deseja remover este livro?')) {
      // removerItem agora retorna um Observable
      this.livroService.removerItem(this.livro.id).subscribe({
        next: () => {
          this.router.navigate(['/livros']);
        },
        error: (error) => {
          console.error('Erro ao remover livro:', error);
          // Opcional: mostrar mensagem de erro para o usuário
        }
      });
    }
  }

  getEstrelas(nota: number | null): number[] {
    if (!nota) return [];
    return Array(nota).fill(0);
  }
}
