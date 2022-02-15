package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.config.EmailUtil;
import com.mycompany.myapp.domain.Abono;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.AbonoRepository;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.CitaTattoService;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
import com.mycompany.myapp.service.mapper.CitaTattoMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CitaTatto}.
 */
@Service
@Transactional
public class CitaTattoServiceImpl implements CitaTattoService {

    private final Logger log = LoggerFactory.getLogger(CitaTattoServiceImpl.class);

    private final CitaTattoRepository citaTattoRepository;

    private final AbonoRepository abonoRepository;

    private final CitaTattoMapper citaTattoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public CitaTattoServiceImpl(AbonoRepository abonoRepository, CitaTattoRepository citaTattoRepository, CitaTattoMapper citaTattoMapper) {
        this.citaTattoRepository = citaTattoRepository;
        this.citaTattoMapper = citaTattoMapper;
        this.abonoRepository = abonoRepository;
    }

    @Override
    public CitaTattoDTO save(CitaTattoDTO citaTattoDTO) {
        log.debug("Request to save CitaTatto : {}", citaTattoDTO);
        CitaTatto citaTatto = citaTattoMapper.toEntity(citaTattoDTO);
        citaTatto.setFechaCreacion(Instant.now());

        if (citaTatto.getDeuda().equals(BigDecimal.ZERO)) {
            citaTatto.estado("Pagada");
        }

        citaTatto = citaTattoRepository.save(citaTatto);
        if (citaTattoDTO.getId() != null) {
            enviarCorreoActualizacion(citaTatto);
        } else {
            enviarCorreoConfirmacion(citaTatto);
        }

        Abono abono = null;

        if (citaTatto.getValorPagado() != null) {
            abono = new Abono();
            abono.setIdCita(citaTatto.getId());
            abono.setFechaAbono(citaTatto.getFechaCreacion());
            abono.setValorAbono(citaTatto.getValorPagado());

            this.abonoRepository.save(abono);
        }

        return citaTattoMapper.toDto(citaTatto);
    }

    private void enviarCorreoConfirmacion(CitaTatto citaTattoo) {
        if (citaTattoo.getEmailCliente() != null && !citaTattoo.getEmailCliente().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String html = "";
            String mensaje =
                "<html><head><title>Confirmacion Correo</title></head>" +
                "<body style='background-color:black;'><p style='color:aqua;'>Buen dia " +
                citaTattoo.getInfoCliente() +
                ".<br/>" +
                "Su cita a sido agendada con exito para el dia " +
                format.format(Date.from(citaTattoo.getFechaCita())) +
                " a las " +
                citaTattoo.getHora() +
                ".</p>" +
                "<p style='color:aqua;'>Recuerde llegar 10-15 minutos antes para la toma de medidas." +
                "<br/>Muchas Gracias.<br/><br/><br/>Preciado Tattoo.</p></body></html>";

            try {
                log.debug("Request to send mail to client: " + citaTattoo.getEmailCliente());
                EmailUtil.sendArchivoTLS(null, citaTattoo.getEmailCliente().trim(), "Confirmacion Cita Tattoo", mensaje);
                log.debug("Send email to: " + citaTattoo.getEmailCliente());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                log.error("Error enviando notificacion ", e);
                log.error("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void enviarCorreoActualizacion(CitaTatto citaTattoo) {
        if (citaTattoo.getEmailCliente() != null && !citaTattoo.getEmailCliente().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String html = "";
            String mensaje =
                "<html><head><title>Confirmacion Correo</title></head>" +
                "<body style='background-color:black;'><p style='color:aqua;'>Buen dia " +
                citaTattoo.getInfoCliente() +
                ".<br/>" +
                "Su cita a sido Actualizada con exito para el dia " +
                format.format(Date.from(citaTattoo.getFechaCita())) +
                " a las " +
                citaTattoo.getHora() +
                ".</p>" +
                "<p style='color:aqua;'>Recuerde llegar 10-15 minutos antes para la toma de medidas." +
                "<br/>Muchas Gracias.<br/><br/><br/>Preciado Tattoo.</p></body></html>";

            try {
                log.debug("Request to send mail to client: " + citaTattoo.getEmailCliente());
                EmailUtil.sendArchivoTLS(null, citaTattoo.getEmailCliente().trim(), "Confirmacion Cita Tattoo", mensaje);
                log.debug("Send email to: " + citaTattoo.getEmailCliente());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                log.error("Error enviando notificacion ", e);
                log.error("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<CitaTattoDTO> partialUpdate(CitaTattoDTO citaTattoDTO) {
        log.debug("Request to partially update CitaTatto : {}", citaTattoDTO);

        return citaTattoRepository
            .findById(citaTattoDTO.getId())
            .map(existingCitaTatto -> {
                citaTattoMapper.partialUpdate(existingCitaTatto, citaTattoDTO);

                return existingCitaTatto;
            })
            .map(citaTattoRepository::save)
            .map(citaTattoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaTattoDTO> findAll() {
        log.debug("Request to get all CitaTattos");

        Query q = entityManager.createQuery(Constants.ORDENAR_CITAS_PORfECHA);

        List<CitaTattoDTO> citas = q.getResultList();

        return citas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaTattoDTO> citaDia() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fechaHoy = format.format(new Date());

        List<CitaTatto> citaDia = citaTattoRepository.citaDia(fechaHoy);

        return citaTattoMapper.toDto(citaDia);
    }

    @Override
    public List<CitaTattoDTO> citasPorFiltro(CitaTattoDTO citaTatto) {
        log.debug("Request to get all citas per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<>();

        sb.append(Constants.CITA_TATTO_BASE);
        if (citaTatto.getInfoCliente() != null && !citaTatto.getInfoCliente().isEmpty()) {
            sb.append(Constants.CITA_TATTO_NOMBRE);
            filtros.put("nombre", "%" + citaTatto.getInfoCliente().toUpperCase() + "%");
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List<CitaTatto> citas = q.getResultList();

        return citaTattoMapper.toDto(citas);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaTattoDTO> findOne(Long id) {
        log.debug("Request to get CitaTatto : {}", id);

        return citaTattoRepository.findById(id).map(citaTattoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitaTatto : {}", id);

        Query q = entityManager.createQuery("DELETE FROM Abono WHERE idCita = :idCita").setParameter("idCita", id);

        q.executeUpdate();

        citaTattoRepository.deleteById(id);
    }
}
