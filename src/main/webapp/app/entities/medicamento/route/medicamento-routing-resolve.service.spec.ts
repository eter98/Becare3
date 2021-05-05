jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedicamento, Medicamento } from '../medicamento.model';
import { MedicamentoService } from '../service/medicamento.service';

import { MedicamentoRoutingResolveService } from './medicamento-routing-resolve.service';

describe('Service Tests', () => {
  describe('Medicamento routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MedicamentoRoutingResolveService;
    let service: MedicamentoService;
    let resultMedicamento: IMedicamento | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MedicamentoRoutingResolveService);
      service = TestBed.inject(MedicamentoService);
      resultMedicamento = undefined;
    });

    describe('resolve', () => {
      it('should return IMedicamento returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicamento).toEqual({ id: 123 });
      });

      it('should return new IMedicamento if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamento = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedicamento).toEqual(new Medicamento());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicamento).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
