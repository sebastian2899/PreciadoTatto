package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComprasMapperTest {

    private ComprasMapper comprasMapper;

    @BeforeEach
    public void setUp() {
        comprasMapper = new ComprasMapperImpl();
    }
}
