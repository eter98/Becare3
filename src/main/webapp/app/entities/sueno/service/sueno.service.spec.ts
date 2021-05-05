import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISueno, Sueno } from '../sueno.model';

import { SuenoService } from './sueno.service';

describe('Service Tests', () => {
  describe('Sueno Service', () => {
    let service: SuenoService;
    let httpMock: HttpTestingController;
    let elemDefault: ISueno;
    let expectedResult: ISueno | ISueno[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SuenoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        superficial: 0,
        profundo: 0,
        despierto: 0,
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

      it('should create a Sueno', () => {
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

        service.create(new Sueno()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sueno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            superficial: 1,
            profundo: 1,
            despierto: 1,
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

      it('should partial update a Sueno', () => {
        const patchObject = Object.assign(
          {
            superficial: 1,
            timeInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          new Sueno()
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

      it('should return a list of Sueno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            superficial: 1,
            profundo: 1,
            despierto: 1,
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

      it('should delete a Sueno', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSuenoToCollectionIfMissing', () => {
        it('should add a Sueno to an empty array', () => {
          const sueno: ISueno = { id: 123 };
          expectedResult = service.addSuenoToCollectionIfMissing([], sueno);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sueno);
        });

        it('should not add a Sueno to an array that contains it', () => {
          const sueno: ISueno = { id: 123 };
          const suenoCollection: ISueno[] = [
            {
              ...sueno,
            },
            { id: 456 },
          ];
          expectedResult = service.addSuenoToCollectionIfMissing(suenoCollection, sueno);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sueno to an array that doesn't contain it", () => {
          const sueno: ISueno = { id: 123 };
          const suenoCollection: ISueno[] = [{ id: 456 }];
          expectedResult = service.addSuenoToCollectionIfMissing(suenoCollection, sueno);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sueno);
        });

        it('should add only unique Sueno to an array', () => {
          const suenoArray: ISueno[] = [{ id: 123 }, { id: 456 }, { id: 9566 }];
          const suenoCollection: ISueno[] = [{ id: 123 }];
          expectedResult = service.addSuenoToCollectionIfMissing(suenoCollection, ...suenoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sueno: ISueno = { id: 123 };
          const sueno2: ISueno = { id: 456 };
          expectedResult = service.addSuenoToCollectionIfMissing([], sueno, sueno2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sueno);
          expect(expectedResult).toContain(sueno2);
        });

        it('should accept null and undefined values', () => {
          const sueno: ISueno = { id: 123 };
          expectedResult = service.addSuenoToCollectionIfMissing([], null, sueno, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sueno);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
