import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CajaIngresosService } from '../service/caja-ingresos.service';

import { CajaIngresosComponent } from './caja-ingresos.component';

describe('Component Tests', () => {
  describe('CajaIngresos Management Component', () => {
    let comp: CajaIngresosComponent;
    let fixture: ComponentFixture<CajaIngresosComponent>;
    let service: CajaIngresosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CajaIngresosComponent],
      })
        .overrideTemplate(CajaIngresosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CajaIngresosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CajaIngresosService);

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
      expect(comp.cajaIngresos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
