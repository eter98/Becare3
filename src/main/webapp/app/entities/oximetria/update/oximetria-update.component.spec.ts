jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OximetriaService } from '../service/oximetria.service';
import { IOximetria, Oximetria } from '../oximetria.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { OximetriaUpdateComponent } from './oximetria-update.component';

describe('Component Tests', () => {
  describe('Oximetria Management Update Component', () => {
    let comp: OximetriaUpdateComponent;
    let fixture: ComponentFixture<OximetriaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let oximetriaService: OximetriaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OximetriaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OximetriaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OximetriaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      oximetriaService = TestBed.inject(OximetriaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const oximetria: IOximetria = { id: 456 };
        const user: IUser = { id: 'Navarra' };
        oximetria.user = user;

        const userCollection: IUser[] = [{ id: 'Operativo' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ oximetria });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const oximetria: IOximetria = { id: 456 };
        const user: IUser = { id: 'firmware Producto override' };
        oximetria.user = user;

        activatedRoute.data = of({ oximetria });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(oximetria));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const oximetria = { id: 123 };
        spyOn(oximetriaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ oximetria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: oximetria }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(oximetriaService.update).toHaveBeenCalledWith(oximetria);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const oximetria = new Oximetria();
        spyOn(oximetriaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ oximetria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: oximetria }));
        saveSubject.complete();

        // THEN
        expect(oximetriaService.create).toHaveBeenCalledWith(oximetria);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const oximetria = { id: 123 };
        spyOn(oximetriaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ oximetria });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(oximetriaService.update).toHaveBeenCalledWith(oximetria);
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
