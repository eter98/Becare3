import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIngesta, Ingesta } from '../ingesta.model';

import { IngestaService } from './ingesta.service';

describe('Service Tests', () => {
  describe('Ingesta Service', () => {
    let service: IngestaService;
    let httpMock: HttpTestingController;
    let elemDefault: IIngesta;
    let expectedResult: IIngesta | IIngesta[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IngestaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        tipo: 'AAAAAAA',
        consumoCalorias: 0,
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

      it('should create a Ingesta', () => {
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

        service.create(new Ingesta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ingesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            consumoCalorias: 1,
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

      it('should partial update a Ingesta', () => {
        const patchObject = Object.assign(
          {
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          new Ingesta()
        );

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

      it('should return a list of Ingesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            consumoCalorias: 1,
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

      it('should delete a Ingesta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIngestaToCollectionIfMissing', () => {
        it('should add a Ingesta to an empty array', () => {
          const ingesta: IIngesta = { id: 123 };
          expectedResult = service.addIngestaToCollectionIfMissing([], ingesta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ingesta);
        });

        it('should not add a Ingesta to an array that contains it', () => {
          const ingesta: IIngesta = { id: 123 };
          const ingestaCollection: IIngesta[] = [
            {
              ...ingesta,
            },
            { id: 456 },
          ];
          expectedResult = service.addIngestaToCollectionIfMissing(ingestaCollection, ingesta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ingesta to an array that doesn't contain it", () => {
          const ingesta: IIngesta = { id: 123 };
          const ingestaCollection: IIngesta[] = [{ id: 456 }];
          expectedResult = service.addIngestaToCollectionIfMissing(ingestaCollection, ingesta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ingesta);
        });

        it('should add only unique Ingesta to an array', () => {
          const ingestaArray: IIngesta[] = [{ id: 123 }, { id: 456 }, { id: 49035 }];
          const ingestaCollection: IIngesta[] = [{ id: 123 }];
          expectedResult = service.addIngestaToCollectionIfMissing(ingestaCollection, ...ingestaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ingesta: IIngesta = { id: 123 };
          const ingesta2: IIngesta = { id: 456 };
          expectedResult = service.addIngestaToCollectionIfMissing([], ingesta, ingesta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ingesta);
          expect(expectedResult).toContain(ingesta2);
        });

        it('should accept null and undefined values', () => {
          const ingesta: IIngesta = { id: 123 };
          expectedResult = service.addIngestaToCollectionIfMissing([], null, ingesta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ingesta);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
