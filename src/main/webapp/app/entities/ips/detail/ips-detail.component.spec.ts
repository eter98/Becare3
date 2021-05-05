import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IPSDetailComponent } from './ips-detail.component';

describe('Component Tests', () => {
  describe('IPS Management Detail Component', () => {
    let comp: IPSDetailComponent;
    let fixture: ComponentFixture<IPSDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IPSDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ iPS: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IPSDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IPSDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load iPS on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.iPS).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
