import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CuestionarioEstadoDetailComponent } from './cuestionario-estado-detail.component';

describe('Component Tests', () => {
  describe('CuestionarioEstado Management Detail Component', () => {
    let comp: CuestionarioEstadoDetailComponent;
    let fixture: ComponentFixture<CuestionarioEstadoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CuestionarioEstadoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cuestionarioEstado: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CuestionarioEstadoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CuestionarioEstadoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cuestionarioEstado on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cuestionarioEstado).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
