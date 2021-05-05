import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotificacionDetailComponent } from './notificacion-detail.component';

describe('Component Tests', () => {
  describe('Notificacion Management Detail Component', () => {
    let comp: NotificacionDetailComponent;
    let fixture: ComponentFixture<NotificacionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NotificacionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ notificacion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NotificacionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificacionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notificacion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notificacion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
