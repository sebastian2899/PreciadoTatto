import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CitaPerforacionDetailComponent } from './cita-perforacion-detail.component';

describe('Component Tests', () => {
  describe('CitaPerforacion Management Detail Component', () => {
    let comp: CitaPerforacionDetailComponent;
    let fixture: ComponentFixture<CitaPerforacionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CitaPerforacionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ citaPerforacion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CitaPerforacionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CitaPerforacionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load citaPerforacion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.citaPerforacion).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
