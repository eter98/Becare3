jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrograma, Programa } from '../programa.model';
import { ProgramaService } from '../service/programa.service';

import { ProgramaRoutingResolveService } from './programa-routing-resolve.service';

describe('Service Tests', () => {
  describe('Programa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProgramaRoutingResolveService;
    let service: ProgramaService;
    let resultPrograma: IPrograma | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProgramaRoutingResolveService);
      service = TestBed.inject(ProgramaService);
      resultPrograma = undefined;
    });

    describe('resolve', () => {
      it('should return IPrograma returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrograma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrograma).toEqual({ id: 123 });
      });

      it('should return new IPrograma if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrograma = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPrograma).toEqual(new Programa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrograma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrograma).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
