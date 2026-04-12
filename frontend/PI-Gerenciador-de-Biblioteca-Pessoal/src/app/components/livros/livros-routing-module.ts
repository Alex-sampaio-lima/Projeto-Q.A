import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListaLivrosComponent } from './lista-livros/lista-livros.component';
import { FormularioLivroComponent } from './formulario-livro/formulario-livro.component';
import { DetalheLivroComponent } from './detalhe-livro/detalhe-livro.component';

const routes: Routes = [
  { path: '', component: ListaLivrosComponent },
  { path: 'novo', component: FormularioLivroComponent },
  { path: 'editar/:id', component: FormularioLivroComponent },
  { path: 'detalhe/:id', component: DetalheLivroComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LivrosRoutingModule { }
