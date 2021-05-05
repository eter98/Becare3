jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IPSService } from '../service/ips.service';
import { IIPS, IPS } from '../ips.model';

import { IPSUpdateComponent } from './ips-update.component';

describe('Component Tests', () => {
  describe('IPS Management Update Component', () => {
    let comp: IPSUpdateComponent;
    let fixture: ComponentFixture<IPSUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let iPSService: IPSService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IPSUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IPSUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IPSUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      iPSService = TestBed.inject(IPSService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const iPS: IIPS = { id: 456 };

        activatedRoute.data = of({ iPS });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(iPS));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iPS = { id: 123 };
        spyOn(iPSService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iPS });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: iPS }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(iPSService.update).toHaveBeenCalledWith(iPS);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iPS = new IPS();
        spyOn(iPSService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iPS });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: iPS }));
        saveSubject.complete();

        // THEN
        expect(iPSService.create).toHaveBeenCalledWith(iPS);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const iPS = { id: 123 };
        spyOn(iPSService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ iPS });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(iPSService.update).toHaveBeenCalledWith(iPS);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
