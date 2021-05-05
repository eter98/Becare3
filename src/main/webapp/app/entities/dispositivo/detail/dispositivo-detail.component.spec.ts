import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DispositivoDetailComponent } from './dispositivo-detail.component';

describe('Component Tests', () => {
  describe('Dispositivo Management Detail Component', () => {
    let comp: DispositivoDetailComponent;
    let fixture: ComponentFixture<DispositivoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DispositivoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dispositivo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DispositivoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DispositivoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dispositivo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dispositivo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
