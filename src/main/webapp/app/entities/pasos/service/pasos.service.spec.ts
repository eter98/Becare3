import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPasos, Pasos } from '../pasos.model';

import { PasosService } from './pasos.service';

describe('Service Tests', () => {
  describe('Pasos Service', () => {
    let service: PasosService;
    let httpMock: HttpTestingController;
    let elemDefault: IPasos;
    let expectedResult: IPasos | IPasos[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PasosService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nroPasos: 0,
        timeInstant: currentDate,
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

      it('should create a Pasos', () => {
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

        service.create(new Pasos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Pasos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nroPasos: 1,
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Pasos', () => {
        const patchObject = Object.assign(
          {
            nroPasos: 1,
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          new Pasos()
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

      it('should return a list of Pasos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nroPasos: 1,
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Pasos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPasosToCollectionIfMissing', () => {
        it('should add a Pasos to an empty array', () => {
          const pasos: IPasos = { id: 123 };
          expectedResult = service.addPasosToCollectionIfMissing([], pasos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pasos);
        });

        it('should not add a Pasos to an array that contains it', () => {
          const pasos: IPasos = { id: 123 };
          const pasosCollection: IPasos[] = [
            {
              ...pasos,
            },
            { id: 456 },
          ];
          expectedResult = service.addPasosToCollectionIfMissing(pasosCollection, pasos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Pasos to an array that doesn't contain it", () => {
          const pasos: IPasos = { id: 123 };
          const pasosCollection: IPasos[] = [{ id: 456 }];
          expectedResult = service.addPasosToCollectionIfMissing(pasosCollection, pasos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pasos);
        });

        it('should add only unique Pasos to an array', () => {
          const pasosArray: IPasos[] = [{ id: 123 }, { id: 456 }, { id: 89363 }];
          const pasosCollection: IPasos[] = [{ id: 123 }];
          expectedResult = service.addPasosToCollectionIfMissing(pasosCollection, ...pasosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pasos: IPasos = { id: 123 };
          const pasos2: IPasos = { id: 456 };
          expectedResult = service.addPasosToCollectionIfMissing([], pasos, pasos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pasos);
          expect(expectedResult).toContain(pasos2);
        });

        it('should accept null and undefined values', () => {
          const pasos: IPasos = { id: 123 };
          expectedResult = service.addPasosToCollectionIfMissing([], null, pasos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pasos);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
