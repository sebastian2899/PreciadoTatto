import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VentasService } from '../service/ventas.service';

import { VentasComponent } from './ventas.component';

describe('Component Tests', () => {
  describe('Ventas Management Component', () => {
    let comp: VentasComponent;
    let fixture: ComponentFixture<VentasComponent>;
    let service: VentasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VentasComponent],
      })
        .overrideTemplate(VentasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(VentasService);

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
      expect(comp.ventas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
