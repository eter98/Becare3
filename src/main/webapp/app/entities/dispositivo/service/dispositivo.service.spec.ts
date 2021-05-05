import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDispositivo, Dispositivo } from '../dispositivo.model';

import { DispositivoService } from './dispositivo.service';

describe('Service Tests', () => {
  describe('Dispositivo Service', () => {
    let service: DispositivoService;
    let httpMock: HttpTestingController;
    let elemDefault: IDispositivo;
    let expectedResult: IDispositivo | IDispositivo[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DispositivoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        dispositivo: 'AAAAAAA',
        mac: 'AAAAAAA',
        conectado: false,
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

      it('should create a Dispositivo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dispositivo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dispositivo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dispositivo: 'BBBBBB',
            mac: 'BBBBBB',
            conectado: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Dispositivo', () => {
        const patchObject = Object.assign(
          {
            conectado: true,
          },
          new Dispositivo()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dispositivo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dispositivo: 'BBBBBB',
            mac: 'BBBBBB',
            conectado: true,
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

      it('should delete a Dispositivo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDispositivoToCollectionIfMissing', () => {
        it('should add a Dispositivo to an empty array', () => {
          const dispositivo: IDispositivo = { id: 123 };
          expectedResult = service.addDispositivoToCollectionIfMissing([], dispositivo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dispositivo);
        });

        it('should not add a Dispositivo to an array that contains it', () => {
          const dispositivo: IDispositivo = { id: 123 };
          const dispositivoCollection: IDispositivo[] = [
            {
              ...dispositivo,
            },
            { id: 456 },
          ];
          expectedResult = service.addDispositivoToCollectionIfMissing(dispositivoCollection, dispositivo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Dispositivo to an array that doesn't contain it", () => {
          const dispositivo: IDispositivo = { id: 123 };
          const dispositivoCollection: IDispositivo[] = [{ id: 456 }];
          expectedResult = service.addDispositivoToCollectionIfMissing(dispositivoCollection, dispositivo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dispositivo);
        });

        it('should add only unique Dispositivo to an array', () => {
          const dispositivoArray: IDispositivo[] = [{ id: 123 }, { id: 456 }, { id: 25028 }];
          const dispositivoCollection: IDispositivo[] = [{ id: 123 }];
          expectedResult = service.addDispositivoToCollectionIfMissing(dispositivoCollection, ...dispositivoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dispositivo: IDispositivo = { id: 123 };
          const dispositivo2: IDispositivo = { id: 456 };
          expectedResult = service.addDispositivoToCollectionIfMissing([], dispositivo, dispositivo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dispositivo);
          expect(expectedResult).toContain(dispositivo2);
        });

        it('should accept null and undefined values', () => {
          const dispositivo: IDispositivo = { id: 123 };
          expectedResult = service.addDispositivoToCollectionIfMissing([], null, dispositivo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dispositivo);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
