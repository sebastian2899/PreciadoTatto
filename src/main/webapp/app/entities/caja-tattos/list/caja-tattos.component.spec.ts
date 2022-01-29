import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CajaTattosService } from '../service/caja-tattos.service';

import { CajaTattosComponent } from './caja-tattos.component';

describe('Component Tests', () => {
  describe('CajaTattos Management Component', () => {
    let comp: CajaTattosComponent;
    let fixture: ComponentFixture<CajaTattosComponent>;
    let service: CajaTattosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CajaTattosComponent],
      })
        .overrideTemplate(CajaTattosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CajaTattosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CajaTattosService);

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
      expect(comp.cajaTattos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
