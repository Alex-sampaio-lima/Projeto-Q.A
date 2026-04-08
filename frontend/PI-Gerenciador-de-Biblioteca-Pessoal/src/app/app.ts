import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './compartilhado/navbar/navbar';
import { Rodape } from './compartilhado/rodape/rodape';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, Rodape],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  title = 'Biblioteca Pessoal';
}
