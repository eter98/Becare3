jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AdherenciaService } from '../service/adherencia.service';

import { AdherenciaDeleteDialogComponent } from './adherencia-delete-dialog.component';

describe('Component Tests', () => {
  describe('Adherencia Management Delete Component', () => {
    let comp: AdherenciaDeleteDialogComponent;
    let fixture: ComponentFixture<AdherenciaDeleteDialogComponent>;
    let service: AdherenciaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdherenciaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AdherenciaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdherenciaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdherenciaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
