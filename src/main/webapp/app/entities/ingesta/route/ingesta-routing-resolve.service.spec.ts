jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIngesta, Ingesta } from '../ingesta.model';
import { IngestaService } from '../service/ingesta.service';

import { IngestaRoutingResolveService } from './ingesta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Ingesta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IngestaRoutingResolveService;
    let service: IngestaService;
    let resultIngesta: IIngesta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IngestaRoutingResolveService);
      service = TestBed.inject(IngestaService);
      resultIngesta = undefined;
    });

    describe('resolve', () => {
      it('should return IIngesta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIngesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIngesta).toEqual({ id: 123 });
      });

      it('should return new IIngesta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIngesta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIngesta).toEqual(new Ingesta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIngesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIngesta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
