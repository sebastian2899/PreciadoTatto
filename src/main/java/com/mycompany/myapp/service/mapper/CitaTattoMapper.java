package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CitaTatto} and its DTO {@link CitaTattoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CitaTattoMapper extends EntityMapper<CitaTattoDTO, CitaTatto> {}
