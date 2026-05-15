import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalheLivroComponent } from './detalhe-livro';

describe('DetalheLivroComponent', () => {
  let component: DetalheLivroComponent;
  let fixture: ComponentFixture<DetalheLivroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalheLivroComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalheLivroComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
