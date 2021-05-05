import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITokenDisp, TokenDisp } from '../token-disp.model';

import { TokenDispService } from './token-disp.service';

describe('Service Tests', () => {
  describe('TokenDisp Service', () => {
    let service: TokenDispService;
    let httpMock: HttpTestingController;
    let elemDefault: ITokenDisp;
    let expectedResult: ITokenDisp | ITokenDisp[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TokenDispService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        tokenConexion: 'AAAAAAA',
        activo: false,
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

      it('should create a TokenDisp', () => {
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

        service.create(new TokenDisp()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TokenDisp', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tokenConexion: 'BBBBBB',
            activo: true,
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

      it('should partial update a TokenDisp', () => {
        const patchObject = Object.assign(
          {
            tokenConexion: 'BBBBBB',
            fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          },
          new TokenDisp()
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

      it('should return a list of TokenDisp', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tokenConexion: 'BBBBBB',
            activo: true,
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

      it('should delete a TokenDisp', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTokenDispToCollectionIfMissing', () => {
        it('should add a TokenDisp to an empty array', () => {
          const tokenDisp: ITokenDisp = { id: 123 };
          expectedResult = service.addTokenDispToCollectionIfMissing([], tokenDisp);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tokenDisp);
        });

        it('should not add a TokenDisp to an array that contains it', () => {
          const tokenDisp: ITokenDisp = { id: 123 };
          const tokenDispCollection: ITokenDisp[] = [
            {
              ...tokenDisp,
            },
            { id: 456 },
          ];
          expectedResult = service.addTokenDispToCollectionIfMissing(tokenDispCollection, tokenDisp);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TokenDisp to an array that doesn't contain it", () => {
          const tokenDisp: ITokenDisp = { id: 123 };
          const tokenDispCollection: ITokenDisp[] = [{ id: 456 }];
          expectedResult = service.addTokenDispToCollectionIfMissing(tokenDispCollection, tokenDisp);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tokenDisp);
        });

        it('should add only unique TokenDisp to an array', () => {
          const tokenDispArray: ITokenDisp[] = [{ id: 123 }, { id: 456 }, { id: 44960 }];
          const tokenDispCollection: ITokenDisp[] = [{ id: 123 }];
          expectedResult = service.addTokenDispToCollectionIfMissing(tokenDispCollection, ...tokenDispArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tokenDisp: ITokenDisp = { id: 123 };
          const tokenDisp2: ITokenDisp = { id: 456 };
          expectedResult = service.addTokenDispToCollectionIfMissing([], tokenDisp, tokenDisp2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tokenDisp);
          expect(expectedResult).toContain(tokenDisp2);
        });

        it('should accept null and undefined values', () => {
          const tokenDisp: ITokenDisp = { id: 123 };
          expectedResult = service.addTokenDispToCollectionIfMissing([], null, tokenDisp, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tokenDisp);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
