import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fisiometria1DetailComponent } from './fisiometria-1-detail.component';

describe('Component Tests', () => {
  describe('Fisiometria1 Management Detail Component', () => {
    let comp: Fisiometria1DetailComponent;
    let fixture: ComponentFixture<Fisiometria1DetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [Fisiometria1DetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fisiometria1: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(Fisiometria1DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Fisiometria1DetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fisiometria1 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fisiometria1).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
