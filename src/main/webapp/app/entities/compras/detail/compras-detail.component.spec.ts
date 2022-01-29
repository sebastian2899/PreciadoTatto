import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComprasDetailComponent } from './compras-detail.component';

describe('Component Tests', () => {
  describe('Compras Management Detail Component', () => {
    let comp: ComprasDetailComponent;
    let fixture: ComponentFixture<ComprasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ComprasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ compras: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ComprasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ComprasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load compras on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.compras).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
