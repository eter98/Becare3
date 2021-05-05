import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProgramaDetailComponent } from './programa-detail.component';

describe('Component Tests', () => {
  describe('Programa Management Detail Component', () => {
    let comp: ProgramaDetailComponent;
    let fixture: ComponentFixture<ProgramaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProgramaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ programa: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProgramaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProgramaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load programa on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.programa).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
