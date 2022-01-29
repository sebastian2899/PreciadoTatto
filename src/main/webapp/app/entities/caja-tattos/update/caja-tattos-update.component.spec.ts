jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CajaTattosService } from '../service/caja-tattos.service';
import { ICajaTattos, CajaTattos } from '../caja-tattos.model';

import { CajaTattosUpdateComponent } from './caja-tattos-update.component';

describe('Component Tests', () => {
  describe('CajaTattos Management Update Component', () => {
    let comp: CajaTattosUpdateComponent;
    let fixture: ComponentFixture<CajaTattosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cajaTattosService: CajaTattosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CajaTattosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CajaTattosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CajaTattosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cajaTattosService = TestBed.inject(CajaTattosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const cajaTattos: ICajaTattos = { id: 456 };

        activatedRoute.data = of({ cajaTattos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cajaTattos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaTattos>>();
        const cajaTattos = { id: 123 };
        jest.spyOn(cajaTattosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaTattos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cajaTattos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cajaTattosService.update).toHaveBeenCalledWith(cajaTattos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaTattos>>();
        const cajaTattos = new CajaTattos();
        jest.spyOn(cajaTattosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaTattos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cajaTattos }));
        saveSubject.complete();

        // THEN
        expect(cajaTattosService.create).toHaveBeenCalledWith(cajaTattos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaTattos>>();
        const cajaTattos = { id: 123 };
        jest.spyOn(cajaTattosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaTattos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cajaTattosService.update).toHaveBeenCalledWith(cajaTattos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
