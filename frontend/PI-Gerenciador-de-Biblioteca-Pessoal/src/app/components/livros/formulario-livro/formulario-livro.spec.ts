import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { FormularioLivroComponent } from './formulario-livro.component';

describe('FormularioLivro', () => {
  let component: FormularioLivroComponent;
  let fixture: ComponentFixture<FormularioLivroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioLivroComponent],
      providers: [provideRouter([]), provideHttpClient()],
    }).compileComponents();

    fixture = TestBed.createComponent(FormularioLivroComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
