import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPeso, Peso } from '../peso.model';

import { PesoService } from './peso.service';

describe('Service Tests', () => {
  describe('Peso Service', () => {
    let service: PesoService;
    let httpMock: HttpTestingController;
    let elemDefault: IPeso;
    let expectedResult: IPeso | IPeso[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PesoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        pesoKG: 0,
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

      it('should create a Peso', () => {
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

        service.create(new Peso()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Peso', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pesoKG: 1,
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

      it('should partial update a Peso', () => {
        const patchObject = Object.assign(
          {
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          new Peso()
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

      it('should return a list of Peso', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pesoKG: 1,
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

      it('should delete a Peso', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPesoToCollectionIfMissing', () => {
        it('should add a Peso to an empty array', () => {
          const peso: IPeso = { id: 123 };
          expectedResult = service.addPesoToCollectionIfMissing([], peso);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(peso);
        });

        it('should not add a Peso to an array that contains it', () => {
          const peso: IPeso = { id: 123 };
          const pesoCollection: IPeso[] = [
            {
              ...peso,
            },
            { id: 456 },
          ];
          expectedResult = service.addPesoToCollectionIfMissing(pesoCollection, peso);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Peso to an array that doesn't contain it", () => {
          const peso: IPeso = { id: 123 };
          const pesoCollection: IPeso[] = [{ id: 456 }];
          expectedResult = service.addPesoToCollectionIfMissing(pesoCollection, peso);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(peso);
        });

        it('should add only unique Peso to an array', () => {
          const pesoArray: IPeso[] = [{ id: 123 }, { id: 456 }, { id: 2802 }];
          const pesoCollection: IPeso[] = [{ id: 123 }];
          expectedResult = service.addPesoToCollectionIfMissing(pesoCollection, ...pesoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const peso: IPeso = { id: 123 };
          const peso2: IPeso = { id: 456 };
          expectedResult = service.addPesoToCollectionIfMissing([], peso, peso2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(peso);
          expect(expectedResult).toContain(peso2);
        });

        it('should accept null and undefined values', () => {
          const peso: IPeso = { id: 123 };
          expectedResult = service.addPesoToCollectionIfMissing([], null, peso, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(peso);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
