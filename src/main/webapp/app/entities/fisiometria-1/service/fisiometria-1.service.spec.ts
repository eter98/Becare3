import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFisiometria1, Fisiometria1 } from '../fisiometria-1.model';

import { Fisiometria1Service } from './fisiometria-1.service';

describe('Service Tests', () => {
  describe('Fisiometria1 Service', () => {
    let service: Fisiometria1Service;
    let httpMock: HttpTestingController;
    let elemDefault: IFisiometria1;
    let expectedResult: IFisiometria1 | IFisiometria1[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(Fisiometria1Service);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        ritmoCardiaco: 0,
        ritmoRespiratorio: 0,
        oximetria: 0,
        presionArterialSistolica: 0,
        presionArterialDiastolica: 0,
        temperatura: 0,
        fechaRegistro: currentDate,
        fechaToma: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
            fechaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fisiometria1', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
            fechaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
            fechaToma: currentDate,
          },
          returnedFromService
        );

        service.create(new Fisiometria1()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fisiometria1', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ritmoCardiaco: 1,
            ritmoRespiratorio: 1,
            oximetria: 1,
            presionArterialSistolica: 1,
            presionArterialDiastolica: 1,
            temperatura: 1,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
            fechaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
            fechaToma: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fisiometria1', () => {
        const patchObject = Object.assign(
          {
            ritmoCardiaco: 1,
            ritmoRespiratorio: 1,
            oximetria: 1,
            presionArterialSistolica: 1,
            fechaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          new Fisiometria1()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
            fechaToma: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fisiometria1', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ritmoCardiaco: 1,
            ritmoRespiratorio: 1,
            oximetria: 1,
            presionArterialSistolica: 1,
            presionArterialDiastolica: 1,
            temperatura: 1,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
            fechaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaRegistro: currentDate,
            fechaToma: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fisiometria1', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFisiometria1ToCollectionIfMissing', () => {
        it('should add a Fisiometria1 to an empty array', () => {
          const fisiometria1: IFisiometria1 = { id: 123 };
          expectedResult = service.addFisiometria1ToCollectionIfMissing([], fisiometria1);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fisiometria1);
        });

        it('should not add a Fisiometria1 to an array that contains it', () => {
          const fisiometria1: IFisiometria1 = { id: 123 };
          const fisiometria1Collection: IFisiometria1[] = [
            {
              ...fisiometria1,
            },
            { id: 456 },
          ];
          expectedResult = service.addFisiometria1ToCollectionIfMissing(fisiometria1Collection, fisiometria1);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fisiometria1 to an array that doesn't contain it", () => {
          const fisiometria1: IFisiometria1 = { id: 123 };
          const fisiometria1Collection: IFisiometria1[] = [{ id: 456 }];
          expectedResult = service.addFisiometria1ToCollectionIfMissing(fisiometria1Collection, fisiometria1);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fisiometria1);
        });

        it('should add only unique Fisiometria1 to an array', () => {
          const fisiometria1Array: IFisiometria1[] = [{ id: 123 }, { id: 456 }, { id: 37248 }];
          const fisiometria1Collection: IFisiometria1[] = [{ id: 123 }];
          expectedResult = service.addFisiometria1ToCollectionIfMissing(fisiometria1Collection, ...fisiometria1Array);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fisiometria1: IFisiometria1 = { id: 123 };
          const fisiometria12: IFisiometria1 = { id: 456 };
          expectedResult = service.addFisiometria1ToCollectionIfMissing([], fisiometria1, fisiometria12);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fisiometria1);
          expect(expectedResult).toContain(fisiometria12);
        });

        it('should accept null and undefined values', () => {
          const fisiometria1: IFisiometria1 = { id: 123 };
          expectedResult = service.addFisiometria1ToCollectionIfMissing([], null, fisiometria1, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fisiometria1);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
