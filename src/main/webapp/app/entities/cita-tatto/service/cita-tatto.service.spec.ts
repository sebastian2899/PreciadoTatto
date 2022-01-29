import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICitaTatto, CitaTatto } from '../cita-tatto.model';

import { CitaTattoService } from './cita-tatto.service';

describe('CitaTatto Service', () => {
  let service: CitaTattoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICitaTatto;
  let expectedResult: ICitaTatto | ICitaTatto[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CitaTattoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idCliente: 0,
      fechaCreacion: currentDate,
      fechaCita: currentDate,
      hora: 'AAAAAAA',
      emailCliente: 'AAAAAAA',
      fotoDisenoContentType: 'image/png',
      fotoDiseno: 'AAAAAAA',
      valorTatto: 0,
      valorPagado: 0,
      abono: 0,
      deuda: 0,
      estado: 'AAAAAAA',
      descripcion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CitaTatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.create(new CitaTatto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CitaTatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idCliente: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          hora: 'BBBBBB',
          emailCliente: 'BBBBBB',
          fotoDiseno: 'BBBBBB',
          valorTatto: 1,
          valorPagado: 1,
          abono: 1,
          deuda: 1,
          estado: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CitaTatto', () => {
      const patchObject = Object.assign(
        {
          hora: 'BBBBBB',
          emailCliente: 'BBBBBB',
          valorPagado: 1,
          deuda: 1,
        },
        new CitaTatto()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CitaTatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idCliente: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          hora: 'BBBBBB',
          emailCliente: 'BBBBBB',
          fotoDiseno: 'BBBBBB',
          valorTatto: 1,
          valorPagado: 1,
          abono: 1,
          deuda: 1,
          estado: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CitaTatto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCitaTattoToCollectionIfMissing', () => {
      it('should add a CitaTatto to an empty array', () => {
        const citaTatto: ICitaTatto = { id: 123 };
        expectedResult = service.addCitaTattoToCollectionIfMissing([], citaTatto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citaTatto);
      });

      it('should not add a CitaTatto to an array that contains it', () => {
        const citaTatto: ICitaTatto = { id: 123 };
        const citaTattoCollection: ICitaTatto[] = [
          {
            ...citaTatto,
          },
          { id: 456 },
        ];
        expectedResult = service.addCitaTattoToCollectionIfMissing(citaTattoCollection, citaTatto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CitaTatto to an array that doesn't contain it", () => {
        const citaTatto: ICitaTatto = { id: 123 };
        const citaTattoCollection: ICitaTatto[] = [{ id: 456 }];
        expectedResult = service.addCitaTattoToCollectionIfMissing(citaTattoCollection, citaTatto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citaTatto);
      });

      it('should add only unique CitaTatto to an array', () => {
        const citaTattoArray: ICitaTatto[] = [{ id: 123 }, { id: 456 }, { id: 38078 }];
        const citaTattoCollection: ICitaTatto[] = [{ id: 123 }];
        expectedResult = service.addCitaTattoToCollectionIfMissing(citaTattoCollection, ...citaTattoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const citaTatto: ICitaTatto = { id: 123 };
        const citaTatto2: ICitaTatto = { id: 456 };
        expectedResult = service.addCitaTattoToCollectionIfMissing([], citaTatto, citaTatto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citaTatto);
        expect(expectedResult).toContain(citaTatto2);
      });

      it('should accept null and undefined values', () => {
        const citaTatto: ICitaTatto = { id: 123 };
        expectedResult = service.addCitaTattoToCollectionIfMissing([], null, citaTatto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citaTatto);
      });

      it('should return initial array if no CitaTatto is added', () => {
        const citaTattoCollection: ICitaTatto[] = [{ id: 123 }];
        expectedResult = service.addCitaTattoToCollectionIfMissing(citaTattoCollection, undefined, null);
        expect(expectedResult).toEqual(citaTattoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
