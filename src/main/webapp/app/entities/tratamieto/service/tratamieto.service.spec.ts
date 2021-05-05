import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITratamieto, Tratamieto } from '../tratamieto.model';

import { TratamietoService } from './tratamieto.service';

describe('Service Tests', () => {
  describe('Tratamieto Service', () => {
    let service: TratamietoService;
    let httpMock: HttpTestingController;
    let elemDefault: ITratamieto;
    let expectedResult: ITratamieto | ITratamieto[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TratamietoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        descripcionTratamiento: 'AAAAAAA',
        fechaInicio: currentDate,
        fechaFin: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tratamieto', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaInicio: currentDate,
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.create(new Tratamieto()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tratamieto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descripcionTratamiento: 'BBBBBB',
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaInicio: currentDate,
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tratamieto', () => {
        const patchObject = Object.assign(
          {
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          new Tratamieto()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaInicio: currentDate,
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tratamieto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descripcionTratamiento: 'BBBBBB',
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaInicio: currentDate,
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tratamieto', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTratamietoToCollectionIfMissing', () => {
        it('should add a Tratamieto to an empty array', () => {
          const tratamieto: ITratamieto = { id: 123 };
          expectedResult = service.addTratamietoToCollectionIfMissing([], tratamieto);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tratamieto);
        });

        it('should not add a Tratamieto to an array that contains it', () => {
          const tratamieto: ITratamieto = { id: 123 };
          const tratamietoCollection: ITratamieto[] = [
            {
              ...tratamieto,
            },
            { id: 456 },
          ];
          expectedResult = service.addTratamietoToCollectionIfMissing(tratamietoCollection, tratamieto);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tratamieto to an array that doesn't contain it", () => {
          const tratamieto: ITratamieto = { id: 123 };
          const tratamietoCollection: ITratamieto[] = [{ id: 456 }];
          expectedResult = service.addTratamietoToCollectionIfMissing(tratamietoCollection, tratamieto);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tratamieto);
        });

        it('should add only unique Tratamieto to an array', () => {
          const tratamietoArray: ITratamieto[] = [{ id: 123 }, { id: 456 }, { id: 27405 }];
          const tratamietoCollection: ITratamieto[] = [{ id: 123 }];
          expectedResult = service.addTratamietoToCollectionIfMissing(tratamietoCollection, ...tratamietoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tratamieto: ITratamieto = { id: 123 };
          const tratamieto2: ITratamieto = { id: 456 };
          expectedResult = service.addTratamietoToCollectionIfMissing([], tratamieto, tratamieto2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tratamieto);
          expect(expectedResult).toContain(tratamieto2);
        });

        it('should accept null and undefined values', () => {
          const tratamieto: ITratamieto = { id: 123 };
          expectedResult = service.addTratamietoToCollectionIfMissing([], null, tratamieto, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tratamieto);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
