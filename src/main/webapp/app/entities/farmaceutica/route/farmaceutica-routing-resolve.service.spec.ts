jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFarmaceutica, Farmaceutica } from '../farmaceutica.model';
import { FarmaceuticaService } from '../service/farmaceutica.service';

import { FarmaceuticaRoutingResolveService } from './farmaceutica-routing-resolve.service';

describe('Service Tests', () => {
  describe('Farmaceutica routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FarmaceuticaRoutingResolveService;
    let service: FarmaceuticaService;
    let resultFarmaceutica: IFarmaceutica | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FarmaceuticaRoutingResolveService);
      service = TestBed.inject(FarmaceuticaService);
      resultFarmaceutica = undefined;
    });

    describe('resolve', () => {
      it('should return IFarmaceutica returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFarmaceutica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFarmaceutica).toEqual({ id: 123 });
      });

      it('should return new IFarmaceutica if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFarmaceutica = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFarmaceutica).toEqual(new Farmaceutica());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFarmaceutica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFarmaceutica).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
