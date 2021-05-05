import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPrograma, Programa } from '../programa.model';

import { ProgramaService } from './programa.service';

describe('Service Tests', () => {
  describe('Programa Service', () => {
    let service: ProgramaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrograma;
    let expectedResult: IPrograma | IPrograma[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProgramaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        caloriasActividad: 0,
        pasosActividad: 0,
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

      it('should create a Programa', () => {
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

        service.create(new Programa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Programa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            caloriasActividad: 1,
            pasosActividad: 1,
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

      it('should partial update a Programa', () => {
        const patchObject = Object.assign(
          {
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          new Programa()
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

      it('should return a list of Programa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            caloriasActividad: 1,
            pasosActividad: 1,
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

      it('should delete a Programa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProgramaToCollectionIfMissing', () => {
        it('should add a Programa to an empty array', () => {
          const programa: IPrograma = { id: 123 };
          expectedResult = service.addProgramaToCollectionIfMissing([], programa);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(programa);
        });

        it('should not add a Programa to an array that contains it', () => {
          const programa: IPrograma = { id: 123 };
          const programaCollection: IPrograma[] = [
            {
              ...programa,
            },
            { id: 456 },
          ];
          expectedResult = service.addProgramaToCollectionIfMissing(programaCollection, programa);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Programa to an array that doesn't contain it", () => {
          const programa: IPrograma = { id: 123 };
          const programaCollection: IPrograma[] = [{ id: 456 }];
          expectedResult = service.addProgramaToCollectionIfMissing(programaCollection, programa);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(programa);
        });

        it('should add only unique Programa to an array', () => {
          const programaArray: IPrograma[] = [{ id: 123 }, { id: 456 }, { id: 34825 }];
          const programaCollection: IPrograma[] = [{ id: 123 }];
          expectedResult = service.addProgramaToCollectionIfMissing(programaCollection, ...programaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const programa: IPrograma = { id: 123 };
          const programa2: IPrograma = { id: 456 };
          expectedResult = service.addProgramaToCollectionIfMissing([], programa, programa2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(programa);
          expect(expectedResult).toContain(programa2);
        });

        it('should accept null and undefined values', () => {
          const programa: IPrograma = { id: 123 };
          expectedResult = service.addProgramaToCollectionIfMissing([], null, programa, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(programa);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
