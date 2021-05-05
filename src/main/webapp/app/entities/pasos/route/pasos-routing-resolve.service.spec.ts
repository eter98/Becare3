jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPasos, Pasos } from '../pasos.model';
import { PasosService } from '../service/pasos.service';

import { PasosRoutingResolveService } from './pasos-routing-resolve.service';

describe('Service Tests', () => {
  describe('Pasos routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PasosRoutingResolveService;
    let service: PasosService;
    let resultPasos: IPasos | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PasosRoutingResolveService);
      service = TestBed.inject(PasosService);
      resultPasos = undefined;
    });

    describe('resolve', () => {
      it('should return IPasos returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPasos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPasos).toEqual({ id: 123 });
      });

      it('should return new IPasos if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPasos = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPasos).toEqual(new Pasos());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPasos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPasos).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
