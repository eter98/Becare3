import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFrecuenciaCardiaca, FrecuenciaCardiaca } from '../frecuencia-cardiaca.model';

import { FrecuenciaCardiacaService } from './frecuencia-cardiaca.service';

describe('Service Tests', () => {
  describe('FrecuenciaCardiaca Service', () => {
    let service: FrecuenciaCardiacaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFrecuenciaCardiaca;
    let expectedResult: IFrecuenciaCardiaca | IFrecuenciaCardiaca[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FrecuenciaCardiacaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        frecuenciaCardiaca: 0,
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

      it('should create a FrecuenciaCardiaca', () => {
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

        service.create(new FrecuenciaCardiaca()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FrecuenciaCardiaca', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            frecuenciaCardiaca: 1,
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

      it('should partial update a FrecuenciaCardiaca', () => {
        const patchObject = Object.assign(
          {
            frecuenciaCardiaca: 1,
          },
          new FrecuenciaCardiaca()
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

      it('should return a list of FrecuenciaCardiaca', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            frecuenciaCardiaca: 1,
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

      it('should delete a FrecuenciaCardiaca', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFrecuenciaCardiacaToCollectionIfMissing', () => {
        it('should add a FrecuenciaCardiaca to an empty array', () => {
          const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 123 };
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing([], frecuenciaCardiaca);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frecuenciaCardiaca);
        });

        it('should not add a FrecuenciaCardiaca to an array that contains it', () => {
          const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 123 };
          const frecuenciaCardiacaCollection: IFrecuenciaCardiaca[] = [
            {
              ...frecuenciaCardiaca,
            },
            { id: 456 },
          ];
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing(frecuenciaCardiacaCollection, frecuenciaCardiaca);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FrecuenciaCardiaca to an array that doesn't contain it", () => {
          const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 123 };
          const frecuenciaCardiacaCollection: IFrecuenciaCardiaca[] = [{ id: 456 }];
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing(frecuenciaCardiacaCollection, frecuenciaCardiaca);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frecuenciaCardiaca);
        });

        it('should add only unique FrecuenciaCardiaca to an array', () => {
          const frecuenciaCardiacaArray: IFrecuenciaCardiaca[] = [{ id: 123 }, { id: 456 }, { id: 63713 }];
          const frecuenciaCardiacaCollection: IFrecuenciaCardiaca[] = [{ id: 123 }];
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing(frecuenciaCardiacaCollection, ...frecuenciaCardiacaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 123 };
          const frecuenciaCardiaca2: IFrecuenciaCardiaca = { id: 456 };
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing([], frecuenciaCardiaca, frecuenciaCardiaca2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frecuenciaCardiaca);
          expect(expectedResult).toContain(frecuenciaCardiaca2);
        });

        it('should accept null and undefined values', () => {
          const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 123 };
          expectedResult = service.addFrecuenciaCardiacaToCollectionIfMissing([], null, frecuenciaCardiaca, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frecuenciaCardiaca);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
