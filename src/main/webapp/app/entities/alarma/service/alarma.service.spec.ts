import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlarma, Alarma } from '../alarma.model';

import { AlarmaService } from './alarma.service';

describe('Service Tests', () => {
  describe('Alarma Service', () => {
    let service: AlarmaService;
    let httpMock: HttpTestingController;
    let elemDefault: IAlarma;
    let expectedResult: IAlarma | IAlarma[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AlarmaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        timeInstant: currentDate,
        descripcion: 'AAAAAAA',
        procedimiento: 'AAAAAAA',
        titulo: 'AAAAAAA',
        verificar: false,
        observaciones: 'AAAAAAA',
        prioridad: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Alarma', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeInstant: currentDate,
          },
          returnedFromService
        );

        service.create(new Alarma()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Alarma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
            descripcion: 'BBBBBB',
            procedimiento: 'BBBBBB',
            titulo: 'BBBBBB',
            verificar: true,
            observaciones: 'BBBBBB',
            prioridad: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeInstant: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Alarma', () => {
        const patchObject = Object.assign(
          {
            procedimiento: 'BBBBBB',
            prioridad: 'BBBBBB',
          },
          new Alarma()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            timeInstant: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Alarma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
            descripcion: 'BBBBBB',
            procedimiento: 'BBBBBB',
            titulo: 'BBBBBB',
            verificar: true,
            observaciones: 'BBBBBB',
            prioridad: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeInstant: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Alarma', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAlarmaToCollectionIfMissing', () => {
        it('should add a Alarma to an empty array', () => {
          const alarma: IAlarma = { id: 123 };
          expectedResult = service.addAlarmaToCollectionIfMissing([], alarma);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(alarma);
        });

        it('should not add a Alarma to an array that contains it', () => {
          const alarma: IAlarma = { id: 123 };
          const alarmaCollection: IAlarma[] = [
            {
              ...alarma,
            },
            { id: 456 },
          ];
          expectedResult = service.addAlarmaToCollectionIfMissing(alarmaCollection, alarma);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Alarma to an array that doesn't contain it", () => {
          const alarma: IAlarma = { id: 123 };
          const alarmaCollection: IAlarma[] = [{ id: 456 }];
          expectedResult = service.addAlarmaToCollectionIfMissing(alarmaCollection, alarma);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(alarma);
        });

        it('should add only unique Alarma to an array', () => {
          const alarmaArray: IAlarma[] = [{ id: 123 }, { id: 456 }, { id: 50476 }];
          const alarmaCollection: IAlarma[] = [{ id: 123 }];
          expectedResult = service.addAlarmaToCollectionIfMissing(alarmaCollection, ...alarmaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const alarma: IAlarma = { id: 123 };
          const alarma2: IAlarma = { id: 456 };
          expectedResult = service.addAlarmaToCollectionIfMissing([], alarma, alarma2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(alarma);
          expect(expectedResult).toContain(alarma2);
        });

        it('should accept null and undefined values', () => {
          const alarma: IAlarma = { id: 123 };
          expectedResult = service.addAlarmaToCollectionIfMissing([], null, alarma, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(alarma);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
