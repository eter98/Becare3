import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOximetria, Oximetria } from '../oximetria.model';

import { OximetriaService } from './oximetria.service';

describe('Service Tests', () => {
  describe('Oximetria Service', () => {
    let service: OximetriaService;
    let httpMock: HttpTestingController;
    let elemDefault: IOximetria;
    let expectedResult: IOximetria | IOximetria[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OximetriaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        oximetria: 0,
        fechaRegistro: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Oximetria', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
          },
          returnedFromService
        );

        service.create(new Oximetria()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Oximetria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            oximetria: 1,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Oximetria', () => {
        const patchObject = Object.assign({}, new Oximetria());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Oximetria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            oximetria: 1,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Oximetria', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOximetriaToCollectionIfMissing', () => {
        it('should add a Oximetria to an empty array', () => {
          const oximetria: IOximetria = { id: 123 };
          expectedResult = service.addOximetriaToCollectionIfMissing([], oximetria);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(oximetria);
        });

        it('should not add a Oximetria to an array that contains it', () => {
          const oximetria: IOximetria = { id: 123 };
          const oximetriaCollection: IOximetria[] = [
            {
              ...oximetria,
            },
            { id: 456 },
          ];
          expectedResult = service.addOximetriaToCollectionIfMissing(oximetriaCollection, oximetria);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Oximetria to an array that doesn't contain it", () => {
          const oximetria: IOximetria = { id: 123 };
          const oximetriaCollection: IOximetria[] = [{ id: 456 }];
          expectedResult = service.addOximetriaToCollectionIfMissing(oximetriaCollection, oximetria);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(oximetria);
        });

        it('should add only unique Oximetria to an array', () => {
          const oximetriaArray: IOximetria[] = [{ id: 123 }, { id: 456 }, { id: 17180 }];
          const oximetriaCollection: IOximetria[] = [{ id: 123 }];
          expectedResult = service.addOximetriaToCollectionIfMissing(oximetriaCollection, ...oximetriaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const oximetria: IOximetria = { id: 123 };
          const oximetria2: IOximetria = { id: 456 };
          expectedResult = service.addOximetriaToCollectionIfMissing([], oximetria, oximetria2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(oximetria);
          expect(expectedResult).toContain(oximetria2);
        });

        it('should accept null and undefined values', () => {
          const oximetria: IOximetria = { id: 123 };
          expectedResult = service.addOximetriaToCollectionIfMissing([], null, oximetria, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(oximetria);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
