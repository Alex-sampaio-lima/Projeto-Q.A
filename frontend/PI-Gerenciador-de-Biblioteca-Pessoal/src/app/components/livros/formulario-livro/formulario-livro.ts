import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { LivroService } from '../../../services/livro';
import { Livro } from '../../../models/livro.model';

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
        const livro = this.livroService.obterPorId(id);
        if (livro) {
          this.livroForm.patchValue(livro);
        } else {
          this.router.navigate(['/livros']);
        }
      }
    });

    if (this.livroForm.get('status')?.value === 'Não Lido') {
      this.livroForm.get('nota')?.disable();
    }

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

  onSubmit() {
    if (this.livroForm.invalid) return;

    const formValue = this.livroForm.getRawValue();
    const livro: Livro = {
      ...formValue,
      id: this.isEditMode ? this.editingId! : '',
      usuarioId: ''
    };

    if (this.isEditMode) {
      const original = this.livroService.obterPorId(this.editingId!);
      if (original) {
        livro.usuarioId = original.usuarioId;
      }
      this.livroService.atualizarItem(livro);
    } else {
      this.livroService.adicionarItem(livro);
    }

    this.router.navigate(['/livros']);
  }
}
