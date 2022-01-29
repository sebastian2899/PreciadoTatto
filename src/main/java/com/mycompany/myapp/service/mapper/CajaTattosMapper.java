package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CajaTattosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CajaTattos} and its DTO {@link CajaTattosDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CajaTattosMapper extends EntityMapper<CajaTattosDTO, CajaTattos> {}
