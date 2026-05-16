import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { LivroService } from '../../../services/livro';
import { Livro } from '../../../models/livro.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-formulario-livro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './formulario-livro.html',
  styleUrl: './formulario-livro.scss'
})
export class FormularioLivroComponent implements OnInit, OnDestroy {
  livroForm: FormGroup;
  isEditMode = false;
  editingId: string | null = null;
  livroOriginal: Livro | null = null; // Para guardar os dados originais na edição
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private livroService: LivroService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.livroForm = this.fb.group({
      titulo: ['', Validators.required],
      autor: ['', Validators.required],
      genero: ['', Validators.required],
      ano: [null],
      capaUrl: [''],
      status: ['Não Lido', Validators.required],
      nota: [null, [Validators.min(1), Validators.max(5)]],
      resumo: ['']
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.editingId = id;

        // Agora obterPorId retorna um Observable, então precisamos nos inscrever
        this.livroService.obterPorId(id).subscribe({
          next: (livro) => {
            if (livro) {
              this.livroOriginal = livro; // Guarda o livro original
              this.livroForm.patchValue({
                titulo: livro.titulo,
                autor: livro.autor,
                genero: livro.genero,
                ano: livro.ano,
                capaUrl: livro.capaUrl,
                status: livro.status,
                nota: livro.nota,
                resumo: livro.resumo
              });
            } else {
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

    // Inicializa o estado da nota baseado no status atual
    if (this.livroForm.get('status')?.value === 'Não Lido') {
      this.livroForm.get('nota')?.disable();
    }

    // Validações dinâmicas: Se está lido, pode ter nota
    this.livroForm.get('status')?.valueChanges.subscribe(status => {
      const notaControl = this.livroForm.get('nota');
      if (status === 'Não Lido') {
        notaControl?.setValue(null);
        notaControl?.disable();
      } else {
        notaControl?.enable();
      }
    });
  }

  ngOnDestroy(): void {
    // Limpa as subscriptions para evitar memory leaks
    this.subscriptions.unsubscribe();
  }

  onSubmit() {
    if (this.livroForm.invalid) return;

    const formValue = this.livroForm.getRawValue();

    if (this.isEditMode && this.editingId) {
      // Modo edição: mantém o id e outros campos que não estão no formulário
      const livro: Livro = {
        ...this.livroOriginal, // Preserva dados originais (incluindo usuarioId)
        ...formValue,          // Sobrescreve com os novos dados do formulário
        id: this.editingId     // Garante que o ID está correto
      };

      this.livroService.atualizarItem(livro).subscribe({
        next: () => {
          this.router.navigate(['/livros']);
        },
        error: (error) => {
          console.error('Erro ao atualizar livro:', error);
          // Aqui você pode adicionar uma mensagem de erro para o usuário
        }
      });
    } else {
      // Modo criação: não precisa de id nem usuarioId (backend gerencia)
      const livro: Livro = {
        ...formValue,
        id: '',           // Será gerado pelo backend
        usuarioId: ''     // Será atribuído pelo backend
      };

      this.livroService.adicionarItem(livro).subscribe({
        next: () => {
          this.router.navigate(['/livros']);
        },
        error: (error) => {
          console.error('Erro ao adicionar livro:', error);
          // Aqui você pode adicionar uma mensagem de erro para o usuário
        }
      });
    }
  }
}
