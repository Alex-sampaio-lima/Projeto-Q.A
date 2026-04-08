export interface Livro {
  id: string;
  titulo: string;
  autor: string;
  genero: string;
  ano: number | null;
  capaUrl?: string; // URL to mock cover
  status: 'Lido' | 'Não Lido';
  nota: number | null; // 1 to 5
  resumo?: string;
  usuarioId: string; // The owner of the book
}
