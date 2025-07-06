package com.example.gestiontecnicos.controller;

import com.example.gestiontecnicos.model.tecnicos;
import com.example.gestiontecnicos.service.ServiceTecnicos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(tecnicocontroller.class)
public class tecnicocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceTecnicos serviceTecnicos;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testObtenerTecnicos_conTecnicos() throws Exception {
        List<tecnicos> lista = Arrays.asList(new tecnicos(), new tecnicos());

        when(serviceTecnicos.BuscarTecnicos()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/tecnicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testObtenerTecnicos_sinTecnicos() throws Exception {
        when(serviceTecnicos.BuscarTecnicos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/tecnicos"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testBuscarTecnicoPorId_existente() throws Exception {
        tecnicos tecnico = new tecnicos();
        tecnico.setId(1L);

        when(serviceTecnicos.buscarporid(1L)).thenReturn(tecnico);

        mockMvc.perform(get("/api/v1/tecnicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testBuscarTecnicoPorId_noExiste() throws Exception {
        when(serviceTecnicos.buscarporid(1L)).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(get("/api/v1/tecnicos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearTecnico_valido() throws Exception {
        tecnicos nuevo = new tecnicos();
        nuevo.setId(1L);
        nuevo.setRut("12345678-9");
        nuevo.setNombre("Juan Perez");
        nuevo.setEspecialidad("Redes");

        when(serviceTecnicos.AgregarTecnico("12345678-9", "Juan Perez", "Redes")).thenReturn(nuevo);

        mockMvc.perform(post("/api/v1/tecnicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }
@Test
public void testCrearTecnico_conflicto() throws Exception {
    tecnicos nuevo = new tecnicos();
    nuevo.setRut("12345678-9");
    nuevo.setNombre("Juan Perez");
    nuevo.setEspecialidad("Redes");

    when(serviceTecnicos.AgregarTecnico(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("El técnico ya existe"));

    mockMvc.perform(post("/api/v1/tecnicos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(nuevo)))
            .andExpect(status().isConflict()) 
            .andExpect(content().string("El técnico ya existe"));
}
    @Test
    public void testActualizarEspecialidad_valido() throws Exception {
        tecnicos actualizado = new tecnicos();
        actualizado.setEspecialidad("Soporte");

        when(serviceTecnicos.actualizarEspecializacion(1L, "Soporte")).thenReturn(actualizado);

        String json = objectMapper.writeValueAsString(Map.of("especialidad", "Soporte"));

        mockMvc.perform(put("/api/v1/tecnicos/1/especialidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especialidad").value("Soporte"));
    }

    @Test
    public void testActualizarEstado_valido() throws Exception {
        tecnicos actualizado = new tecnicos();
        actualizado.setEstado(false);

        when(serviceTecnicos.actualizarestado(1L, false)).thenReturn(actualizado);

        String json = objectMapper.writeValueAsString(Map.of("estado", false));

        mockMvc.perform(put("/api/v1/tecnicos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    public void testActualizarEstado_error() throws Exception {
        when(serviceTecnicos.actualizarestado(1L, false))
                .thenThrow(new RuntimeException("Técnico no encontrado"));

        String json = objectMapper.writeValueAsString(Map.of("estado", false));

        mockMvc.perform(put("/api/v1/tecnicos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Técnico no encontrado"));
    }
}