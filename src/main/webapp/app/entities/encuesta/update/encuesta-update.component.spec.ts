jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EncuestaService } from '../service/encuesta.service';
import { IEncuesta, Encuesta } from '../encuesta.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EncuestaUpdateComponent } from './encuesta-update.component';

describe('Component Tests', () => {
  describe('Encuesta Management Update Component', () => {
    let comp: EncuestaUpdateComponent;
    let fixture: ComponentFixture<EncuestaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let encuestaService: EncuestaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EncuestaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EncuestaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EncuestaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      encuestaService = TestBed.inject(EncuestaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const encuesta: IEncuesta = { id: 456 };
        const user: IUser = { id: 'uniforme' };
        encuesta.user = user;

        const userCollection: IUser[] = [{ id: 'Acero' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const encuesta: IEncuesta = { id: 456 };
        const user: IUser = { id: 'Som USB Cambridgeshire' };
        encuesta.user = user;

        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(encuesta));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const encuesta = { id: 123 };
        spyOn(encuestaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: encuesta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(encuestaService.update).toHaveBeenCalledWith(encuesta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const encuesta = new Encuesta();
        spyOn(encuestaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: encuesta }));
        saveSubject.complete();

        // THEN
        expect(encuestaService.create).toHaveBeenCalledWith(encuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const encuesta = { id: 123 };
        spyOn(encuestaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(encuestaService.update).toHaveBeenCalledWith(encuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
