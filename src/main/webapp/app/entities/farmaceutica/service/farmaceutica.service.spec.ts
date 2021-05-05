import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFarmaceutica, Farmaceutica } from '../farmaceutica.model';

import { FarmaceuticaService } from './farmaceutica.service';

describe('Service Tests', () => {
  describe('Farmaceutica Service', () => {
    let service: FarmaceuticaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFarmaceutica;
    let expectedResult: IFarmaceutica | IFarmaceutica[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FarmaceuticaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        direccion: 'AAAAAAA',
        propietario: 'AAAAAAA',
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

      it('should create a Farmaceutica', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Farmaceutica()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Farmaceutica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            direccion: 'BBBBBB',
            propietario: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Farmaceutica', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            propietario: 'BBBBBB',
          },
          new Farmaceutica()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Farmaceutica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            direccion: 'BBBBBB',
            propietario: 'BBBBBB',
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

      it('should delete a Farmaceutica', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFarmaceuticaToCollectionIfMissing', () => {
        it('should add a Farmaceutica to an empty array', () => {
          const farmaceutica: IFarmaceutica = { id: 123 };
          expectedResult = service.addFarmaceuticaToCollectionIfMissing([], farmaceutica);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(farmaceutica);
        });

        it('should not add a Farmaceutica to an array that contains it', () => {
          const farmaceutica: IFarmaceutica = { id: 123 };
          const farmaceuticaCollection: IFarmaceutica[] = [
            {
              ...farmaceutica,
            },
            { id: 456 },
          ];
          expectedResult = service.addFarmaceuticaToCollectionIfMissing(farmaceuticaCollection, farmaceutica);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Farmaceutica to an array that doesn't contain it", () => {
          const farmaceutica: IFarmaceutica = { id: 123 };
          const farmaceuticaCollection: IFarmaceutica[] = [{ id: 456 }];
          expectedResult = service.addFarmaceuticaToCollectionIfMissing(farmaceuticaCollection, farmaceutica);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(farmaceutica);
        });

        it('should add only unique Farmaceutica to an array', () => {
          const farmaceuticaArray: IFarmaceutica[] = [{ id: 123 }, { id: 456 }, { id: 15800 }];
          const farmaceuticaCollection: IFarmaceutica[] = [{ id: 123 }];
          expectedResult = service.addFarmaceuticaToCollectionIfMissing(farmaceuticaCollection, ...farmaceuticaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const farmaceutica: IFarmaceutica = { id: 123 };
          const farmaceutica2: IFarmaceutica = { id: 456 };
          expectedResult = service.addFarmaceuticaToCollectionIfMissing([], farmaceutica, farmaceutica2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(farmaceutica);
          expect(expectedResult).toContain(farmaceutica2);
        });

        it('should accept null and undefined values', () => {
          const farmaceutica: IFarmaceutica = { id: 123 };
          expectedResult = service.addFarmaceuticaToCollectionIfMissing([], null, farmaceutica, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(farmaceutica);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
