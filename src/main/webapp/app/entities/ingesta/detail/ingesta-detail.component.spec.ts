import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IngestaDetailComponent } from './ingesta-detail.component';

describe('Component Tests', () => {
  describe('Ingesta Management Detail Component', () => {
    let comp: IngestaDetailComponent;
    let fixture: ComponentFixture<IngestaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IngestaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ingesta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IngestaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IngestaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ingesta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ingesta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
