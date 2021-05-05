jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIPS, IPS } from '../ips.model';
import { IPSService } from '../service/ips.service';

import { IPSRoutingResolveService } from './ips-routing-resolve.service';

describe('Service Tests', () => {
  describe('IPS routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IPSRoutingResolveService;
    let service: IPSService;
    let resultIPS: IIPS | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IPSRoutingResolveService);
      service = TestBed.inject(IPSService);
      resultIPS = undefined;
    });

    describe('resolve', () => {
      it('should return IIPS returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIPS = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIPS).toEqual({ id: 123 });
      });

      it('should return new IIPS if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIPS = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIPS).toEqual(new IPS());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIPS = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIPS).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
