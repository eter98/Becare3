jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TratamietoService } from '../service/tratamieto.service';
import { ITratamieto, Tratamieto } from '../tratamieto.model';

import { TratamietoUpdateComponent } from './tratamieto-update.component';

describe('Component Tests', () => {
  describe('Tratamieto Management Update Component', () => {
    let comp: TratamietoUpdateComponent;
    let fixture: ComponentFixture<TratamietoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tratamietoService: TratamietoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TratamietoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TratamietoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TratamietoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tratamietoService = TestBed.inject(TratamietoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tratamieto: ITratamieto = { id: 456 };

        activatedRoute.data = of({ tratamieto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tratamieto));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamieto = { id: 123 };
        spyOn(tratamietoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamieto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tratamieto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tratamietoService.update).toHaveBeenCalledWith(tratamieto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamieto = new Tratamieto();
        spyOn(tratamietoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamieto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tratamieto }));
        saveSubject.complete();

        // THEN
        expect(tratamietoService.create).toHaveBeenCalledWith(tratamieto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tratamieto = { id: 123 };
        spyOn(tratamietoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tratamieto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tratamietoService.update).toHaveBeenCalledWith(tratamieto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
