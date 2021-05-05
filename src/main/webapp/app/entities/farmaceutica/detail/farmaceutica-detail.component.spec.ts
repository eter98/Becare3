import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FarmaceuticaDetailComponent } from './farmaceutica-detail.component';

describe('Component Tests', () => {
  describe('Farmaceutica Management Detail Component', () => {
    let comp: FarmaceuticaDetailComponent;
    let fixture: ComponentFixture<FarmaceuticaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FarmaceuticaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ farmaceutica: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FarmaceuticaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FarmaceuticaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load farmaceutica on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.farmaceutica).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
