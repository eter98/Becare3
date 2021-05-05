jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DispositivoService } from '../service/dispositivo.service';
import { IDispositivo, Dispositivo } from '../dispositivo.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { DispositivoUpdateComponent } from './dispositivo-update.component';

describe('Component Tests', () => {
  describe('Dispositivo Management Update Component', () => {
    let comp: DispositivoUpdateComponent;
    let fixture: ComponentFixture<DispositivoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dispositivoService: DispositivoService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DispositivoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DispositivoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DispositivoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dispositivoService = TestBed.inject(DispositivoService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const dispositivo: IDispositivo = { id: 456 };
        const user: IUser = { id: 'Zloty Gestionado' };
        dispositivo.user = user;

        const userCollection: IUser[] = [{ id: 'Distrito Letonia' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dispositivo });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dispositivo: IDispositivo = { id: 456 };
        const user: IUser = { id: 'Violeta Papelería Inversor' };
        dispositivo.user = user;

        activatedRoute.data = of({ dispositivo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dispositivo));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispositivo = { id: 123 };
        spyOn(dispositivoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispositivo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dispositivo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dispositivoService.update).toHaveBeenCalledWith(dispositivo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispositivo = new Dispositivo();
        spyOn(dispositivoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispositivo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dispositivo }));
        saveSubject.complete();

        // THEN
        expect(dispositivoService.create).toHaveBeenCalledWith(dispositivo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispositivo = { id: 123 };
        spyOn(dispositivoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispositivo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dispositivoService.update).toHaveBeenCalledWith(dispositivo);
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
