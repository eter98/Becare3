jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TokenDispService } from '../service/token-disp.service';
import { ITokenDisp, TokenDisp } from '../token-disp.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TokenDispUpdateComponent } from './token-disp-update.component';

describe('Component Tests', () => {
  describe('TokenDisp Management Update Component', () => {
    let comp: TokenDispUpdateComponent;
    let fixture: ComponentFixture<TokenDispUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tokenDispService: TokenDispService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TokenDispUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TokenDispUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TokenDispUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tokenDispService = TestBed.inject(TokenDispService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const tokenDisp: ITokenDisp = { id: 456 };
        const user: IUser = { id: 'withdrawal' };
        tokenDisp.user = user;

        const userCollection: IUser[] = [{ id: 'Aragón Entrada virtual' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tokenDisp });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tokenDisp: ITokenDisp = { id: 456 };
        const user: IUser = { id: 'withdrawal Adelante actitud' };
        tokenDisp.user = user;

        activatedRoute.data = of({ tokenDisp });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tokenDisp));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tokenDisp = { id: 123 };
        spyOn(tokenDispService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tokenDisp });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tokenDisp }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tokenDispService.update).toHaveBeenCalledWith(tokenDisp);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tokenDisp = new TokenDisp();
        spyOn(tokenDispService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tokenDisp });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tokenDisp }));
        saveSubject.complete();

        // THEN
        expect(tokenDispService.create).toHaveBeenCalledWith(tokenDisp);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tokenDisp = { id: 123 };
        spyOn(tokenDispService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tokenDisp });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tokenDispService.update).toHaveBeenCalledWith(tokenDisp);
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
