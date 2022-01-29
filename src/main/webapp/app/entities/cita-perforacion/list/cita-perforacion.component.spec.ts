import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CitaPerforacionService } from '../service/cita-perforacion.service';

import { CitaPerforacionComponent } from './cita-perforacion.component';

describe('Component Tests', () => {
  describe('CitaPerforacion Management Component', () => {
    let comp: CitaPerforacionComponent;
    let fixture: ComponentFixture<CitaPerforacionComponent>;
    let service: CitaPerforacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CitaPerforacionComponent],
      })
        .overrideTemplate(CitaPerforacionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CitaPerforacionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CitaPerforacionService);

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
      expect(comp.citaPerforacions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
