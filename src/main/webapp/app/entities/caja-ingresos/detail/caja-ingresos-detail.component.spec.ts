import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CajaIngresosDetailComponent } from './caja-ingresos-detail.component';

describe('Component Tests', () => {
  describe('CajaIngresos Management Detail Component', () => {
    let comp: CajaIngresosDetailComponent;
    let fixture: ComponentFixture<CajaIngresosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CajaIngresosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cajaIngresos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CajaIngresosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CajaIngresosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cajaIngresos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cajaIngresos).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
