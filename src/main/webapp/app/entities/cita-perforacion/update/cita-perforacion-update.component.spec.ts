jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CitaPerforacionService } from '../service/cita-perforacion.service';
import { ICitaPerforacion, CitaPerforacion } from '../cita-perforacion.model';

import { CitaPerforacionUpdateComponent } from './cita-perforacion-update.component';

describe('Component Tests', () => {
  describe('CitaPerforacion Management Update Component', () => {
    let comp: CitaPerforacionUpdateComponent;
    let fixture: ComponentFixture<CitaPerforacionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let citaPerforacionService: CitaPerforacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CitaPerforacionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CitaPerforacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CitaPerforacionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      citaPerforacionService = TestBed.inject(CitaPerforacionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const citaPerforacion: ICitaPerforacion = { id: 456 };

        activatedRoute.data = of({ citaPerforacion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(citaPerforacion));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaPerforacion>>();
        const citaPerforacion = { id: 123 };
        jest.spyOn(citaPerforacionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaPerforacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: citaPerforacion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(citaPerforacionService.update).toHaveBeenCalledWith(citaPerforacion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaPerforacion>>();
        const citaPerforacion = new CitaPerforacion();
        jest.spyOn(citaPerforacionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaPerforacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: citaPerforacion }));
        saveSubject.complete();

        // THEN
        expect(citaPerforacionService.create).toHaveBeenCalledWith(citaPerforacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaPerforacion>>();
        const citaPerforacion = { id: 123 };
        jest.spyOn(citaPerforacionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaPerforacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(citaPerforacionService.update).toHaveBeenCalledWith(citaPerforacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
