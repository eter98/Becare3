jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CuestionarioEstadoService } from '../service/cuestionario-estado.service';
import { ICuestionarioEstado, CuestionarioEstado } from '../cuestionario-estado.model';
import { IPregunta } from 'app/entities/pregunta/pregunta.model';
import { PreguntaService } from 'app/entities/pregunta/service/pregunta.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CuestionarioEstadoUpdateComponent } from './cuestionario-estado-update.component';

describe('Component Tests', () => {
  describe('CuestionarioEstado Management Update Component', () => {
    let comp: CuestionarioEstadoUpdateComponent;
    let fixture: ComponentFixture<CuestionarioEstadoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cuestionarioEstadoService: CuestionarioEstadoService;
    let preguntaService: PreguntaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CuestionarioEstadoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CuestionarioEstadoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CuestionarioEstadoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cuestionarioEstadoService = TestBed.inject(CuestionarioEstadoService);
      preguntaService = TestBed.inject(PreguntaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pregunta query and add missing value', () => {
        const cuestionarioEstado: ICuestionarioEstado = { id: 456 };
        const pregunta: IPregunta = { id: 20331 };
        cuestionarioEstado.pregunta = pregunta;

        const preguntaCollection: IPregunta[] = [{ id: 8427 }];
        spyOn(preguntaService, 'query').and.returnValue(of(new HttpResponse({ body: preguntaCollection })));
        const additionalPreguntas = [pregunta];
        const expectedCollection: IPregunta[] = [...additionalPreguntas, ...preguntaCollection];
        spyOn(preguntaService, 'addPreguntaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        expect(preguntaService.query).toHaveBeenCalled();
        expect(preguntaService.addPreguntaToCollectionIfMissing).toHaveBeenCalledWith(preguntaCollection, ...additionalPreguntas);
        expect(comp.preguntasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const cuestionarioEstado: ICuestionarioEstado = { id: 456 };
        const user: IUser = { id: 'Shilling Egipto Cliente' };
        cuestionarioEstado.user = user;

        const userCollection: IUser[] = [{ id: 'alarm' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cuestionarioEstado: ICuestionarioEstado = { id: 456 };
        const pregunta: IPregunta = { id: 79359 };
        cuestionarioEstado.pregunta = pregunta;
        const user: IUser = { id: 'input Huerta generate' };
        cuestionarioEstado.user = user;

        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cuestionarioEstado));
        expect(comp.preguntasSharedCollection).toContain(pregunta);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cuestionarioEstado = { id: 123 };
        spyOn(cuestionarioEstadoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cuestionarioEstado }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cuestionarioEstadoService.update).toHaveBeenCalledWith(cuestionarioEstado);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cuestionarioEstado = new CuestionarioEstado();
        spyOn(cuestionarioEstadoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cuestionarioEstado }));
        saveSubject.complete();

        // THEN
        expect(cuestionarioEstadoService.create).toHaveBeenCalledWith(cuestionarioEstado);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cuestionarioEstado = { id: 123 };
        spyOn(cuestionarioEstadoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuestionarioEstado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cuestionarioEstadoService.update).toHaveBeenCalledWith(cuestionarioEstado);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPreguntaById', () => {
        it('Should return tracked Pregunta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPreguntaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
