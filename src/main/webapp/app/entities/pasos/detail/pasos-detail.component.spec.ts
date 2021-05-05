import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PasosDetailComponent } from './pasos-detail.component';

describe('Component Tests', () => {
  describe('Pasos Management Detail Component', () => {
    let comp: PasosDetailComponent;
    let fixture: ComponentFixture<PasosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PasosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pasos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PasosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PasosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pasos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pasos).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
