import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { LivroService } from '../../services/livro';
import { Livro } from '../../models/livro.model';

@Component({
  selector: 'app-detalhe-livro',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detalhe-livro.html',
  styleUrl: './detalhe-livro.scss'
})
export class DetalheLivroComponent implements OnInit {
  livro: Livro | undefined;

  constructor(
    private livroService: LivroService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.livro = this.livroService.obterPorId(id);
        if (!this.livro) {
          this.router.navigate(['/livros']);
        }
      }
    });
  }

  deletar() {
    if (this.livro && confirm('Tem certeza que deseja remover este livro?')) {
      this.livroService.removerItem(this.livro.id);
      this.router.navigate(['/livros']);
    }
  }

  getEstrelas(nota: number | null): number[] {
    if (!nota) return [];
    return Array(nota).fill(0);
  }
}
