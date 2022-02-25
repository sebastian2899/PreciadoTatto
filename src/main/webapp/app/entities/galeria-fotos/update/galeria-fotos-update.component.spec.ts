jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GaleriaFotosService } from '../service/galeria-fotos.service';
import { IGaleriaFotos, GaleriaFotos } from '../galeria-fotos.model';

import { GaleriaFotosUpdateComponent } from './galeria-fotos-update.component';

describe('Component Tests', () => {
  describe('GaleriaFotos Management Update Component', () => {
    let comp: GaleriaFotosUpdateComponent;
    let fixture: ComponentFixture<GaleriaFotosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let galeriaFotosService: GaleriaFotosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GaleriaFotosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GaleriaFotosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GaleriaFotosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      galeriaFotosService = TestBed.inject(GaleriaFotosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const galeriaFotos: IGaleriaFotos = { id: 456 };

        activatedRoute.data = of({ galeriaFotos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(galeriaFotos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GaleriaFotos>>();
        const galeriaFotos = { id: 123 };
        jest.spyOn(galeriaFotosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ galeriaFotos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: galeriaFotos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(galeriaFotosService.update).toHaveBeenCalledWith(galeriaFotos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GaleriaFotos>>();
        const galeriaFotos = new GaleriaFotos();
        jest.spyOn(galeriaFotosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ galeriaFotos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: galeriaFotos }));
        saveSubject.complete();

        // THEN
        expect(galeriaFotosService.create).toHaveBeenCalledWith(galeriaFotos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GaleriaFotos>>();
        const galeriaFotos = { id: 123 };
        jest.spyOn(galeriaFotosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ galeriaFotos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(galeriaFotosService.update).toHaveBeenCalledWith(galeriaFotos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
