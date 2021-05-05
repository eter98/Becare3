import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CaloriaDetailComponent } from './caloria-detail.component';

describe('Component Tests', () => {
  describe('Caloria Management Detail Component', () => {
    let comp: CaloriaDetailComponent;
    let fixture: ComponentFixture<CaloriaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CaloriaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ caloria: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CaloriaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CaloriaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load caloria on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.caloria).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
