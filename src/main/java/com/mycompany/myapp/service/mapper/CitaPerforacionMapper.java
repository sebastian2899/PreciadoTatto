package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CitaPerforacion} and its DTO {@link CitaPerforacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CitaPerforacionMapper extends EntityMapper<CitaPerforacionDTO, CitaPerforacion> {}
