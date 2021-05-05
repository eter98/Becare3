jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FarmaceuticaService } from '../service/farmaceutica.service';
import { IFarmaceutica, Farmaceutica } from '../farmaceutica.model';

import { FarmaceuticaUpdateComponent } from './farmaceutica-update.component';

describe('Component Tests', () => {
  describe('Farmaceutica Management Update Component', () => {
    let comp: FarmaceuticaUpdateComponent;
    let fixture: ComponentFixture<FarmaceuticaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let farmaceuticaService: FarmaceuticaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FarmaceuticaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FarmaceuticaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FarmaceuticaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      farmaceuticaService = TestBed.inject(FarmaceuticaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const farmaceutica: IFarmaceutica = { id: 456 };

        activatedRoute.data = of({ farmaceutica });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(farmaceutica));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const farmaceutica = { id: 123 };
        spyOn(farmaceuticaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ farmaceutica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: farmaceutica }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(farmaceuticaService.update).toHaveBeenCalledWith(farmaceutica);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const farmaceutica = new Farmaceutica();
        spyOn(farmaceuticaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ farmaceutica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: farmaceutica }));
        saveSubject.complete();

        // THEN
        expect(farmaceuticaService.create).toHaveBeenCalledWith(farmaceutica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const farmaceutica = { id: 123 };
        spyOn(farmaceuticaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ farmaceutica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(farmaceuticaService.update).toHaveBeenCalledWith(farmaceutica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
