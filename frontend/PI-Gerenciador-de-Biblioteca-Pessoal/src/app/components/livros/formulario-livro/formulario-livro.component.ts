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
  livroOriginal: Livro | null = null; // ← Do 2º arquivo
  private subscriptions: Subscription = new Subscription();
  loading = false;
  error: string | null = null;

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
    // Carrega dados do livro se estiver em modo de edição
    const routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.editingId = id;
        this.carregarLivroParaEdicao(id);
      }
    });
    this.subscriptions.add(routeSub);

    // Validações dinâmicas: Se está lido, pode ter nota
    const statusSub = this.livroForm.get('status')?.valueChanges.subscribe(status => {
      const notaControl = this.livroForm.get('nota');
      if (status === 'Não Lido') {
        notaControl?.setValue(null);
        notaControl?.disable();
      } else {
        notaControl?.enable();
      }
    });

    if (statusSub) {
      this.subscriptions.add(statusSub);
    }

    // Inicializa o estado da nota baseado no status atual
    const statusAtual = this.livroForm.get('status')?.value;
    if (statusAtual === 'Não Lido') {
      this.livroForm.get('nota')?.disable();
    }
  }

  ngOnDestroy(): void {
    // Limpa as subscriptions para evitar memory leaks
    this.subscriptions.unsubscribe();
  }

  private carregarLivroParaEdicao(id: string): void {
    this.loading = true;
    const livroSub = this.livroService.obterPorId(id).subscribe({
      next: (livro) => {
        if (livro) {
          console.log("Livro recebido do service", livro.titulo);

          this.livroOriginal = livro; // ← Guarda o livro original (do 2º arquivo)
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
          this.loading = false;
        } else {
          this.error = 'Livro não encontrado';
          this.loading = false;
          setTimeout(() => this.router.navigate(['/livros']), 2000);
        }
      },
      error: (err) => {
        console.error('Erro ao carregar livro:', err);
        this.error = 'Erro ao carregar os dados do livro';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/livros']), 2000);
      }
    });
    this.subscriptions.add(livroSub);
  }

  onSubmit(): void {
    if (this.livroForm.invalid || this.loading) return;

    const formValue = this.livroForm.getRawValue();
    this.loading = true;
    this.error = null;

    if (this.isEditMode && this.editingId) {
      // Modo edição: preserva dados originais (do 2º arquivo)
      const livro: Livro = {
        ...this.livroOriginal, // ← Preserva dados originais (incluindo usuarioId)
        ...formValue,          // Sobrescreve com os novos dados do formulário
        id: this.editingId     // Garante que o ID está correto
      };

      const updateSub = this.livroService.atualizarItem(livro).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/livros']);
        },
        error: (err) => {
          console.error('Erro ao atualizar livro:', err);
          this.error = 'Erro ao atualizar o livro. Tente novamente.';
          this.loading = false;
        }
      });
      this.subscriptions.add(updateSub);
    } else {
      // Modo criação
      const livro: Partial<Livro> = {
        ...formValue
      };

      const createSub = this.livroService.adicionarItem(livro as Livro).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/livros']);
        },
        error: (err) => {
          console.error('Erro ao adicionar livro:', err);
          this.error = 'Erro ao adicionar o livro. Tente novamente.';
          this.loading = false;
        }
      });
      this.subscriptions.add(createSub);
    }
  }
}
