import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITratamientoMedicamento, TratamientoMedicamento } from '../tratamiento-medicamento.model';

import { TratamientoMedicamentoService } from './tratamiento-medicamento.service';

describe('Service Tests', () => {
  describe('TratamientoMedicamento Service', () => {
    let service: TratamientoMedicamentoService;
    let httpMock: HttpTestingController;
    let elemDefault: ITratamientoMedicamento;
    let expectedResult: ITratamientoMedicamento | ITratamientoMedicamento[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TratamientoMedicamentoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        dosis: 'AAAAAAA',
        intensidad: 'AAAAAAA',
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

      it('should create a TratamientoMedicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TratamientoMedicamento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TratamientoMedicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dosis: 'BBBBBB',
            intensidad: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TratamientoMedicamento', () => {
        const patchObject = Object.assign(
          {
            dosis: 'BBBBBB',
            intensidad: 'BBBBBB',
          },
          new TratamientoMedicamento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TratamientoMedicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dosis: 'BBBBBB',
            intensidad: 'BBBBBB',
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

      it('should delete a TratamientoMedicamento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTratamientoMedicamentoToCollectionIfMissing', () => {
        it('should add a TratamientoMedicamento to an empty array', () => {
          const tratamientoMedicamento: ITratamientoMedicamento = { id: 123 };
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing([], tratamientoMedicamento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tratamientoMedicamento);
        });

        it('should not add a TratamientoMedicamento to an array that contains it', () => {
          const tratamientoMedicamento: ITratamientoMedicamento = { id: 123 };
          const tratamientoMedicamentoCollection: ITratamientoMedicamento[] = [
            {
              ...tratamientoMedicamento,
            },
            { id: 456 },
          ];
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing(tratamientoMedicamentoCollection, tratamientoMedicamento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TratamientoMedicamento to an array that doesn't contain it", () => {
          const tratamientoMedicamento: ITratamientoMedicamento = { id: 123 };
          const tratamientoMedicamentoCollection: ITratamientoMedicamento[] = [{ id: 456 }];
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing(tratamientoMedicamentoCollection, tratamientoMedicamento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tratamientoMedicamento);
        });

        it('should add only unique TratamientoMedicamento to an array', () => {
          const tratamientoMedicamentoArray: ITratamientoMedicamento[] = [{ id: 123 }, { id: 456 }, { id: 81148 }];
          const tratamientoMedicamentoCollection: ITratamientoMedicamento[] = [{ id: 123 }];
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing(
            tratamientoMedicamentoCollection,
            ...tratamientoMedicamentoArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tratamientoMedicamento: ITratamientoMedicamento = { id: 123 };
          const tratamientoMedicamento2: ITratamientoMedicamento = { id: 456 };
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing([], tratamientoMedicamento, tratamientoMedicamento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tratamientoMedicamento);
          expect(expectedResult).toContain(tratamientoMedicamento2);
        });

        it('should accept null and undefined values', () => {
          const tratamientoMedicamento: ITratamientoMedicamento = { id: 123 };
          expectedResult = service.addTratamientoMedicamentoToCollectionIfMissing([], null, tratamientoMedicamento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tratamientoMedicamento);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
