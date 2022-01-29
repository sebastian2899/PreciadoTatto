import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CitaTattoService } from '../service/cita-tatto.service';

import { CitaTattoComponent } from './cita-tatto.component';

describe('Component Tests', () => {
  describe('CitaTatto Management Component', () => {
    let comp: CitaTattoComponent;
    let fixture: ComponentFixture<CitaTattoComponent>;
    let service: CitaTattoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CitaTattoComponent],
      })
        .overrideTemplate(CitaTattoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CitaTattoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CitaTattoService);

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
      expect(comp.citaTattos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
