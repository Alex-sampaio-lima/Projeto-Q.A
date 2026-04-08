import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioLivro } from './formulario-livro';

describe('FormularioLivro', () => {
  let component: FormularioLivro;
  let fixture: ComponentFixture<FormularioLivro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioLivro],
    }).compileComponents();

    fixture = TestBed.createComponent(FormularioLivro);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
