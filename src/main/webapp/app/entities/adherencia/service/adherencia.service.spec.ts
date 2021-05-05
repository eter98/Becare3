import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAdherencia, Adherencia } from '../adherencia.model';

import { AdherenciaService } from './adherencia.service';

describe('Service Tests', () => {
  describe('Adherencia Service', () => {
    let service: AdherenciaService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdherencia;
    let expectedResult: IAdherencia | IAdherencia[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdherenciaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        horaToma: currentDate,
        respuesta: false,
        valor: 0,
        comentario: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            horaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Adherencia', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            horaToma: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            horaToma: currentDate,
          },
          returnedFromService
        );

        service.create(new Adherencia()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Adherencia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            horaToma: currentDate.format(DATE_TIME_FORMAT),
            respuesta: true,
            valor: 1,
            comentario: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            horaToma: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Adherencia', () => {
        const patchObject = Object.assign(
          {
            horaToma: currentDate.format(DATE_TIME_FORMAT),
            respuesta: true,
          },
          new Adherencia()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            horaToma: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Adherencia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            horaToma: currentDate.format(DATE_TIME_FORMAT),
            respuesta: true,
            valor: 1,
            comentario: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            horaToma: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Adherencia', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdherenciaToCollectionIfMissing', () => {
        it('should add a Adherencia to an empty array', () => {
          const adherencia: IAdherencia = { id: 123 };
          expectedResult = service.addAdherenciaToCollectionIfMissing([], adherencia);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adherencia);
        });

        it('should not add a Adherencia to an array that contains it', () => {
          const adherencia: IAdherencia = { id: 123 };
          const adherenciaCollection: IAdherencia[] = [
            {
              ...adherencia,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdherenciaToCollectionIfMissing(adherenciaCollection, adherencia);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Adherencia to an array that doesn't contain it", () => {
          const adherencia: IAdherencia = { id: 123 };
          const adherenciaCollection: IAdherencia[] = [{ id: 456 }];
          expectedResult = service.addAdherenciaToCollectionIfMissing(adherenciaCollection, adherencia);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adherencia);
        });

        it('should add only unique Adherencia to an array', () => {
          const adherenciaArray: IAdherencia[] = [{ id: 123 }, { id: 456 }, { id: 15770 }];
          const adherenciaCollection: IAdherencia[] = [{ id: 123 }];
          expectedResult = service.addAdherenciaToCollectionIfMissing(adherenciaCollection, ...adherenciaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const adherencia: IAdherencia = { id: 123 };
          const adherencia2: IAdherencia = { id: 456 };
          expectedResult = service.addAdherenciaToCollectionIfMissing([], adherencia, adherencia2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adherencia);
          expect(expectedResult).toContain(adherencia2);
        });

        it('should accept null and undefined values', () => {
          const adherencia: IAdherencia = { id: 123 };
          expectedResult = service.addAdherenciaToCollectionIfMissing([], null, adherencia, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adherencia);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
