import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TokenDispDetailComponent } from './token-disp-detail.component';

describe('Component Tests', () => {
  describe('TokenDisp Management Detail Component', () => {
    let comp: TokenDispDetailComponent;
    let fixture: ComponentFixture<TokenDispDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TokenDispDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tokenDisp: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TokenDispDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TokenDispDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tokenDisp on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tokenDisp).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
