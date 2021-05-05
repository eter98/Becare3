import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlarmaDetailComponent } from './alarma-detail.component';

describe('Component Tests', () => {
  describe('Alarma Management Detail Component', () => {
    let comp: AlarmaDetailComponent;
    let fixture: ComponentFixture<AlarmaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AlarmaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ alarma: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AlarmaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlarmaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load alarma on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.alarma).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
