import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LivroService } from '../../../services/livro';
import { Livro } from '../../../models/livro.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-lista-livros',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './lista-livros.html',
  styleUrl: './lista-livros.scss'
})
export class ListaLivrosComponent implements OnInit {
  livros$: Observable<Livro[]>;

  constructor(private livroService: LivroService) {
    this.livros$ = this.livroService.livros$;
  }

  ngOnInit(): void {}

  deletar(id: string, event: Event) {
    event.stopPropagation();
    if(confirm('Tem certeza que deseja remover este livro?')) {
        this.livroService.removerItem(id);
    }
  }

  getEstrelas(nota: number | null): number[] {
    if (!nota) return [];
    return Array(nota).fill(0);
  }
}
