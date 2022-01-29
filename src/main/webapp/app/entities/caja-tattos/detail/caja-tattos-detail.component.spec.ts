import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CajaTattosDetailComponent } from './caja-tattos-detail.component';

describe('Component Tests', () => {
  describe('CajaTattos Management Detail Component', () => {
    let comp: CajaTattosDetailComponent;
    let fixture: ComponentFixture<CajaTattosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CajaTattosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cajaTattos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CajaTattosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CajaTattosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cajaTattos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cajaTattos).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
