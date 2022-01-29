package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentasMapperTest {

    private VentasMapper ventasMapper;

    @BeforeEach
    public void setUp() {
        ventasMapper = new VentasMapperImpl();
    }
}
