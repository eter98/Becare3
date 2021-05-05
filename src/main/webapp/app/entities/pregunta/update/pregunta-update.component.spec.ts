jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PreguntaService } from '../service/pregunta.service';
import { IPregunta, Pregunta } from '../pregunta.model';
import { ICondicion } from 'app/entities/condicion/condicion.model';
import { CondicionService } from 'app/entities/condicion/service/condicion.service';

import { PreguntaUpdateComponent } from './pregunta-update.component';

describe('Component Tests', () => {
  describe('Pregunta Management Update Component', () => {
    let comp: PreguntaUpdateComponent;
    let fixture: ComponentFixture<PreguntaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let preguntaService: PreguntaService;
    let condicionService: CondicionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PreguntaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PreguntaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PreguntaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      preguntaService = TestBed.inject(PreguntaService);
      condicionService = TestBed.inject(CondicionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Condicion query and add missing value', () => {
        const pregunta: IPregunta = { id: 456 };
        const condicion: ICondicion = { id: 92903 };
        pregunta.condicion = condicion;

        const condicionCollection: ICondicion[] = [{ id: 74293 }];
        spyOn(condicionService, 'query').and.returnValue(of(new HttpResponse({ body: condicionCollection })));
        const additionalCondicions = [condicion];
        const expectedCollection: ICondicion[] = [...additionalCondicions, ...condicionCollection];
        spyOn(condicionService, 'addCondicionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pregunta });
        comp.ngOnInit();

        expect(condicionService.query).toHaveBeenCalled();
        expect(condicionService.addCondicionToCollectionIfMissing).toHaveBeenCalledWith(condicionCollection, ...additionalCondicions);
        expect(comp.condicionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pregunta: IPregunta = { id: 456 };
        const condicion: ICondicion = { id: 84934 };
        pregunta.condicion = condicion;

        activatedRoute.data = of({ pregunta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pregunta));
        expect(comp.condicionsSharedCollection).toContain(condicion);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pregunta = { id: 123 };
        spyOn(preguntaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pregunta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pregunta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(preguntaService.update).toHaveBeenCalledWith(pregunta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pregunta = new Pregunta();
        spyOn(preguntaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pregunta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pregunta }));
        saveSubject.complete();

        // THEN
        expect(preguntaService.create).toHaveBeenCalledWith(pregunta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pregunta = { id: 123 };
        spyOn(preguntaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pregunta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(preguntaService.update).toHaveBeenCalledWith(pregunta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCondicionById', () => {
        it('Should return tracked Condicion primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCondicionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
