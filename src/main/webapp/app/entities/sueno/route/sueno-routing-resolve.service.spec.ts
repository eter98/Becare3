jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISueno, Sueno } from '../sueno.model';
import { SuenoService } from '../service/sueno.service';

import { SuenoRoutingResolveService } from './sueno-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sueno routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SuenoRoutingResolveService;
    let service: SuenoService;
    let resultSueno: ISueno | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SuenoRoutingResolveService);
      service = TestBed.inject(SuenoService);
      resultSueno = undefined;
    });

    describe('resolve', () => {
      it('should return ISueno returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSueno = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSueno).toEqual({ id: 123 });
      });

      it('should return new ISueno if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSueno = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSueno).toEqual(new Sueno());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSueno = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSueno).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
