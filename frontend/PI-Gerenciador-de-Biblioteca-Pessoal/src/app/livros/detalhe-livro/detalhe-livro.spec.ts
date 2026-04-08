import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalheLivro } from './detalhe-livro';

describe('DetalheLivro', () => {
  let component: DetalheLivro;
  let fixture: ComponentFixture<DetalheLivro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalheLivro],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalheLivro);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
