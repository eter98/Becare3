jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOximetria, Oximetria } from '../oximetria.model';
import { OximetriaService } from '../service/oximetria.service';

import { OximetriaRoutingResolveService } from './oximetria-routing-resolve.service';

describe('Service Tests', () => {
  describe('Oximetria routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OximetriaRoutingResolveService;
    let service: OximetriaService;
    let resultOximetria: IOximetria | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OximetriaRoutingResolveService);
      service = TestBed.inject(OximetriaService);
      resultOximetria = undefined;
    });

    describe('resolve', () => {
      it('should return IOximetria returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOximetria = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOximetria).toEqual({ id: 123 });
      });

      it('should return new IOximetria if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOximetria = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOximetria).toEqual(new Oximetria());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOximetria = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOximetria).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
