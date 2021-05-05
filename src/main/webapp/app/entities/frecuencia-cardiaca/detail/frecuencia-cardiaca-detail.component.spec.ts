import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FrecuenciaCardiacaDetailComponent } from './frecuencia-cardiaca-detail.component';

describe('Component Tests', () => {
  describe('FrecuenciaCardiaca Management Detail Component', () => {
    let comp: FrecuenciaCardiacaDetailComponent;
    let fixture: ComponentFixture<FrecuenciaCardiacaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FrecuenciaCardiacaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ frecuenciaCardiaca: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FrecuenciaCardiacaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FrecuenciaCardiacaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load frecuenciaCardiaca on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.frecuenciaCardiaca).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
