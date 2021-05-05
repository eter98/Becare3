import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OximetriaDetailComponent } from './oximetria-detail.component';

describe('Component Tests', () => {
  describe('Oximetria Management Detail Component', () => {
    let comp: OximetriaDetailComponent;
    let fixture: ComponentFixture<OximetriaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OximetriaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ oximetria: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OximetriaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OximetriaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load oximetria on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.oximetria).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
