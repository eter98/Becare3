jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFisiometria1, Fisiometria1 } from '../fisiometria-1.model';
import { Fisiometria1Service } from '../service/fisiometria-1.service';

import { Fisiometria1RoutingResolveService } from './fisiometria-1-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fisiometria1 routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: Fisiometria1RoutingResolveService;
    let service: Fisiometria1Service;
    let resultFisiometria1: IFisiometria1 | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(Fisiometria1RoutingResolveService);
      service = TestBed.inject(Fisiometria1Service);
      resultFisiometria1 = undefined;
    });

    describe('resolve', () => {
      it('should return IFisiometria1 returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFisiometria1 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFisiometria1).toEqual({ id: 123 });
      });

      it('should return new IFisiometria1 if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFisiometria1 = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFisiometria1).toEqual(new Fisiometria1());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFisiometria1 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFisiometria1).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
