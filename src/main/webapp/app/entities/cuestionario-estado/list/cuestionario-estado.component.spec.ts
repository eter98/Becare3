import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CuestionarioEstadoService } from '../service/cuestionario-estado.service';

import { CuestionarioEstadoComponent } from './cuestionario-estado.component';

describe('Component Tests', () => {
  describe('CuestionarioEstado Management Component', () => {
    let comp: CuestionarioEstadoComponent;
    let fixture: ComponentFixture<CuestionarioEstadoComponent>;
    let service: CuestionarioEstadoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CuestionarioEstadoComponent],
      })
        .overrideTemplate(CuestionarioEstadoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CuestionarioEstadoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CuestionarioEstadoService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cuestionarioEstados[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cuestionarioEstados[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,asc'] }));
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['name,asc', 'id'] }));
    });

    it('should re-initialize the page', () => {
      // WHEN
      comp.loadPage(1);
      comp.reset();

      // THEN
      expect(comp.page).toEqual(0);
      expect(service.query).toHaveBeenCalledTimes(2);
      expect(comp.cuestionarioEstados[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
