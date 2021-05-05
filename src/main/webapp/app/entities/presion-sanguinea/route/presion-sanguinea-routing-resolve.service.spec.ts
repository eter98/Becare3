jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPresionSanguinea, PresionSanguinea } from '../presion-sanguinea.model';
import { PresionSanguineaService } from '../service/presion-sanguinea.service';

import { PresionSanguineaRoutingResolveService } from './presion-sanguinea-routing-resolve.service';

describe('Service Tests', () => {
  describe('PresionSanguinea routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PresionSanguineaRoutingResolveService;
    let service: PresionSanguineaService;
    let resultPresionSanguinea: IPresionSanguinea | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PresionSanguineaRoutingResolveService);
      service = TestBed.inject(PresionSanguineaService);
      resultPresionSanguinea = undefined;
    });

    describe('resolve', () => {
      it('should return IPresionSanguinea returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPresionSanguinea = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPresionSanguinea).toEqual({ id: 123 });
      });

      it('should return new IPresionSanguinea if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPresionSanguinea = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPresionSanguinea).toEqual(new PresionSanguinea());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPresionSanguinea = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPresionSanguinea).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
