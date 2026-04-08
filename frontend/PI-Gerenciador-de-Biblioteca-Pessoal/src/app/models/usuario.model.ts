export interface Usuario {
  id: string;
  nome: string;
  email: string;
  senha?: string; // used internally for auth, could be omitted in active user state
}
