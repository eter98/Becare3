jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAlarma, Alarma } from '../alarma.model';
import { AlarmaService } from '../service/alarma.service';

import { AlarmaRoutingResolveService } from './alarma-routing-resolve.service';

describe('Service Tests', () => {
  describe('Alarma routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AlarmaRoutingResolveService;
    let service: AlarmaService;
    let resultAlarma: IAlarma | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AlarmaRoutingResolveService);
      service = TestBed.inject(AlarmaService);
      resultAlarma = undefined;
    });

    describe('resolve', () => {
      it('should return IAlarma returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlarma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAlarma).toEqual({ id: 123 });
      });

      it('should return new IAlarma if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlarma = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAlarma).toEqual(new Alarma());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlarma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAlarma).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
