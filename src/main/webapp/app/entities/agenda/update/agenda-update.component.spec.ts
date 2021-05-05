jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AgendaService } from '../service/agenda.service';
import { IAgenda, Agenda } from '../agenda.model';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';

import { AgendaUpdateComponent } from './agenda-update.component';

describe('Component Tests', () => {
  describe('Agenda Management Update Component', () => {
    let comp: AgendaUpdateComponent;
    let fixture: ComponentFixture<AgendaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let agendaService: AgendaService;
    let medicamentoService: MedicamentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AgendaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AgendaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AgendaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      agendaService = TestBed.inject(AgendaService);
      medicamentoService = TestBed.inject(MedicamentoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Medicamento query and add missing value', () => {
        const agenda: IAgenda = { id: 456 };
        const medicamento: IMedicamento = { id: 55364 };
        agenda.medicamento = medicamento;

        const medicamentoCollection: IMedicamento[] = [{ id: 66829 }];
        spyOn(medicamentoService, 'query').and.returnValue(of(new HttpResponse({ body: medicamentoCollection })));
        const additionalMedicamentos = [medicamento];
        const expectedCollection: IMedicamento[] = [...additionalMedicamentos, ...medicamentoCollection];
        spyOn(medicamentoService, 'addMedicamentoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ agenda });
        comp.ngOnInit();

        expect(medicamentoService.query).toHaveBeenCalled();
        expect(medicamentoService.addMedicamentoToCollectionIfMissing).toHaveBeenCalledWith(
          medicamentoCollection,
          ...additionalMedicamentos
        );
        expect(comp.medicamentosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const agenda: IAgenda = { id: 456 };
        const medicamento: IMedicamento = { id: 37150 };
        agenda.medicamento = medicamento;

        activatedRoute.data = of({ agenda });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(agenda));
        expect(comp.medicamentosSharedCollection).toContain(medicamento);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agenda = { id: 123 };
        spyOn(agendaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agenda });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: agenda }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(agendaService.update).toHaveBeenCalledWith(agenda);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agenda = new Agenda();
        spyOn(agendaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agenda });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: agenda }));
        saveSubject.complete();

        // THEN
        expect(agendaService.create).toHaveBeenCalledWith(agenda);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agenda = { id: 123 };
        spyOn(agendaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agenda });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(agendaService.update).toHaveBeenCalledWith(agenda);
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
    });
  });
});
