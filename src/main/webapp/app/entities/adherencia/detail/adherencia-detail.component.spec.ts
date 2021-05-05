import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdherenciaDetailComponent } from './adherencia-detail.component';

describe('Component Tests', () => {
  describe('Adherencia Management Detail Component', () => {
    let comp: AdherenciaDetailComponent;
    let fixture: ComponentFixture<AdherenciaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AdherenciaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ adherencia: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AdherenciaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdherenciaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adherencia on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adherencia).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
