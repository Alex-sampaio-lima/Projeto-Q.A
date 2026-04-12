import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioLivroComponent } from './formulario-livro';

describe('FormularioLivro', () => {
  let component: FormularioLivroComponent;
  let fixture: ComponentFixture<FormularioLivroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioLivroComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormularioLivroComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
