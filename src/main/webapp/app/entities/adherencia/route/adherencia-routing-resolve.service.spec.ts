jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdherencia, Adherencia } from '../adherencia.model';
import { AdherenciaService } from '../service/adherencia.service';

import { AdherenciaRoutingResolveService } from './adherencia-routing-resolve.service';

describe('Service Tests', () => {
  describe('Adherencia routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdherenciaRoutingResolveService;
    let service: AdherenciaService;
    let resultAdherencia: IAdherencia | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdherenciaRoutingResolveService);
      service = TestBed.inject(AdherenciaService);
      resultAdherencia = undefined;
    });

    describe('resolve', () => {
      it('should return IAdherencia returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdherencia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdherencia).toEqual({ id: 123 });
      });

      it('should return new IAdherencia if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdherencia = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdherencia).toEqual(new Adherencia());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdherencia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdherencia).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
