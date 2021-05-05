jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IngestaService } from '../service/ingesta.service';
import { IIngesta, Ingesta } from '../ingesta.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { IngestaUpdateComponent } from './ingesta-update.component';

describe('Component Tests', () => {
  describe('Ingesta Management Update Component', () => {
    let comp: IngestaUpdateComponent;
    let fixture: ComponentFixture<IngestaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ingestaService: IngestaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IngestaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IngestaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IngestaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ingestaService = TestBed.inject(IngestaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const ingesta: IIngesta = { id: 456 };
        const user: IUser = { id: 'Plástico Dong Borders' };
        ingesta.user = user;

        const userCollection: IUser[] = [{ id: 'Hormigon Bicicleta' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ ingesta });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ingesta: IIngesta = { id: 456 };
        const user: IUser = { id: 'deposit Berkshire' };
        ingesta.user = user;

        activatedRoute.data = of({ ingesta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ingesta));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ingesta = { id: 123 };
        spyOn(ingestaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ingesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ingesta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ingestaService.update).toHaveBeenCalledWith(ingesta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ingesta = new Ingesta();
        spyOn(ingestaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ingesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ingesta }));
        saveSubject.complete();

        // THEN
        expect(ingestaService.create).toHaveBeenCalledWith(ingesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ingesta = { id: 123 };
        spyOn(ingestaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ingesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ingestaService.update).toHaveBeenCalledWith(ingesta);
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
