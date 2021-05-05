jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDispositivo, Dispositivo } from '../dispositivo.model';
import { DispositivoService } from '../service/dispositivo.service';

import { DispositivoRoutingResolveService } from './dispositivo-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dispositivo routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DispositivoRoutingResolveService;
    let service: DispositivoService;
    let resultDispositivo: IDispositivo | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DispositivoRoutingResolveService);
      service = TestBed.inject(DispositivoService);
      resultDispositivo = undefined;
    });

    describe('resolve', () => {
      it('should return IDispositivo returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispositivo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDispositivo).toEqual({ id: 123 });
      });

      it('should return new IDispositivo if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispositivo = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDispositivo).toEqual(new Dispositivo());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispositivo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDispositivo).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
