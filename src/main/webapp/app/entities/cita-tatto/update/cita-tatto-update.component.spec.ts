jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CitaTattoService } from '../service/cita-tatto.service';
import { ICitaTatto, CitaTatto } from '../cita-tatto.model';

import { CitaTattoUpdateComponent } from './cita-tatto-update.component';

describe('Component Tests', () => {
  describe('CitaTatto Management Update Component', () => {
    let comp: CitaTattoUpdateComponent;
    let fixture: ComponentFixture<CitaTattoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let citaTattoService: CitaTattoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CitaTattoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CitaTattoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CitaTattoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      citaTattoService = TestBed.inject(CitaTattoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const citaTatto: ICitaTatto = { id: 456 };

        activatedRoute.data = of({ citaTatto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(citaTatto));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaTatto>>();
        const citaTatto = { id: 123 };
        jest.spyOn(citaTattoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaTatto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: citaTatto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(citaTattoService.update).toHaveBeenCalledWith(citaTatto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaTatto>>();
        const citaTatto = new CitaTatto();
        jest.spyOn(citaTattoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaTatto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: citaTatto }));
        saveSubject.complete();

        // THEN
        expect(citaTattoService.create).toHaveBeenCalledWith(citaTatto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CitaTatto>>();
        const citaTatto = { id: 123 };
        jest.spyOn(citaTattoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ citaTatto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(citaTattoService.update).toHaveBeenCalledWith(citaTatto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
