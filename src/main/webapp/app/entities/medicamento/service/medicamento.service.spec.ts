import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Presentacion } from 'app/entities/enumerations/presentacion.model';
import { IMedicamento, Medicamento } from '../medicamento.model';

import { MedicamentoService } from './medicamento.service';

describe('Service Tests', () => {
  describe('Medicamento Service', () => {
    let service: MedicamentoService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedicamento;
    let expectedResult: IMedicamento | IMedicamento[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MedicamentoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        descripcion: 'AAAAAAA',
        fechaIngreso: currentDate,
        presentacion: Presentacion.Comprimido,
        generico: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Medicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaIngreso: currentDate,
          },
          returnedFromService
        );

        service.create(new Medicamento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Medicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
            presentacion: 'BBBBBB',
            generico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaIngreso: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Medicamento', () => {
        const patchObject = Object.assign(
          {
            descripcion: 'BBBBBB',
          },
          new Medicamento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaIngreso: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Medicamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
            presentacion: 'BBBBBB',
            generico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaIngreso: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Medicamento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMedicamentoToCollectionIfMissing', () => {
        it('should add a Medicamento to an empty array', () => {
          const medicamento: IMedicamento = { id: 123 };
          expectedResult = service.addMedicamentoToCollectionIfMissing([], medicamento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicamento);
        });

        it('should not add a Medicamento to an array that contains it', () => {
          const medicamento: IMedicamento = { id: 123 };
          const medicamentoCollection: IMedicamento[] = [
            {
              ...medicamento,
            },
            { id: 456 },
          ];
          expectedResult = service.addMedicamentoToCollectionIfMissing(medicamentoCollection, medicamento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Medicamento to an array that doesn't contain it", () => {
          const medicamento: IMedicamento = { id: 123 };
          const medicamentoCollection: IMedicamento[] = [{ id: 456 }];
          expectedResult = service.addMedicamentoToCollectionIfMissing(medicamentoCollection, medicamento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicamento);
        });

        it('should add only unique Medicamento to an array', () => {
          const medicamentoArray: IMedicamento[] = [{ id: 123 }, { id: 456 }, { id: 8217 }];
          const medicamentoCollection: IMedicamento[] = [{ id: 123 }];
          expectedResult = service.addMedicamentoToCollectionIfMissing(medicamentoCollection, ...medicamentoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const medicamento: IMedicamento = { id: 123 };
          const medicamento2: IMedicamento = { id: 456 };
          expectedResult = service.addMedicamentoToCollectionIfMissing([], medicamento, medicamento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicamento);
          expect(expectedResult).toContain(medicamento2);
        });

        it('should accept null and undefined values', () => {
          const medicamento: IMedicamento = { id: 123 };
          expectedResult = service.addMedicamentoToCollectionIfMissing([], null, medicamento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicamento);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
