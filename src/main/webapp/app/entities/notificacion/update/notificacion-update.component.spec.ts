jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NotificacionService } from '../service/notificacion.service';
import { INotificacion, Notificacion } from '../notificacion.model';
import { ITokenDisp } from 'app/entities/token-disp/token-disp.model';
import { TokenDispService } from 'app/entities/token-disp/service/token-disp.service';

import { NotificacionUpdateComponent } from './notificacion-update.component';

describe('Component Tests', () => {
  describe('Notificacion Management Update Component', () => {
    let comp: NotificacionUpdateComponent;
    let fixture: ComponentFixture<NotificacionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let notificacionService: NotificacionService;
    let tokenDispService: TokenDispService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NotificacionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NotificacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificacionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      notificacionService = TestBed.inject(NotificacionService);
      tokenDispService = TestBed.inject(TokenDispService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TokenDisp query and add missing value', () => {
        const notificacion: INotificacion = { id: 456 };
        const token: ITokenDisp = { id: 54458 };
        notificacion.token = token;

        const tokenDispCollection: ITokenDisp[] = [{ id: 93776 }];
        spyOn(tokenDispService, 'query').and.returnValue(of(new HttpResponse({ body: tokenDispCollection })));
        const additionalTokenDisps = [token];
        const expectedCollection: ITokenDisp[] = [...additionalTokenDisps, ...tokenDispCollection];
        spyOn(tokenDispService, 'addTokenDispToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ notificacion });
        comp.ngOnInit();

        expect(tokenDispService.query).toHaveBeenCalled();
        expect(tokenDispService.addTokenDispToCollectionIfMissing).toHaveBeenCalledWith(tokenDispCollection, ...additionalTokenDisps);
        expect(comp.tokenDispsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const notificacion: INotificacion = { id: 456 };
        const token: ITokenDisp = { id: 17426 };
        notificacion.token = token;

        activatedRoute.data = of({ notificacion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(notificacion));
        expect(comp.tokenDispsSharedCollection).toContain(token);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notificacion = { id: 123 };
        spyOn(notificacionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notificacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notificacion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(notificacionService.update).toHaveBeenCalledWith(notificacion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notificacion = new Notificacion();
        spyOn(notificacionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notificacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notificacion }));
        saveSubject.complete();

        // THEN
        expect(notificacionService.create).toHaveBeenCalledWith(notificacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notificacion = { id: 123 };
        spyOn(notificacionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notificacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(notificacionService.update).toHaveBeenCalledWith(notificacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTokenDispById', () => {
        it('Should return tracked TokenDisp primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTokenDispById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
