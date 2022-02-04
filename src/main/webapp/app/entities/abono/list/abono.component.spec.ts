import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AbonoService } from '../service/abono.service';

import { AbonoComponent } from './abono.component';

describe('Component Tests', () => {
  describe('Abono Management Component', () => {
    let comp: AbonoComponent;
    let fixture: ComponentFixture<AbonoComponent>;
    let service: AbonoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AbonoComponent],
      })
        .overrideTemplate(AbonoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AbonoService);

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
      expect(comp.abonos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
