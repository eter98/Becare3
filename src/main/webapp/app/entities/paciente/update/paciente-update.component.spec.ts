jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PacienteService } from '../service/paciente.service';
import { IPaciente, Paciente } from '../paciente.model';
import { ICondicion } from 'app/entities/condicion/condicion.model';
import { CondicionService } from 'app/entities/condicion/service/condicion.service';
import { IIPS } from 'app/entities/ips/ips.model';
import { IPSService } from 'app/entities/ips/service/ips.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { TratamietoService } from 'app/entities/tratamieto/service/tratamieto.service';
import { IFarmaceutica } from 'app/entities/farmaceutica/farmaceutica.model';
import { FarmaceuticaService } from 'app/entities/farmaceutica/service/farmaceutica.service';

import { PacienteUpdateComponent } from './paciente-update.component';

describe('Component Tests', () => {
  describe('Paciente Management Update Component', () => {
    let comp: PacienteUpdateComponent;
    let fixture: ComponentFixture<PacienteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pacienteService: PacienteService;
    let condicionService: CondicionService;
    let iPSService: IPSService;
    let userService: UserService;
    let tratamietoService: TratamietoService;
    let farmaceuticaService: FarmaceuticaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PacienteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PacienteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PacienteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pacienteService = TestBed.inject(PacienteService);
      condicionService = TestBed.inject(CondicionService);
      iPSService = TestBed.inject(IPSService);
      userService = TestBed.inject(UserService);
      tratamietoService = TestBed.inject(TratamietoService);
      farmaceuticaService = TestBed.inject(FarmaceuticaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Condicion query and add missing value', () => {
        const paciente: IPaciente = { id: 456 };
        const condicion: ICondicion = { id: 23516 };
        paciente.condicion = condicion;

        const condicionCollection: ICondicion[] = [{ id: 17219 }];
        spyOn(condicionService, 'query').and.returnValue(of(new HttpResponse({ body: condicionCollection })));
        const additionalCondicions = [condicion];
        const expectedCollection: ICondicion[] = [...additionalCondicions, ...condicionCollection];
        spyOn(condicionService, 'addCondicionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(condicionService.query).toHaveBeenCalled();
        expect(condicionService.addCondicionToCollectionIfMissing).toHaveBeenCalledWith(condicionCollection, ...additionalCondicions);
        expect(comp.condicionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call IPS query and add missing value', () => {
        const paciente: IPaciente = { id: 456 };
        const ips: IIPS = { id: 67651 };
        paciente.ips = ips;

        const iPSCollection: IIPS[] = [{ id: 70356 }];
        spyOn(iPSService, 'query').and.returnValue(of(new HttpResponse({ body: iPSCollection })));
        const additionalIPS = [ips];
        const expectedCollection: IIPS[] = [...additionalIPS, ...iPSCollection];
        spyOn(iPSService, 'addIPSToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(iPSService.query).toHaveBeenCalled();
        expect(iPSService.addIPSToCollectionIfMissing).toHaveBeenCalledWith(iPSCollection, ...additionalIPS);
        expect(comp.iPSSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const paciente: IPaciente = { id: 456 };
        const user: IUser = { id: 'calculating' };
        paciente.user = user;

        const userCollection: IUser[] = [{ id: 'Sabroso Verde' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tratamieto query and add missing value', () => {
        const paciente: IPaciente = { id: 456 };
        const tratamiento: ITratamieto = { id: 28946 };
        paciente.tratamiento = tratamiento;

        const tratamietoCollection: ITratamieto[] = [{ id: 97679 }];
        spyOn(tratamietoService, 'query').and.returnValue(of(new HttpResponse({ body: tratamietoCollection })));
        const additionalTratamietos = [tratamiento];
        const expectedCollection: ITratamieto[] = [...additionalTratamietos, ...tratamietoCollection];
        spyOn(tratamietoService, 'addTratamietoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(tratamietoService.query).toHaveBeenCalled();
        expect(tratamietoService.addTratamietoToCollectionIfMissing).toHaveBeenCalledWith(tratamietoCollection, ...additionalTratamietos);
        expect(comp.tratamietosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Farmaceutica query and add missing value', () => {
        const paciente: IPaciente = { id: 456 };
        const farmaceutica: IFarmaceutica = { id: 93952 };
        paciente.farmaceutica = farmaceutica;

        const farmaceuticaCollection: IFarmaceutica[] = [{ id: 32730 }];
        spyOn(farmaceuticaService, 'query').and.returnValue(of(new HttpResponse({ body: farmaceuticaCollection })));
        const additionalFarmaceuticas = [farmaceutica];
        const expectedCollection: IFarmaceutica[] = [...additionalFarmaceuticas, ...farmaceuticaCollection];
        spyOn(farmaceuticaService, 'addFarmaceuticaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(farmaceuticaService.query).toHaveBeenCalled();
        expect(farmaceuticaService.addFarmaceuticaToCollectionIfMissing).toHaveBeenCalledWith(
          farmaceuticaCollection,
          ...additionalFarmaceuticas
        );
        expect(comp.farmaceuticasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paciente: IPaciente = { id: 456 };
        const condicion: ICondicion = { id: 10343 };
        paciente.condicion = condicion;
        const ips: IIPS = { id: 93730 };
        paciente.ips = ips;
        const user: IUser = { id: 'e-services Polígono Enfocado' };
        paciente.user = user;
        const tratamiento: ITratamieto = { id: 36187 };
        paciente.tratamiento = tratamiento;
        const farmaceutica: IFarmaceutica = { id: 30485 };
        paciente.farmaceutica = farmaceutica;

        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paciente));
        expect(comp.condicionsSharedCollection).toContain(condicion);
        expect(comp.iPSSharedCollection).toContain(ips);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.tratamietosSharedCollection).toContain(tratamiento);
        expect(comp.farmaceuticasSharedCollection).toContain(farmaceutica);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paciente = { id: 123 };
        spyOn(pacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paciente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pacienteService.update).toHaveBeenCalledWith(paciente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paciente = new Paciente();
        spyOn(pacienteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paciente }));
        saveSubject.complete();

        // THEN
        expect(pacienteService.create).toHaveBeenCalledWith(paciente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paciente = { id: 123 };
        spyOn(pacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pacienteService.update).toHaveBeenCalledWith(paciente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCondicionById', () => {
        it('Should return tracked Condicion primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCondicionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackIPSById', () => {
        it('Should return tracked IPS primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackIPSById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTratamietoById', () => {
        it('Should return tracked Tratamieto primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTratamietoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFarmaceuticaById', () => {
        it('Should return tracked Farmaceutica primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFarmaceuticaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
