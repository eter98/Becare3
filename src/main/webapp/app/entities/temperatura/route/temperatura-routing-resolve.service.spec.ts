jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITemperatura, Temperatura } from '../temperatura.model';
import { TemperaturaService } from '../service/temperatura.service';

import { TemperaturaRoutingResolveService } from './temperatura-routing-resolve.service';

describe('Service Tests', () => {
  describe('Temperatura routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TemperaturaRoutingResolveService;
    let service: TemperaturaService;
    let resultTemperatura: ITemperatura | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TemperaturaRoutingResolveService);
      service = TestBed.inject(TemperaturaService);
      resultTemperatura = undefined;
    });

    describe('resolve', () => {
      it('should return ITemperatura returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTemperatura = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTemperatura).toEqual({ id: 123 });
      });

      it('should return new ITemperatura if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTemperatura = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTemperatura).toEqual(new Temperatura());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTemperatura = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTemperatura).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
