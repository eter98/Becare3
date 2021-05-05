import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PesoDetailComponent } from './peso-detail.component';

describe('Component Tests', () => {
  describe('Peso Management Detail Component', () => {
    let comp: PesoDetailComponent;
    let fixture: ComponentFixture<PesoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PesoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ peso: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PesoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PesoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load peso on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.peso).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
