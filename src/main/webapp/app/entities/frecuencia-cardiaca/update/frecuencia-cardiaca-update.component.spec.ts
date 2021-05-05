jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';
import { IFrecuenciaCardiaca, FrecuenciaCardiaca } from '../frecuencia-cardiaca.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FrecuenciaCardiacaUpdateComponent } from './frecuencia-cardiaca-update.component';

describe('Component Tests', () => {
  describe('FrecuenciaCardiaca Management Update Component', () => {
    let comp: FrecuenciaCardiacaUpdateComponent;
    let fixture: ComponentFixture<FrecuenciaCardiacaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let frecuenciaCardiacaService: FrecuenciaCardiacaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FrecuenciaCardiacaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FrecuenciaCardiacaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FrecuenciaCardiacaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      frecuenciaCardiacaService = TestBed.inject(FrecuenciaCardiacaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 456 };
        const user: IUser = { id: 'invoice engage' };
        frecuenciaCardiaca.user = user;

        const userCollection: IUser[] = [{ id: 'Orientado Azul platforms' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ frecuenciaCardiaca });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const frecuenciaCardiaca: IFrecuenciaCardiaca = { id: 456 };
        const user: IUser = { id: 'Dalasi programming' };
        frecuenciaCardiaca.user = user;

        activatedRoute.data = of({ frecuenciaCardiaca });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(frecuenciaCardiaca));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frecuenciaCardiaca = { id: 123 };
        spyOn(frecuenciaCardiacaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frecuenciaCardiaca });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frecuenciaCardiaca }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(frecuenciaCardiacaService.update).toHaveBeenCalledWith(frecuenciaCardiaca);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frecuenciaCardiaca = new FrecuenciaCardiaca();
        spyOn(frecuenciaCardiacaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frecuenciaCardiaca });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frecuenciaCardiaca }));
        saveSubject.complete();

        // THEN
        expect(frecuenciaCardiacaService.create).toHaveBeenCalledWith(frecuenciaCardiaca);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frecuenciaCardiaca = { id: 123 };
        spyOn(frecuenciaCardiacaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frecuenciaCardiaca });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(frecuenciaCardiacaService.update).toHaveBeenCalledWith(frecuenciaCardiaca);
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
