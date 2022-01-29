import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICitaPerforacion, CitaPerforacion } from '../cita-perforacion.model';

import { CitaPerforacionService } from './cita-perforacion.service';

describe('CitaPerforacion Service', () => {
  let service: CitaPerforacionService;
  let httpMock: HttpTestingController;
  let elemDefault: ICitaPerforacion;
  let expectedResult: ICitaPerforacion | ICitaPerforacion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CitaPerforacionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      fechaCita: currentDate,
      hora: 'AAAAAAA',
      nombreCliente: 'AAAAAAA',
      valorPerforacion: 0,
      valorPagado: 0,
      valorDeuda: 0,
      estado: 0,
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

    it('should create a CitaPerforacion', () => {
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

      service.create(new CitaPerforacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CitaPerforacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          hora: 'BBBBBB',
          nombreCliente: 'BBBBBB',
          valorPerforacion: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 1,
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

    it('should partial update a CitaPerforacion', () => {
      const patchObject = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          hora: 'BBBBBB',
          valorPagado: 1,
          estado: 1,
        },
        new CitaPerforacion()
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

    it('should return a list of CitaPerforacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          hora: 'BBBBBB',
          nombreCliente: 'BBBBBB',
          valorPerforacion: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 1,
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

    it('should delete a CitaPerforacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCitaPerforacionToCollectionIfMissing', () => {
      it('should add a CitaPerforacion to an empty array', () => {
        const citaPerforacion: ICitaPerforacion = { id: 123 };
        expectedResult = service.addCitaPerforacionToCollectionIfMissing([], citaPerforacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citaPerforacion);
      });

      it('should not add a CitaPerforacion to an array that contains it', () => {
        const citaPerforacion: ICitaPerforacion = { id: 123 };
        const citaPerforacionCollection: ICitaPerforacion[] = [
          {
            ...citaPerforacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addCitaPerforacionToCollectionIfMissing(citaPerforacionCollection, citaPerforacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CitaPerforacion to an array that doesn't contain it", () => {
        const citaPerforacion: ICitaPerforacion = { id: 123 };
        const citaPerforacionCollection: ICitaPerforacion[] = [{ id: 456 }];
        expectedResult = service.addCitaPerforacionToCollectionIfMissing(citaPerforacionCollection, citaPerforacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citaPerforacion);
      });

      it('should add only unique CitaPerforacion to an array', () => {
        const citaPerforacionArray: ICitaPerforacion[] = [{ id: 123 }, { id: 456 }, { id: 87226 }];
        const citaPerforacionCollection: ICitaPerforacion[] = [{ id: 123 }];
        expectedResult = service.addCitaPerforacionToCollectionIfMissing(citaPerforacionCollection, ...citaPerforacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const citaPerforacion: ICitaPerforacion = { id: 123 };
        const citaPerforacion2: ICitaPerforacion = { id: 456 };
        expectedResult = service.addCitaPerforacionToCollectionIfMissing([], citaPerforacion, citaPerforacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citaPerforacion);
        expect(expectedResult).toContain(citaPerforacion2);
      });

      it('should accept null and undefined values', () => {
        const citaPerforacion: ICitaPerforacion = { id: 123 };
        expectedResult = service.addCitaPerforacionToCollectionIfMissing([], null, citaPerforacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citaPerforacion);
      });

      it('should return initial array if no CitaPerforacion is added', () => {
        const citaPerforacionCollection: ICitaPerforacion[] = [{ id: 123 }];
        expectedResult = service.addCitaPerforacionToCollectionIfMissing(citaPerforacionCollection, undefined, null);
        expect(expectedResult).toEqual(citaPerforacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
