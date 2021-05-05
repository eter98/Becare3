jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFrecuenciaCardiaca, FrecuenciaCardiaca } from '../frecuencia-cardiaca.model';
import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';

import { FrecuenciaCardiacaRoutingResolveService } from './frecuencia-cardiaca-routing-resolve.service';

describe('Service Tests', () => {
  describe('FrecuenciaCardiaca routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FrecuenciaCardiacaRoutingResolveService;
    let service: FrecuenciaCardiacaService;
    let resultFrecuenciaCardiaca: IFrecuenciaCardiaca | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FrecuenciaCardiacaRoutingResolveService);
      service = TestBed.inject(FrecuenciaCardiacaService);
      resultFrecuenciaCardiaca = undefined;
    });

    describe('resolve', () => {
      it('should return IFrecuenciaCardiaca returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrecuenciaCardiaca = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFrecuenciaCardiaca).toEqual({ id: 123 });
      });

      it('should return new IFrecuenciaCardiaca if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrecuenciaCardiaca = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFrecuenciaCardiaca).toEqual(new FrecuenciaCardiaca());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrecuenciaCardiaca = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFrecuenciaCardiaca).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
