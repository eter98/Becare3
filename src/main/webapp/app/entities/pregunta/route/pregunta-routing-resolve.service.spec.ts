jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPregunta, Pregunta } from '../pregunta.model';
import { PreguntaService } from '../service/pregunta.service';

import { PreguntaRoutingResolveService } from './pregunta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Pregunta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PreguntaRoutingResolveService;
    let service: PreguntaService;
    let resultPregunta: IPregunta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PreguntaRoutingResolveService);
      service = TestBed.inject(PreguntaService);
      resultPregunta = undefined;
    });

    describe('resolve', () => {
      it('should return IPregunta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPregunta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPregunta).toEqual({ id: 123 });
      });

      it('should return new IPregunta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPregunta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPregunta).toEqual(new Pregunta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPregunta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPregunta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
