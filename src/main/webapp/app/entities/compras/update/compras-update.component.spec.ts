jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ComprasService } from '../service/compras.service';
import { ICompras, Compras } from '../compras.model';

import { ComprasUpdateComponent } from './compras-update.component';

describe('Component Tests', () => {
  describe('Compras Management Update Component', () => {
    let comp: ComprasUpdateComponent;
    let fixture: ComponentFixture<ComprasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let comprasService: ComprasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ComprasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ComprasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ComprasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      comprasService = TestBed.inject(ComprasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const compras: ICompras = { id: 456 };

        activatedRoute.data = of({ compras });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(compras));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Compras>>();
        const compras = { id: 123 };
        jest.spyOn(comprasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ compras });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compras }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(comprasService.update).toHaveBeenCalledWith(compras);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Compras>>();
        const compras = new Compras();
        jest.spyOn(comprasService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ compras });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compras }));
        saveSubject.complete();

        // THEN
        expect(comprasService.create).toHaveBeenCalledWith(compras);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Compras>>();
        const compras = { id: 123 };
        jest.spyOn(comprasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ compras });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(comprasService.update).toHaveBeenCalledWith(compras);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
