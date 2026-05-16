import { Component, OnInit } from '@angular/core';
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
export class FormularioLivroComponent implements OnInit {
  livroForm: FormGroup;
  isEditMode = false;
  editingId: string | null = null;
  private subscription: Subscription = new Subscription();
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
    this.subscription.add(routeSub);

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
      this.subscription.add(statusSub);
    }

    // Inicializa o estado da nota baseado no status atual
    const statusAtual = this.livroForm.get('status')?.value;
    if (statusAtual === 'Não Lido') {
      this.livroForm.get('nota')?.disable();
    }
  }

  ngOnDestroy(): void {
    // Limpa as subscriptions para evitar memory leaks
    this.subscription.unsubscribe();
  }

  private carregarLivroParaEdicao(id: string): void {
    this.loading = true;
    const livroSub = this.livroService.obterPorId(id).subscribe({
      next: (livro) => {
        if (livro) {
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
    this.subscription.add(livroSub);
  }

  onSubmit(): void {
    if (this.livroForm.invalid || this.loading) return;

    const formValue = this.livroForm.getRawValue();
    this.loading = true;
    this.error = null;

    if (this.isEditMode && this.editingId) {
      // Modo de edição - apenas envia os dados do formulário
      const livro: Partial<Livro> = {
        id: this.editingId,
        ...formValue
      };

      const updateSub = this.livroService.atualizarItem(livro as Livro).subscribe({
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
      this.subscription.add(updateSub);
    } else {
      // Modo de criação - o backend vai gerar o ID e associar ao usuário
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
      this.subscription.add(createSub);
    }
  }
}
