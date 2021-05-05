import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { TratamietoDetailComponent } from './tratamieto-detail.component';

describe('Component Tests', () => {
  describe('Tratamieto Management Detail Component', () => {
    let comp: TratamietoDetailComponent;
    let fixture: ComponentFixture<TratamietoDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TratamietoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tratamieto: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TratamietoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TratamietoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load tratamieto on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tratamieto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
