jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CaloriaService } from '../service/caloria.service';
import { ICaloria, Caloria } from '../caloria.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CaloriaUpdateComponent } from './caloria-update.component';

describe('Component Tests', () => {
  describe('Caloria Management Update Component', () => {
    let comp: CaloriaUpdateComponent;
    let fixture: ComponentFixture<CaloriaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let caloriaService: CaloriaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CaloriaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CaloriaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CaloriaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      caloriaService = TestBed.inject(CaloriaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const caloria: ICaloria = { id: 456 };
        const user: IUser = { id: 'Mesa Camiseta' };
        caloria.user = user;

        const userCollection: IUser[] = [{ id: 'Galicia Facilitador Rwanda' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ caloria });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const caloria: ICaloria = { id: 456 };
        const user: IUser = { id: 'Madera' };
        caloria.user = user;

        activatedRoute.data = of({ caloria });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(caloria));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const caloria = { id: 123 };
        spyOn(caloriaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ caloria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: caloria }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(caloriaService.update).toHaveBeenCalledWith(caloria);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const caloria = new Caloria();
        spyOn(caloriaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ caloria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: caloria }));
        saveSubject.complete();

        // THEN
        expect(caloriaService.create).toHaveBeenCalledWith(caloria);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const caloria = { id: 123 };
        spyOn(caloriaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ caloria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(caloriaService.update).toHaveBeenCalledWith(caloria);
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
