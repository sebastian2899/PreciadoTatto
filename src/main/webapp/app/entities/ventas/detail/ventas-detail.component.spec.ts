import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VentasDetailComponent } from './ventas-detail.component';

describe('Component Tests', () => {
  describe('Ventas Management Detail Component', () => {
    let comp: VentasDetailComponent;
    let fixture: ComponentFixture<VentasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VentasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ventas: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VentasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VentasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ventas on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ventas).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
