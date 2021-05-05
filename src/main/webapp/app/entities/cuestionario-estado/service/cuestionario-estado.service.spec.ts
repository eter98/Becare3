import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICuestionarioEstado, CuestionarioEstado } from '../cuestionario-estado.model';

import { CuestionarioEstadoService } from './cuestionario-estado.service';

describe('Service Tests', () => {
  describe('CuestionarioEstado Service', () => {
    let service: CuestionarioEstadoService;
    let httpMock: HttpTestingController;
    let elemDefault: ICuestionarioEstado;
    let expectedResult: ICuestionarioEstado | ICuestionarioEstado[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CuestionarioEstadoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        valor: 0,
        valoracion: 'AAAAAAA',
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

      it('should create a CuestionarioEstado', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CuestionarioEstado()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CuestionarioEstado', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            valor: 1,
            valoracion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CuestionarioEstado', () => {
        const patchObject = Object.assign({}, new CuestionarioEstado());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CuestionarioEstado', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            valor: 1,
            valoracion: 'BBBBBB',
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

      it('should delete a CuestionarioEstado', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCuestionarioEstadoToCollectionIfMissing', () => {
        it('should add a CuestionarioEstado to an empty array', () => {
          const cuestionarioEstado: ICuestionarioEstado = { id: 123 };
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing([], cuestionarioEstado);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cuestionarioEstado);
        });

        it('should not add a CuestionarioEstado to an array that contains it', () => {
          const cuestionarioEstado: ICuestionarioEstado = { id: 123 };
          const cuestionarioEstadoCollection: ICuestionarioEstado[] = [
            {
              ...cuestionarioEstado,
            },
            { id: 456 },
          ];
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing(cuestionarioEstadoCollection, cuestionarioEstado);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CuestionarioEstado to an array that doesn't contain it", () => {
          const cuestionarioEstado: ICuestionarioEstado = { id: 123 };
          const cuestionarioEstadoCollection: ICuestionarioEstado[] = [{ id: 456 }];
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing(cuestionarioEstadoCollection, cuestionarioEstado);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cuestionarioEstado);
        });

        it('should add only unique CuestionarioEstado to an array', () => {
          const cuestionarioEstadoArray: ICuestionarioEstado[] = [{ id: 123 }, { id: 456 }, { id: 72291 }];
          const cuestionarioEstadoCollection: ICuestionarioEstado[] = [{ id: 123 }];
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing(cuestionarioEstadoCollection, ...cuestionarioEstadoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cuestionarioEstado: ICuestionarioEstado = { id: 123 };
          const cuestionarioEstado2: ICuestionarioEstado = { id: 456 };
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing([], cuestionarioEstado, cuestionarioEstado2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cuestionarioEstado);
          expect(expectedResult).toContain(cuestionarioEstado2);
        });

        it('should accept null and undefined values', () => {
          const cuestionarioEstado: ICuestionarioEstado = { id: 123 };
          expectedResult = service.addCuestionarioEstadoToCollectionIfMissing([], null, cuestionarioEstado, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cuestionarioEstado);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
