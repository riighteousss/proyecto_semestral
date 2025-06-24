package test.java.com.example.gestiontecnicos.service;

import com.example.gestiontecnicos.model.tecnicos;
import com.example.gestiontecnicos.repository.RepositoryTecnicos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTecnicosTest {

    @Mock
    private RepositoryTecnicos repositoryTecnicos;

    @InjectMocks
    private ServiceTecnicos serviceTecnicos;

    private tecnicos tecnicoEjemplo;

    @BeforeEach
    public void setUp() {
        tecnicoEjemplo = new tecnicos();
        tecnicoEjemplo.setId(1L);
        tecnicoEjemplo.setRut("12345678-9");
        tecnicoEjemplo.setNombre("Juan Pérez");
        tecnicoEjemplo.setEspecialidad("Hardware");
    }

    @Test
    public void testBuscarTecnicos() {
        List<tecnicos> lista = Arrays.asList(tecnicoEjemplo);

        when(repositoryTecnicos.findAll()).thenReturn(lista);

        List<tecnicos> resultado = serviceTecnicos.BuscarTecnicos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repositoryTecnicos, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId_existente() {
        when(repositoryTecnicos.findById(1L)).thenReturn(Optional.of(tecnicoEjemplo));

        tecnicos resultado = serviceTecnicos.buscarporid(1L);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(repositoryTecnicos, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorId_noExiste() {
        when(repositoryTecnicos.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceTecnicos.buscarporid(1L);
        });

        assertEquals("el tecnico de ID:1 no existe", exception.getMessage());
        verify(repositoryTecnicos, times(1)).findById(1L);
    }

    @Test
    public void testAgregarTecnico_nuevo() {
        when(repositoryTecnicos.findByRut("12345678-9")).thenReturn(Optional.empty());
        when(repositoryTecnicos.save(any(tecnicos.class))).thenReturn(tecnicoEjemplo);

        tecnicos resultado = serviceTecnicos.AgregarTecnico("12345678-9", "Juan Pérez", "Hardware");

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(repositoryTecnicos, times(1)).findByRut("12345678-9");
        verify(repositoryTecnicos, times(1)).save(any(tecnicos.class));
    }

    @Test
    public void testAgregarTecnico_yaExiste() {
        when(repositoryTecnicos.findByRut("12345678-9")).thenReturn(Optional.of(tecnicoEjemplo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceTecnicos.AgregarTecnico("12345678-9", "Otro Nombre", "Redes");
        });

        assertEquals("El técnico con RUT 12345678-9 ya existe.", exception.getMessage());
        verify(repositoryTecnicos, times(1)).findByRut("12345678-9");
        verify(repositoryTecnicos, never()).save(any());
    }

    @Test
    public void testActualizarEspecializacion_existente() {
        when(repositoryTecnicos.findById(1L)).thenReturn(Optional.of(tecnicoEjemplo));
        when(repositoryTecnicos.save(any(tecnicos.class))).thenReturn(tecnicoEjemplo);

        tecnicos resultado = serviceTecnicos.actualizarEspecializacion(1L, "Redes");

        assertNotNull(resultado);
        assertEquals("Redes", resultado.getEspecialidad());
        verify(repositoryTecnicos, times(1)).findById(1L);
        verify(repositoryTecnicos, times(1)).save(any(tecnicos.class));
    }

    @Test
    public void testActualizarEspecializacion_noExiste() {
        when(repositoryTecnicos.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceTecnicos.actualizarEspecializacion(1L, "Redes");
        });

        assertEquals("tecnico de ID: 1 no encontrado ", exception.getMessage());
        verify(repositoryTecnicos, times(1)).findById(1L);
        verify(repositoryTecnicos, never()).save(any());
    }
}
