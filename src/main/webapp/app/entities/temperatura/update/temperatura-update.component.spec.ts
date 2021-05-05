jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TemperaturaService } from '../service/temperatura.service';
import { ITemperatura, Temperatura } from '../temperatura.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TemperaturaUpdateComponent } from './temperatura-update.component';

describe('Component Tests', () => {
  describe('Temperatura Management Update Component', () => {
    let comp: TemperaturaUpdateComponent;
    let fixture: ComponentFixture<TemperaturaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let temperaturaService: TemperaturaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TemperaturaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TemperaturaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemperaturaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      temperaturaService = TestBed.inject(TemperaturaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const temperatura: ITemperatura = { id: 456 };
        const user: IUser = { id: 'driver' };
        temperatura.user = user;

        const userCollection: IUser[] = [{ id: 'cliente' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ temperatura });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const temperatura: ITemperatura = { id: 456 };
        const user: IUser = { id: 'robust' };
        temperatura.user = user;

        activatedRoute.data = of({ temperatura });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(temperatura));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const temperatura = { id: 123 };
        spyOn(temperaturaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ temperatura });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: temperatura }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(temperaturaService.update).toHaveBeenCalledWith(temperatura);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const temperatura = new Temperatura();
        spyOn(temperaturaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ temperatura });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: temperatura }));
        saveSubject.complete();

        // THEN
        expect(temperaturaService.create).toHaveBeenCalledWith(temperatura);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const temperatura = { id: 123 };
        spyOn(temperaturaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ temperatura });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(temperaturaService.update).toHaveBeenCalledWith(temperatura);
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
