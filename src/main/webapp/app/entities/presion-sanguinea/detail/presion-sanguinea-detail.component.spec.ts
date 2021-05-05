import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PresionSanguineaDetailComponent } from './presion-sanguinea-detail.component';

describe('Component Tests', () => {
  describe('PresionSanguinea Management Detail Component', () => {
    let comp: PresionSanguineaDetailComponent;
    let fixture: ComponentFixture<PresionSanguineaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PresionSanguineaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ presionSanguinea: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PresionSanguineaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PresionSanguineaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load presionSanguinea on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.presionSanguinea).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
