import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITemperatura, Temperatura } from '../temperatura.model';

import { TemperaturaService } from './temperatura.service';

describe('Service Tests', () => {
  describe('Temperatura Service', () => {
    let service: TemperaturaService;
    let httpMock: HttpTestingController;
    let elemDefault: ITemperatura;
    let expectedResult: ITemperatura | ITemperatura[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TemperaturaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        temperatura: 0,
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

      it('should create a Temperatura', () => {
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

        service.create(new Temperatura()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Temperatura', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            temperatura: 1,
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

      it('should partial update a Temperatura', () => {
        const patchObject = Object.assign({}, new Temperatura());

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

      it('should return a list of Temperatura', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            temperatura: 1,
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

      it('should delete a Temperatura', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTemperaturaToCollectionIfMissing', () => {
        it('should add a Temperatura to an empty array', () => {
          const temperatura: ITemperatura = { id: 123 };
          expectedResult = service.addTemperaturaToCollectionIfMissing([], temperatura);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(temperatura);
        });

        it('should not add a Temperatura to an array that contains it', () => {
          const temperatura: ITemperatura = { id: 123 };
          const temperaturaCollection: ITemperatura[] = [
            {
              ...temperatura,
            },
            { id: 456 },
          ];
          expectedResult = service.addTemperaturaToCollectionIfMissing(temperaturaCollection, temperatura);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Temperatura to an array that doesn't contain it", () => {
          const temperatura: ITemperatura = { id: 123 };
          const temperaturaCollection: ITemperatura[] = [{ id: 456 }];
          expectedResult = service.addTemperaturaToCollectionIfMissing(temperaturaCollection, temperatura);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(temperatura);
        });

        it('should add only unique Temperatura to an array', () => {
          const temperaturaArray: ITemperatura[] = [{ id: 123 }, { id: 456 }, { id: 918 }];
          const temperaturaCollection: ITemperatura[] = [{ id: 123 }];
          expectedResult = service.addTemperaturaToCollectionIfMissing(temperaturaCollection, ...temperaturaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const temperatura: ITemperatura = { id: 123 };
          const temperatura2: ITemperatura = { id: 456 };
          expectedResult = service.addTemperaturaToCollectionIfMissing([], temperatura, temperatura2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(temperatura);
          expect(expectedResult).toContain(temperatura2);
        });

        it('should accept null and undefined values', () => {
          const temperatura: ITemperatura = { id: 123 };
          expectedResult = service.addTemperaturaToCollectionIfMissing([], null, temperatura, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(temperatura);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
