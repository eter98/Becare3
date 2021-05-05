import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICondicion, Condicion } from '../condicion.model';

import { CondicionService } from './condicion.service';

describe('Service Tests', () => {
  describe('Condicion Service', () => {
    let service: CondicionService;
    let httpMock: HttpTestingController;
    let elemDefault: ICondicion;
    let expectedResult: ICondicion | ICondicion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CondicionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        condicion: 'AAAAAAA',
        descripcion: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Condicion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Condicion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Condicion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            condicion: 'BBBBBB',
            descripcion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Condicion', () => {
        const patchObject = Object.assign(
          {
            condicion: 'BBBBBB',
          },
          new Condicion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Condicion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            condicion: 'BBBBBB',
            descripcion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Condicion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCondicionToCollectionIfMissing', () => {
        it('should add a Condicion to an empty array', () => {
          const condicion: ICondicion = { id: 123 };
          expectedResult = service.addCondicionToCollectionIfMissing([], condicion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(condicion);
        });

        it('should not add a Condicion to an array that contains it', () => {
          const condicion: ICondicion = { id: 123 };
          const condicionCollection: ICondicion[] = [
            {
              ...condicion,
            },
            { id: 456 },
          ];
          expectedResult = service.addCondicionToCollectionIfMissing(condicionCollection, condicion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Condicion to an array that doesn't contain it", () => {
          const condicion: ICondicion = { id: 123 };
          const condicionCollection: ICondicion[] = [{ id: 456 }];
          expectedResult = service.addCondicionToCollectionIfMissing(condicionCollection, condicion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(condicion);
        });

        it('should add only unique Condicion to an array', () => {
          const condicionArray: ICondicion[] = [{ id: 123 }, { id: 456 }, { id: 15306 }];
          const condicionCollection: ICondicion[] = [{ id: 123 }];
          expectedResult = service.addCondicionToCollectionIfMissing(condicionCollection, ...condicionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const condicion: ICondicion = { id: 123 };
          const condicion2: ICondicion = { id: 456 };
          expectedResult = service.addCondicionToCollectionIfMissing([], condicion, condicion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(condicion);
          expect(expectedResult).toContain(condicion2);
        });

        it('should accept null and undefined values', () => {
          const condicion: ICondicion = { id: 123 };
          expectedResult = service.addCondicionToCollectionIfMissing([], null, condicion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(condicion);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
