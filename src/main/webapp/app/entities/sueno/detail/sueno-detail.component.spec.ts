import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuenoDetailComponent } from './sueno-detail.component';

describe('Component Tests', () => {
  describe('Sueno Management Detail Component', () => {
    let comp: SuenoDetailComponent;
    let fixture: ComponentFixture<SuenoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SuenoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sueno: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SuenoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SuenoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sueno on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sueno).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
