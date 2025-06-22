package com.example.equipos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.equipos.model.equipo;
import com.example.equipos.repository.EquipoRepository;

@ExtendWith(MockitoExtension.class)
public class equiposerviceTest {
    @Mock
    private EquipoRepository equiporepository;

    @InjectMocks
    private EquipoService service;

    @Test
    public void testBuscarquipos_RetornaLista() {
        List<equipo> listaMock = Arrays.asList(
            new equipo(1L, 100L, "Laptop", "Dell", "XPS"),
            new equipo(2L, 101L, "Celular", "Samsung", "S21")
        );

        when(equiporepository.findAll()).thenReturn(listaMock);

        List<equipo> resultado = service.buscarquipos();

        assertEquals(2, resultado.size());
        verify(equiporepository, times(1)).findAll();
    }
   
}
