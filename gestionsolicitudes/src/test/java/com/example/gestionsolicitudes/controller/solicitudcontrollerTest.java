package com.example.gestionsolicitudes.controller;

import com.example.gestionsolicitudes.model.solicitud;
import com.example.gestionsolicitudes.services.solicitudservice;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(solicitudcontroller.class)
public class solicitudcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private solicitudservice solicitudService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testObtenerSolicitudes_conSolicitudes() throws Exception {
        List<solicitud> lista = Arrays.asList(new solicitud(), new solicitud());

        when(solicitudService.buscarsolicitudes()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testObtenerSolicitudes_sinSolicitudes() throws Exception {
        when(solicitudService.buscarsolicitudes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/solicitudes"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testBuscarSolicitudesPorUsuario_conResultados() throws Exception {
        List<solicitud> lista = Arrays.asList(new solicitud());

        when(solicitudService.buscarporidusuario(1L)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testBuscarSolicitudesPorUsuario_sinResultados() throws Exception {
        when(solicitudService.buscarporidusuario(1L))
                .thenThrow(new RuntimeException("No se encontraron solicitudes"));

        mockMvc.perform(get("/api/v1/solicitudes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron solicitudes"));
    }

   @Test
public void testCrearSolicitud_valido() throws Exception {
    solicitud nueva = new solicitud();
    nueva.setTiposolicitud("tipo");
    nueva.setDescripciongeneral("desc");
    nueva.setIdusuario(1L);
    nueva.setIdequipo(2L);

    solicitud creada = new solicitud();
    creada.setId(1L);
    creada.setTiposolicitud("tipo");
    creada.setDescripciongeneral("desc");
    creada.setIdusuario(1L);
    creada.setIdequipo(2L);

    String token = "token-de-prueba";

    when(solicitudService.crearsolicitud("tipo", "desc", 1L, 2L, token)).thenReturn(creada);

    mockMvc.perform(post("/api/v1/solicitudes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token)
                    .content(objectMapper.writeValueAsString(nueva)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));
}

@Test
public void testCrearSolicitud_error() throws Exception {
    solicitud nueva = new solicitud();
    nueva.setTiposolicitud("tipo");
    nueva.setDescripciongeneral("desc");
    nueva.setIdusuario(1L);
    nueva.setIdequipo(2L);

    String token = "token-de-prueba";

    when(solicitudService.crearsolicitud("tipo", "desc", 1L, 2L, token))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

    mockMvc.perform(post("/api/v1/solicitudes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token)
                    .content(objectMapper.writeValueAsString(nueva)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Usuario no encontrado"));
}
    @Test
    public void testActualizarSolicitud_valido() throws Exception {
        solicitud actualizada = new solicitud();
        actualizada.setTiposolicitud("nuevo");
        actualizada.setDescripciongeneral("actualizado");

        when(solicitudService.actualizarporusuario(1L, 1L, "nuevo", "actualizado"))
                .thenReturn(actualizada);

        String json = """
            {
                "tiposolicitud": "nuevo",
                "descripciongeneral": "actualizado"
            }
            """;

        mockMvc.perform(put("/api/v1/solicitudes/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tiposolicitud").value("nuevo"));
    }

    @Test
    public void testActualizarSolicitud_error() throws Exception {
        when(solicitudService.actualizarporusuario(1L, 1L, "nuevo", "actualizado"))
                .thenThrow(new RuntimeException("Solicitud no válida"));

        String json = """
            {
                "tiposolicitud": "nuevo",
                "descripciongeneral": "actualizado"
            }
            """;

        mockMvc.perform(put("/api/v1/solicitudes/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Solicitud no válida"));
    }

    @Test
    public void testEliminarSolicitud_existe() throws Exception {
        when(solicitudService.eliminarporid(1L)).thenReturn("Solicitud eliminada");

        mockMvc.perform(delete("/api/v1/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Solicitud eliminada"));
    }

    @Test
    public void testEliminarSolicitud_noExiste() throws Exception {
        when(solicitudService.eliminarporid(1L))
                .thenThrow(new RuntimeException("Solicitud no encontrada"));

        mockMvc.perform(delete("/api/v1/solicitudes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Solicitud no encontrada"));
    }
}
