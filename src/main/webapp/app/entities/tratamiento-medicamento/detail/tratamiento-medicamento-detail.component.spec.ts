import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TratamientoMedicamentoDetailComponent } from './tratamiento-medicamento-detail.component';

describe('Component Tests', () => {
  describe('TratamientoMedicamento Management Detail Component', () => {
    let comp: TratamientoMedicamentoDetailComponent;
    let fixture: ComponentFixture<TratamientoMedicamentoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TratamientoMedicamentoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tratamientoMedicamento: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TratamientoMedicamentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TratamientoMedicamentoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tratamientoMedicamento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tratamientoMedicamento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
