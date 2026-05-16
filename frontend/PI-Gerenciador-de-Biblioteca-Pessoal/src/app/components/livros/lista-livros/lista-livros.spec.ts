import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { ListaLivrosComponent } from './lista-livros.component';

describe('ListaLivros', () => {
  let component: ListaLivrosComponent;
  let fixture: ComponentFixture<ListaLivrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaLivrosComponent],
      providers: [provideRouter([]), provideHttpClient()],
    }).compileComponents();

    fixture = TestBed.createComponent(ListaLivrosComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
