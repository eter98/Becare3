jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TratamientoMedicamentoService } from '../service/tratamiento-medicamento.service';
import { ITratamientoMedicamento, TratamientoMedicamento } from '../tratamiento-medicamento.model';
import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { TratamietoService } from 'app/entities/tratamieto/service/tratamieto.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';

import { TratamientoMedicamentoUpdateComponent } from './tratamiento-medicamento-update.component';

describe('Component Tests', () => {
  describe('TratamientoMedicamento Management Update Component', () => {
    let comp: TratamientoMedicamentoUpdateComponent;
    let fixture: ComponentFixture<TratamientoMedicamentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tratamientoMedicamentoService: TratamientoMedicamentoService;
    let tratamietoService: TratamietoService;
    let medicamentoService: MedicamentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TratamientoMedicamentoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TratamientoMedicamentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TratamientoMedicamentoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tratamientoMedicamentoService = TestBed.inject(TratamientoMedicamentoService);
      tratamietoService = TestBed.inject(TratamietoService);
      medicamentoService = TestBed.inject(MedicamentoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tratamieto query and add missing value', () => {
        const tratamientoMedicamento: ITratamientoMedicamento = { id: 456 };
        const tratamieto: ITratamieto = { id: 9002 };
        tratamientoMedicamento.tratamieto = tratamieto;

        const tratamietoCollection: ITratamieto[] = [{ id: 63549 }];
        spyOn(tratamietoService, 'query').and.returnValue(of(new HttpResponse({ body: tratamietoCollection })));
        const additionalTratamietos = [tratamieto];
        const expectedCollection: ITratamieto[] = [...additionalTratamietos, ...tratamietoCollection];
        spyOn(tratamietoService, 'addTratamietoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        expect(tratamietoService.query).toHaveBeenCalled();
        expect(tratamietoService.addTratamietoToCollectionIfMissing).toHaveBeenCalledWith(tratamietoCollection, ...additionalTratamietos);
        expect(comp.tratamietosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Medicamento query and add missing value', () => {
        const tratamientoMedicamento: ITratamientoMedicamento = { id: 456 };
        const medicamento: IMedicamento = { id: 39133 };
        tratamientoMedicamento.medicamento = medicamento;

        const medicamentoCollection: IMedicamento[] = [{ id: 62648 }];
        spyOn(medicamentoService, 'query').and.returnValue(of(new HttpResponse({ body: medicamentoCollection })));
        const additionalMedicamentos = [medicamento];
        const expectedCollection: IMedicamento[] = [...additionalMedicamentos, ...medicamentoCollection];
        spyOn(medicamentoService, 'addMedicamentoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        expect(medicamentoService.query).toHaveBeenCalled();
        expect(medicamentoService.addMedicamentoToCollectionIfMissing).toHaveBeenCalledWith(
          medicamentoCollection,
          ...additionalMedicamentos
        );
        expect(comp.medicamentosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tratamientoMedicamento: ITratamientoMedicamento = { id: 456 };
        const tratamieto: ITratamieto = { id: 13372 };
        tratamientoMedicamento.tratamieto = tratamieto;
        const medicamento: IMedicamento = { id: 86299 };
        tratamientoMedicamento.medicamento = medicamento;

        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tratamientoMedicamento));
        expect(comp.tratamietosSharedCollection).toContain(tratamieto);
        expect(comp.medicamentosSharedCollection).toContain(medicamento);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamientoMedicamento = { id: 123 };
        spyOn(tratamientoMedicamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tratamientoMedicamento }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tratamientoMedicamentoService.update).toHaveBeenCalledWith(tratamientoMedicamento);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamientoMedicamento = new TratamientoMedicamento();
        spyOn(tratamientoMedicamentoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tratamientoMedicamento }));
        saveSubject.complete();

        // THEN
        expect(tratamientoMedicamentoService.create).toHaveBeenCalledWith(tratamientoMedicamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamientoMedicamento = { id: 123 };
        spyOn(tratamientoMedicamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamientoMedicamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tratamientoMedicamentoService.update).toHaveBeenCalledWith(tratamientoMedicamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTratamietoById', () => {
        it('Should return tracked Tratamieto primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTratamietoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
