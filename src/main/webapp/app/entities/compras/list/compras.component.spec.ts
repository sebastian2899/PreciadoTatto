import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ComprasService } from '../service/compras.service';

import { ComprasComponent } from './compras.component';

describe('Component Tests', () => {
  describe('Compras Management Component', () => {
    let comp: ComprasComponent;
    let fixture: ComponentFixture<ComprasComponent>;
    let service: ComprasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ComprasComponent],
      })
        .overrideTemplate(ComprasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ComprasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ComprasService);

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
      expect(comp.compras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
