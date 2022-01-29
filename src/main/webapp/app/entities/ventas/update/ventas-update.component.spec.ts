jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VentasService } from '../service/ventas.service';
import { IVentas, Ventas } from '../ventas.model';

import { VentasUpdateComponent } from './ventas-update.component';

describe('Component Tests', () => {
  describe('Ventas Management Update Component', () => {
    let comp: VentasUpdateComponent;
    let fixture: ComponentFixture<VentasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ventasService: VentasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VentasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VentasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ventasService = TestBed.inject(VentasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ventas: IVentas = { id: 456 };

        activatedRoute.data = of({ ventas });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ventas));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ventas>>();
        const ventas = { id: 123 };
        jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ventas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ventas }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ventasService.update).toHaveBeenCalledWith(ventas);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ventas>>();
        const ventas = new Ventas();
        jest.spyOn(ventasService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ventas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ventas }));
        saveSubject.complete();

        // THEN
        expect(ventasService.create).toHaveBeenCalledWith(ventas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ventas>>();
        const ventas = { id: 123 };
        jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ventas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ventasService.update).toHaveBeenCalledWith(ventas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
