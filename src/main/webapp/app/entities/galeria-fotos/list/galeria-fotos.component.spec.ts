import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GaleriaFotosService } from '../service/galeria-fotos.service';

import { GaleriaFotosComponent } from './galeria-fotos.component';

describe('Component Tests', () => {
  describe('GaleriaFotos Management Component', () => {
    let comp: GaleriaFotosComponent;
    let fixture: ComponentFixture<GaleriaFotosComponent>;
    let service: GaleriaFotosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GaleriaFotosComponent],
      })
        .overrideTemplate(GaleriaFotosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GaleriaFotosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GaleriaFotosService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.galeriaFotos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
