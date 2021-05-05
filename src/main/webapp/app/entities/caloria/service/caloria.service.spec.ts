import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICaloria, Caloria } from '../caloria.model';

import { CaloriaService } from './caloria.service';

describe('Service Tests', () => {
  describe('Caloria Service', () => {
    let service: CaloriaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICaloria;
    let expectedResult: ICaloria | ICaloria[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CaloriaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        caloriasActivas: 0,
        descripcion: 'AAAAAAA',
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

      it('should create a Caloria', () => {
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

        service.create(new Caloria()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Caloria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            caloriasActivas: 1,
            descripcion: 'BBBBBB',
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

      it('should partial update a Caloria', () => {
        const patchObject = Object.assign(
          {
            caloriasActivas: 1,
            descripcion: 'BBBBBB',
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          new Caloria()
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

      it('should return a list of Caloria', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            caloriasActivas: 1,
            descripcion: 'BBBBBB',
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

      it('should delete a Caloria', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCaloriaToCollectionIfMissing', () => {
        it('should add a Caloria to an empty array', () => {
          const caloria: ICaloria = { id: 123 };
          expectedResult = service.addCaloriaToCollectionIfMissing([], caloria);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(caloria);
        });

        it('should not add a Caloria to an array that contains it', () => {
          const caloria: ICaloria = { id: 123 };
          const caloriaCollection: ICaloria[] = [
            {
              ...caloria,
            },
            { id: 456 },
          ];
          expectedResult = service.addCaloriaToCollectionIfMissing(caloriaCollection, caloria);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Caloria to an array that doesn't contain it", () => {
          const caloria: ICaloria = { id: 123 };
          const caloriaCollection: ICaloria[] = [{ id: 456 }];
          expectedResult = service.addCaloriaToCollectionIfMissing(caloriaCollection, caloria);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(caloria);
        });

        it('should add only unique Caloria to an array', () => {
          const caloriaArray: ICaloria[] = [{ id: 123 }, { id: 456 }, { id: 81545 }];
          const caloriaCollection: ICaloria[] = [{ id: 123 }];
          expectedResult = service.addCaloriaToCollectionIfMissing(caloriaCollection, ...caloriaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const caloria: ICaloria = { id: 123 };
          const caloria2: ICaloria = { id: 456 };
          expectedResult = service.addCaloriaToCollectionIfMissing([], caloria, caloria2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(caloria);
          expect(expectedResult).toContain(caloria2);
        });

        it('should accept null and undefined values', () => {
          const caloria: ICaloria = { id: 123 };
          expectedResult = service.addCaloriaToCollectionIfMissing([], null, caloria, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(caloria);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
