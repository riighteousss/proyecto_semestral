package com.example.equipos.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.equipos.repository.EquipoRepository;

@ExtendWith(MockitoExtension.class)
public class equiposerviceTest {
    @Mock
    private EquipoRepository equiporepository;

    @InjectMocks
    private EquipoService service;

    

}
