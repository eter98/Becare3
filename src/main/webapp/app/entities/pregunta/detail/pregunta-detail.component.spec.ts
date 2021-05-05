import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PreguntaDetailComponent } from './pregunta-detail.component';

describe('Component Tests', () => {
  describe('Pregunta Management Detail Component', () => {
    let comp: PreguntaDetailComponent;
    let fixture: ComponentFixture<PreguntaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PreguntaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pregunta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PreguntaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PreguntaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pregunta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pregunta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
