package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Abono;
import com.mycompany.myapp.domain.CitaPerforacion;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.AbonoRepository;
import com.mycompany.myapp.repository.CitaPerforacionRepository;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.AbonoService;
import com.mycompany.myapp.service.dto.AbonoDTO;
import com.mycompany.myapp.service.mapper.AbonoMapper;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Abono}.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final CitaTattoRepository citaTattoRepository;

    private final AbonoMapper abonoMapper;

    private final CitaPerforacionRepository citaPerforacionRepository;

    public AbonoServiceImpl(
        AbonoRepository abonoRepository,
        AbonoMapper abonoMapper,
        CitaTattoRepository citaTattoRepository,
        CitaPerforacionRepository citaPerforacionRepository
    ) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
        this.citaTattoRepository = citaTattoRepository;
        this.citaPerforacionRepository = citaPerforacionRepository;
    }

    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono.setFechaAbono(Instant.now());

        // SE CONSULTA SI EXISTE ALGUNA CITA CON EL IDCITA QUE TRAE EL ABONO
        if (abonoDTO.getIdCita() != null) {
            CitaTatto cita = citaTattoRepository.citaById(abonoDTO.getIdCita());
            //SI EL IDCITA QUE TRAE EL ABONO ES DE TATTO SE RESTA EL VALOR ABONO AL VALOR DEUDA DE LA CITA TATTO
            if (cita != null) {
                cita.setDeuda(cita.getDeuda().subtract(abonoDTO.getValorAbono()));

                citaTattoRepository.save(cita);
                abono.setIdCita(cita.getId());
                abono.setTipoCita("Tattoo Preciado");
            }
        }

        /*SI EL IDCITA DEL ABONO ES DE PERFORACION SE VALIDA SI LA CITA ES TATTOO O ES PERFORACION.
         * SE RESTA TODO EL VALOR DEL ABONO A LA DEUDA PERO EL VALOR QUE IRA A LA CAJA TENDRA EL DESCUENTO
         * CORRESPONDIENTE SEGUN LA CITA.
         */
        CitaPerforacion citaPerfo = citaPerforacionRepository.citaPorId(abonoDTO.getIdCita());
        if (citaPerfo != null) {
            citaPerfo.setValorDeuda(citaPerfo.getValorDeuda().subtract(abono.getValorAbono()));

            String tipoCita = citaPerfo.getTipoCita();

            switch (tipoCita) {
                case "Cita Tattoo":
                    BigDecimal descuento = new BigDecimal(30);

                    BigDecimal valorDescontar =
                        (abono.getValorAbono().multiply(descuento).divide(new BigDecimal(100), MathContext.DECIMAL128));

                    //abono.setValorCaja(valorDescontar);
                    abono.setValorAbono(valorDescontar);
                    abono.setTipoCita("Cita Tattoo");
                    abono.setIdCita(abonoDTO.getIdCita());
                    break;
                case "Cita Perforacion":
                    BigDecimal descuento2 = new BigDecimal(50);

                    BigDecimal valorDescuento =
                        (abono.getValorAbono().multiply(descuento2).divide(new BigDecimal(100), MathContext.DECIMAL128));

                    //citaPerfo.setValorCaja(valorDescuento);
                    abono.setValorAbono(valorDescuento);
                    abono.setTipoCita("Cita Perforacion");
                    abono.setIdCita(abonoDTO.getIdCita());
                    break;
                default:
                    break;
            }
        }

        abono = abonoRepository.save(abono);
        return abonoMapper.toDto(abono);
    }

    @Override
    public Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO) {
        log.debug("Request to partially update Abono : {}", abonoDTO);

        return abonoRepository
            .findById(abonoDTO.getId())
            .map(existingAbono -> {
                abonoMapper.partialUpdate(existingAbono, abonoDTO);

                return existingAbono;
            })
            .map(abonoRepository::save)
            .map(abonoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbonoDTO> findAll() {
        log.debug("Request to get all Abonos");
        return abonoRepository.findAll().stream().map(abonoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AbonoDTO> findOne(Long id) {
        log.debug("Request to get Abono : {}", id);
        return abonoRepository.findById(id).map(abonoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abono : {}", id);
        abonoRepository.deleteById(id);
    }

    @Override
    public BigDecimal valorDeuda(Long id) {
        BigDecimal valorRetorno = new BigDecimal(0);
        BigDecimal deuda = abonoRepository.valorDeuda(id);

        BigDecimal valorDeudaPerfo = abonoRepository.valorDeudaPerfo(id);

        if (deuda == null) {
            deuda = BigDecimal.ZERO;
            valorRetorno = deuda;
        } else {
            valorRetorno = deuda;
            return valorRetorno;
        }

        if (valorDeudaPerfo == null) {
            valorDeudaPerfo = BigDecimal.ZERO;
            valorRetorno = valorDeudaPerfo;
        } else {
            valorRetorno = valorDeudaPerfo;
        }
        return valorRetorno;
    }

    @Override
    public List<AbonoDTO> consultarAbonoPorCita(Long idCita) {
        return abonoRepository.abonosPorCita(idCita).stream().map(abonoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
