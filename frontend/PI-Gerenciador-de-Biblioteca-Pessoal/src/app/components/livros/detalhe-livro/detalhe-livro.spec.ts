import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

import { DetalheLivroComponent } from './detalhe-livro.component';

@Component({ standalone: true, template: '' })
class DummyComponent {}

describe('DetalheLivroComponent', () => {
  let component: DetalheLivroComponent;
  let fixture: ComponentFixture<DetalheLivroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalheLivroComponent],
      providers: [
        provideRouter([
          { path: 'livros', component: DummyComponent }
        ]),
        provideHttpClient()
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalheLivroComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
