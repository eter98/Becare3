import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPregunta, Pregunta } from '../pregunta.model';

import { PreguntaService } from './pregunta.service';

describe('Service Tests', () => {
  describe('Pregunta Service', () => {
    let service: PreguntaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPregunta;
    let expectedResult: IPregunta | IPregunta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PreguntaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pregunta: 'AAAAAAA',
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

      it('should create a Pregunta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Pregunta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Pregunta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pregunta: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Pregunta', () => {
        const patchObject = Object.assign({}, new Pregunta());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Pregunta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pregunta: 'BBBBBB',
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

      it('should delete a Pregunta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPreguntaToCollectionIfMissing', () => {
        it('should add a Pregunta to an empty array', () => {
          const pregunta: IPregunta = { id: 123 };
          expectedResult = service.addPreguntaToCollectionIfMissing([], pregunta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pregunta);
        });

        it('should not add a Pregunta to an array that contains it', () => {
          const pregunta: IPregunta = { id: 123 };
          const preguntaCollection: IPregunta[] = [
            {
              ...pregunta,
            },
            { id: 456 },
          ];
          expectedResult = service.addPreguntaToCollectionIfMissing(preguntaCollection, pregunta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Pregunta to an array that doesn't contain it", () => {
          const pregunta: IPregunta = { id: 123 };
          const preguntaCollection: IPregunta[] = [{ id: 456 }];
          expectedResult = service.addPreguntaToCollectionIfMissing(preguntaCollection, pregunta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pregunta);
        });

        it('should add only unique Pregunta to an array', () => {
          const preguntaArray: IPregunta[] = [{ id: 123 }, { id: 456 }, { id: 78243 }];
          const preguntaCollection: IPregunta[] = [{ id: 123 }];
          expectedResult = service.addPreguntaToCollectionIfMissing(preguntaCollection, ...preguntaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pregunta: IPregunta = { id: 123 };
          const pregunta2: IPregunta = { id: 456 };
          expectedResult = service.addPreguntaToCollectionIfMissing([], pregunta, pregunta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pregunta);
          expect(expectedResult).toContain(pregunta2);
        });

        it('should accept null and undefined values', () => {
          const pregunta: IPregunta = { id: 123 };
          expectedResult = service.addPreguntaToCollectionIfMissing([], null, pregunta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pregunta);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
