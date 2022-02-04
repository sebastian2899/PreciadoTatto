import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AbonoDetailComponent } from './abono-detail.component';

describe('Component Tests', () => {
  describe('Abono Management Detail Component', () => {
    let comp: AbonoDetailComponent;
    let fixture: ComponentFixture<AbonoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AbonoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ abono: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AbonoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AbonoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load abono on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.abono).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
