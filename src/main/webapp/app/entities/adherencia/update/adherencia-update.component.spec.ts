jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdherenciaService } from '../service/adherencia.service';
import { IAdherencia, Adherencia } from '../adherencia.model';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { AdherenciaUpdateComponent } from './adherencia-update.component';

describe('Component Tests', () => {
  describe('Adherencia Management Update Component', () => {
    let comp: AdherenciaUpdateComponent;
    let fixture: ComponentFixture<AdherenciaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let adherenciaService: AdherenciaService;
    let medicamentoService: MedicamentoService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdherenciaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdherenciaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdherenciaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      adherenciaService = TestBed.inject(AdherenciaService);
      medicamentoService = TestBed.inject(MedicamentoService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Medicamento query and add missing value', () => {
        const adherencia: IAdherencia = { id: 456 };
        const medicamento: IMedicamento = { id: 75631 };
        adherencia.medicamento = medicamento;

        const medicamentoCollection: IMedicamento[] = [{ id: 49387 }];
        spyOn(medicamentoService, 'query').and.returnValue(of(new HttpResponse({ body: medicamentoCollection })));
        const additionalMedicamentos = [medicamento];
        const expectedCollection: IMedicamento[] = [...additionalMedicamentos, ...medicamentoCollection];
        spyOn(medicamentoService, 'addMedicamentoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        expect(medicamentoService.query).toHaveBeenCalled();
        expect(medicamentoService.addMedicamentoToCollectionIfMissing).toHaveBeenCalledWith(
          medicamentoCollection,
          ...additionalMedicamentos
        );
        expect(comp.medicamentosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Paciente query and add missing value', () => {
        const adherencia: IAdherencia = { id: 456 };
        const paciente: IPaciente = { id: 19350 };
        adherencia.paciente = paciente;

        const pacienteCollection: IPaciente[] = [{ id: 40191 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: pacienteCollection })));
        const additionalPacientes = [paciente];
        const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(pacienteCollection, ...additionalPacientes);
        expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const adherencia: IAdherencia = { id: 456 };
        const medicamento: IMedicamento = { id: 21187 };
        adherencia.medicamento = medicamento;
        const paciente: IPaciente = { id: 40173 };
        adherencia.paciente = paciente;

        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(adherencia));
        expect(comp.medicamentosSharedCollection).toContain(medicamento);
        expect(comp.pacientesSharedCollection).toContain(paciente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const adherencia = { id: 123 };
        spyOn(adherenciaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adherencia }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(adherenciaService.update).toHaveBeenCalledWith(adherencia);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const adherencia = new Adherencia();
        spyOn(adherenciaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adherencia }));
        saveSubject.complete();

        // THEN
        expect(adherenciaService.create).toHaveBeenCalledWith(adherencia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const adherencia = { id: 123 };
        spyOn(adherenciaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ adherencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(adherenciaService.update).toHaveBeenCalledWith(adherencia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMedicamentoById', () => {
        it('Should return tracked Medicamento primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicamentoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPacienteById', () => {
        it('Should return tracked Paciente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPacienteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
