jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICaloria, Caloria } from '../caloria.model';
import { CaloriaService } from '../service/caloria.service';

import { CaloriaRoutingResolveService } from './caloria-routing-resolve.service';

describe('Service Tests', () => {
  describe('Caloria routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CaloriaRoutingResolveService;
    let service: CaloriaService;
    let resultCaloria: ICaloria | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CaloriaRoutingResolveService);
      service = TestBed.inject(CaloriaService);
      resultCaloria = undefined;
    });

    describe('resolve', () => {
      it('should return ICaloria returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCaloria = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCaloria).toEqual({ id: 123 });
      });

      it('should return new ICaloria if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCaloria = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCaloria).toEqual(new Caloria());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCaloria = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCaloria).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
