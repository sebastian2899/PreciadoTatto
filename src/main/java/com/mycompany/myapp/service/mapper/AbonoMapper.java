package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AbonoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Abono} and its DTO {@link AbonoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AbonoMapper extends EntityMapper<AbonoDTO, Abono> {}
