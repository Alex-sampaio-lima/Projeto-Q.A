import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/compartilhado/navbar/navbar';
import { Rodape } from './components/compartilhado/rodape/rodape';

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
