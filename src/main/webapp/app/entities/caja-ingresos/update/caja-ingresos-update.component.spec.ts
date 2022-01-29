jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CajaIngresosService } from '../service/caja-ingresos.service';
import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';

import { CajaIngresosUpdateComponent } from './caja-ingresos-update.component';

describe('Component Tests', () => {
  describe('CajaIngresos Management Update Component', () => {
    let comp: CajaIngresosUpdateComponent;
    let fixture: ComponentFixture<CajaIngresosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cajaIngresosService: CajaIngresosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CajaIngresosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CajaIngresosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CajaIngresosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cajaIngresosService = TestBed.inject(CajaIngresosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const cajaIngresos: ICajaIngresos = { id: 456 };

        activatedRoute.data = of({ cajaIngresos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cajaIngresos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaIngresos>>();
        const cajaIngresos = { id: 123 };
        jest.spyOn(cajaIngresosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaIngresos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cajaIngresos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cajaIngresosService.update).toHaveBeenCalledWith(cajaIngresos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaIngresos>>();
        const cajaIngresos = new CajaIngresos();
        jest.spyOn(cajaIngresosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaIngresos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cajaIngresos }));
        saveSubject.complete();

        // THEN
        expect(cajaIngresosService.create).toHaveBeenCalledWith(cajaIngresos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CajaIngresos>>();
        const cajaIngresos = { id: 123 };
        jest.spyOn(cajaIngresosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cajaIngresos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cajaIngresosService.update).toHaveBeenCalledWith(cajaIngresos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
