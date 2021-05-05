jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INotificacion, Notificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';

import { NotificacionRoutingResolveService } from './notificacion-routing-resolve.service';

describe('Service Tests', () => {
  describe('Notificacion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NotificacionRoutingResolveService;
    let service: NotificacionService;
    let resultNotificacion: INotificacion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NotificacionRoutingResolveService);
      service = TestBed.inject(NotificacionService);
      resultNotificacion = undefined;
    });

    describe('resolve', () => {
      it('should return INotificacion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotificacion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotificacion).toEqual({ id: 123 });
      });

      it('should return new INotificacion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotificacion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNotificacion).toEqual(new Notificacion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotificacion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotificacion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
