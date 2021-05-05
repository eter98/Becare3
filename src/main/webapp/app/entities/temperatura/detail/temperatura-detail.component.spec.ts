import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TemperaturaDetailComponent } from './temperatura-detail.component';

describe('Component Tests', () => {
  describe('Temperatura Management Detail Component', () => {
    let comp: TemperaturaDetailComponent;
    let fixture: ComponentFixture<TemperaturaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TemperaturaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ temperatura: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TemperaturaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TemperaturaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load temperatura on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.temperatura).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
