jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PresionSanguineaService } from '../service/presion-sanguinea.service';
import { IPresionSanguinea, PresionSanguinea } from '../presion-sanguinea.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PresionSanguineaUpdateComponent } from './presion-sanguinea-update.component';

describe('Component Tests', () => {
  describe('PresionSanguinea Management Update Component', () => {
    let comp: PresionSanguineaUpdateComponent;
    let fixture: ComponentFixture<PresionSanguineaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let presionSanguineaService: PresionSanguineaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PresionSanguineaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PresionSanguineaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PresionSanguineaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      presionSanguineaService = TestBed.inject(PresionSanguineaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const presionSanguinea: IPresionSanguinea = { id: 456 };
        const user: IUser = { id: 'Granito withdrawal' };
        presionSanguinea.user = user;

        const userCollection: IUser[] = [{ id: 'functionalities Bicicleta Patatas' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ presionSanguinea });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const presionSanguinea: IPresionSanguinea = { id: 456 };
        const user: IUser = { id: 'paradigms Moroccan' };
        presionSanguinea.user = user;

        activatedRoute.data = of({ presionSanguinea });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(presionSanguinea));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const presionSanguinea = { id: 123 };
        spyOn(presionSanguineaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ presionSanguinea });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: presionSanguinea }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(presionSanguineaService.update).toHaveBeenCalledWith(presionSanguinea);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const presionSanguinea = new PresionSanguinea();
        spyOn(presionSanguineaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ presionSanguinea });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: presionSanguinea }));
        saveSubject.complete();

        // THEN
        expect(presionSanguineaService.create).toHaveBeenCalledWith(presionSanguinea);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const presionSanguinea = { id: 123 };
        spyOn(presionSanguineaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ presionSanguinea });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(presionSanguineaService.update).toHaveBeenCalledWith(presionSanguinea);
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
