jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AbonoService } from '../service/abono.service';
import { AbonoUpdateComponent } from './abono-update.component';
import { ICitaTatto } from 'app/entities/cita-tatto/cita-tatto.model';

describe('Component Tests', () => {
  describe('Abono Management Update Component', () => {
    let comp: AbonoUpdateComponent;
    let fixture: ComponentFixture<AbonoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let abonoService: AbonoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AbonoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AbonoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      abonoService = TestBed.inject(AbonoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const citaTatto: ICitaTatto = { id: 456 };

        activatedRoute.data = of({ citaTatto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(citaTatto));
      });
    });
  });
});
