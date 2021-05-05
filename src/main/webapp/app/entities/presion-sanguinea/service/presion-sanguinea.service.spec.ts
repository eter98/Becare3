import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPresionSanguinea, PresionSanguinea } from '../presion-sanguinea.model';

import { PresionSanguineaService } from './presion-sanguinea.service';

describe('Service Tests', () => {
  describe('PresionSanguinea Service', () => {
    let service: PresionSanguineaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPresionSanguinea;
    let expectedResult: IPresionSanguinea | IPresionSanguinea[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PresionSanguineaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        presionSanguineaSistolica: 0,
        presionSanguineaDiastolica: 0,
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

      it('should create a PresionSanguinea', () => {
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

        service.create(new PresionSanguinea()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PresionSanguinea', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            presionSanguineaSistolica: 1,
            presionSanguineaDiastolica: 1,
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

      it('should partial update a PresionSanguinea', () => {
        const patchObject = Object.assign(
          {
            presionSanguineaDiastolica: 1,
            fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          },
          new PresionSanguinea()
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

      it('should return a list of PresionSanguinea', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            presionSanguineaSistolica: 1,
            presionSanguineaDiastolica: 1,
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

      it('should delete a PresionSanguinea', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPresionSanguineaToCollectionIfMissing', () => {
        it('should add a PresionSanguinea to an empty array', () => {
          const presionSanguinea: IPresionSanguinea = { id: 123 };
          expectedResult = service.addPresionSanguineaToCollectionIfMissing([], presionSanguinea);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(presionSanguinea);
        });

        it('should not add a PresionSanguinea to an array that contains it', () => {
          const presionSanguinea: IPresionSanguinea = { id: 123 };
          const presionSanguineaCollection: IPresionSanguinea[] = [
            {
              ...presionSanguinea,
            },
            { id: 456 },
          ];
          expectedResult = service.addPresionSanguineaToCollectionIfMissing(presionSanguineaCollection, presionSanguinea);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PresionSanguinea to an array that doesn't contain it", () => {
          const presionSanguinea: IPresionSanguinea = { id: 123 };
          const presionSanguineaCollection: IPresionSanguinea[] = [{ id: 456 }];
          expectedResult = service.addPresionSanguineaToCollectionIfMissing(presionSanguineaCollection, presionSanguinea);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(presionSanguinea);
        });

        it('should add only unique PresionSanguinea to an array', () => {
          const presionSanguineaArray: IPresionSanguinea[] = [{ id: 123 }, { id: 456 }, { id: 52135 }];
          const presionSanguineaCollection: IPresionSanguinea[] = [{ id: 123 }];
          expectedResult = service.addPresionSanguineaToCollectionIfMissing(presionSanguineaCollection, ...presionSanguineaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const presionSanguinea: IPresionSanguinea = { id: 123 };
          const presionSanguinea2: IPresionSanguinea = { id: 456 };
          expectedResult = service.addPresionSanguineaToCollectionIfMissing([], presionSanguinea, presionSanguinea2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(presionSanguinea);
          expect(expectedResult).toContain(presionSanguinea2);
        });

        it('should accept null and undefined values', () => {
          const presionSanguinea: IPresionSanguinea = { id: 123 };
          expectedResult = service.addPresionSanguineaToCollectionIfMissing([], null, presionSanguinea, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(presionSanguinea);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
