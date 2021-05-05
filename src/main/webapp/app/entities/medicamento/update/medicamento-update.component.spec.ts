jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MedicamentoService } from '../service/medicamento.service';
import { IMedicamento, Medicamento } from '../medicamento.model';

import { MedicamentoUpdateComponent } from './medicamento-update.component';

describe('Component Tests', () => {
  describe('Medicamento Management Update Component', () => {
    let comp: MedicamentoUpdateComponent;
    let fixture: ComponentFixture<MedicamentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let medicamentoService: MedicamentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicamentoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MedicamentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicamentoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      medicamentoService = TestBed.inject(MedicamentoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const medicamento: IMedicamento = { id: 456 };

        activatedRoute.data = of({ medicamento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(medicamento));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamento = { id: 123 };
        spyOn(medicamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicamento }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(medicamentoService.update).toHaveBeenCalledWith(medicamento);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamento = new Medicamento();
        spyOn(medicamentoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicamento }));
        saveSubject.complete();

        // THEN
        expect(medicamentoService.create).toHaveBeenCalledWith(medicamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamento = { id: 123 };
        spyOn(medicamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(medicamentoService.update).toHaveBeenCalledWith(medicamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
