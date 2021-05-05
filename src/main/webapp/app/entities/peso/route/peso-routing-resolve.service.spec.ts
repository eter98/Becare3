jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeso, Peso } from '../peso.model';
import { PesoService } from '../service/peso.service';

import { PesoRoutingResolveService } from './peso-routing-resolve.service';

describe('Service Tests', () => {
  describe('Peso routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PesoRoutingResolveService;
    let service: PesoService;
    let resultPeso: IPeso | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PesoRoutingResolveService);
      service = TestBed.inject(PesoService);
      resultPeso = undefined;
    });

    describe('resolve', () => {
      it('should return IPeso returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeso = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeso).toEqual({ id: 123 });
      });

      it('should return new IPeso if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeso = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPeso).toEqual(new Peso());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPeso = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPeso).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
