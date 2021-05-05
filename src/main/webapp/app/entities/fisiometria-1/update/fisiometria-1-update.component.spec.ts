jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { Fisiometria1Service } from '../service/fisiometria-1.service';
import { IFisiometria1, Fisiometria1 } from '../fisiometria-1.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { Fisiometria1UpdateComponent } from './fisiometria-1-update.component';

describe('Component Tests', () => {
  describe('Fisiometria1 Management Update Component', () => {
    let comp: Fisiometria1UpdateComponent;
    let fixture: ComponentFixture<Fisiometria1UpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fisiometria1Service: Fisiometria1Service;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Fisiometria1UpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(Fisiometria1UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Fisiometria1UpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fisiometria1Service = TestBed.inject(Fisiometria1Service);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const fisiometria1: IFisiometria1 = { id: 456 };
        const user: IUser = { id: 'Italia precios' };
        fisiometria1.user = user;

        const userCollection: IUser[] = [{ id: 'feed soporte' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ fisiometria1 });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fisiometria1: IFisiometria1 = { id: 456 };
        const user: IUser = { id: 'e-business Artesanal Humano' };
        fisiometria1.user = user;

        activatedRoute.data = of({ fisiometria1 });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fisiometria1));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fisiometria1 = { id: 123 };
        spyOn(fisiometria1Service, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fisiometria1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fisiometria1 }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fisiometria1Service.update).toHaveBeenCalledWith(fisiometria1);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fisiometria1 = new Fisiometria1();
        spyOn(fisiometria1Service, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fisiometria1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fisiometria1 }));
        saveSubject.complete();

        // THEN
        expect(fisiometria1Service.create).toHaveBeenCalledWith(fisiometria1);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fisiometria1 = { id: 123 };
        spyOn(fisiometria1Service, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fisiometria1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fisiometria1Service.update).toHaveBeenCalledWith(fisiometria1);
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
