package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CajaTattosMapperTest {

    private CajaTattosMapper cajaTattosMapper;

    @BeforeEach
    public void setUp() {
        cajaTattosMapper = new CajaTattosMapperImpl();
    }
}
