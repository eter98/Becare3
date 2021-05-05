import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIPS, IPS } from '../ips.model';

import { IPSService } from './ips.service';

describe('Service Tests', () => {
  describe('IPS Service', () => {
    let service: IPSService;
    let httpMock: HttpTestingController;
    let elemDefault: IIPS;
    let expectedResult: IIPS | IIPS[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IPSService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        nit: 'AAAAAAA',
        direccion: 'AAAAAAA',
        telefono: 'AAAAAAA',
        correoElectronico: 'AAAAAAA',
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

      it('should create a IPS', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new IPS()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IPS', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            nit: 'BBBBBB',
            direccion: 'BBBBBB',
            telefono: 'BBBBBB',
            correoElectronico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a IPS', () => {
        const patchObject = Object.assign(
          {
            nit: 'BBBBBB',
          },
          new IPS()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of IPS', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            nit: 'BBBBBB',
            direccion: 'BBBBBB',
            telefono: 'BBBBBB',
            correoElectronico: 'BBBBBB',
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

      it('should delete a IPS', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIPSToCollectionIfMissing', () => {
        it('should add a IPS to an empty array', () => {
          const iPS: IIPS = { id: 123 };
          expectedResult = service.addIPSToCollectionIfMissing([], iPS);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(iPS);
        });

        it('should not add a IPS to an array that contains it', () => {
          const iPS: IIPS = { id: 123 };
          const iPSCollection: IIPS[] = [
            {
              ...iPS,
            },
            { id: 456 },
          ];
          expectedResult = service.addIPSToCollectionIfMissing(iPSCollection, iPS);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IPS to an array that doesn't contain it", () => {
          const iPS: IIPS = { id: 123 };
          const iPSCollection: IIPS[] = [{ id: 456 }];
          expectedResult = service.addIPSToCollectionIfMissing(iPSCollection, iPS);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(iPS);
        });

        it('should add only unique IPS to an array', () => {
          const iPSArray: IIPS[] = [{ id: 123 }, { id: 456 }, { id: 83091 }];
          const iPSCollection: IIPS[] = [{ id: 123 }];
          expectedResult = service.addIPSToCollectionIfMissing(iPSCollection, ...iPSArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const iPS: IIPS = { id: 123 };
          const iPS2: IIPS = { id: 456 };
          expectedResult = service.addIPSToCollectionIfMissing([], iPS, iPS2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(iPS);
          expect(expectedResult).toContain(iPS2);
        });

        it('should accept null and undefined values', () => {
          const iPS: IIPS = { id: 123 };
          expectedResult = service.addIPSToCollectionIfMissing([], null, iPS, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(iPS);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
