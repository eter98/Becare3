jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITratamientoMedicamento, TratamientoMedicamento } from '../tratamiento-medicamento.model';
import { TratamientoMedicamentoService } from '../service/tratamiento-medicamento.service';

import { TratamientoMedicamentoRoutingResolveService } from './tratamiento-medicamento-routing-resolve.service';

describe('Service Tests', () => {
  describe('TratamientoMedicamento routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TratamientoMedicamentoRoutingResolveService;
    let service: TratamientoMedicamentoService;
    let resultTratamientoMedicamento: ITratamientoMedicamento | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TratamientoMedicamentoRoutingResolveService);
      service = TestBed.inject(TratamientoMedicamentoService);
      resultTratamientoMedicamento = undefined;
    });

    describe('resolve', () => {
      it('should return ITratamientoMedicamento returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTratamientoMedicamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTratamientoMedicamento).toEqual({ id: 123 });
      });

      it('should return new ITratamientoMedicamento if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTratamientoMedicamento = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTratamientoMedicamento).toEqual(new TratamientoMedicamento());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTratamientoMedicamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTratamientoMedicamento).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
