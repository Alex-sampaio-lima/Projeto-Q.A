import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { LivrosRoutingModule } from './livros-routing-module';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LivrosRoutingModule
  ]
})
export class LivrosModule { }