jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CondicionService } from '../service/condicion.service';
import { ICondicion, Condicion } from '../condicion.model';

import { CondicionUpdateComponent } from './condicion-update.component';

describe('Component Tests', () => {
  describe('Condicion Management Update Component', () => {
    let comp: CondicionUpdateComponent;
    let fixture: ComponentFixture<CondicionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let condicionService: CondicionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CondicionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CondicionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CondicionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      condicionService = TestBed.inject(CondicionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const condicion: ICondicion = { id: 456 };

        activatedRoute.data = of({ condicion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(condicion));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const condicion = { id: 123 };
        spyOn(condicionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ condicion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: condicion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(condicionService.update).toHaveBeenCalledWith(condicion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const condicion = new Condicion();
        spyOn(condicionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ condicion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: condicion }));
        saveSubject.complete();

        // THEN
        expect(condicionService.create).toHaveBeenCalledWith(condicion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const condicion = { id: 123 };
        spyOn(condicionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ condicion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(condicionService.update).toHaveBeenCalledWith(condicion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
