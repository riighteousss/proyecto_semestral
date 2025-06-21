package com.example.gestiontecnicos.controller;

import java.util.List;
import java.util.Optional;

import com.example.gestiontecnicos.model.tecnicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestiontecnicos.service.ServiceTecnicos;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class tecnicocontroller {
    @Autowired
    private ServiceTecnicos serviceTecnicos;

    // Endpoint para consultar todos los técnicos
    @GetMapping("/tecnicos")
    public ResponseEntity<List<tecnicos>> obtenerTecnicos(){
        List<tecnicos> tecnico = serviceTecnicos.buscarTecnicos();
        return tecnico.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(tecnico);
    }

    // Endpoint para obtener técnico por ID
    @GetMapping("/tecnicos/{id}")
    public ResponseEntity<?> obtenerTecnicoPorId(@PathVariable Long id) {
        try {
            Optional<tecnicos> tecnico = serviceTecnicos.buscarPorId(id);
            return tecnico.isPresent() ? 
                ResponseEntity.ok(tecnico.get()) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Endpoint para obtener técnico por RUT
    @GetMapping("/tecnicos/rut/{rut}")
    public ResponseEntity<?> obtenerTecnicoPorRut(@PathVariable String rut) {
        try {
            Optional<tecnicos> tecnico = serviceTecnicos.buscarPorRut(rut);
            return tecnico.isPresent() ? 
                ResponseEntity.ok(tecnico.get()) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Endpoint para crear un nuevo técnico
    @PostMapping("/tecnicos")
    public ResponseEntity<?> crearTecnico(@RequestBody tecnicos tecnico){
        try {
            tecnicos nuevoTecnico = serviceTecnicos.agregarTecnico(
                tecnico.getRut(),
                tecnico.getNombre(),
                tecnico.getEspecialidad()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTecnico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para modificar técnico completo
    @PutMapping("/tecnicos/{id}")
    public ResponseEntity<?> modificarTecnico(
            @PathVariable Long id,
            @RequestBody tecnicos tecnicoData) {
        try {
            tecnicos tecnicoActualizado = serviceTecnicos.modificarTecnico(
                id,
                tecnicoData.getRut(),
                tecnicoData.getNombre(),
                tecnicoData.getEspecialidad()
            );
            return ResponseEntity.ok(tecnicoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para actualizar solo la especialidad de un técnico
    @PutMapping("/tecnicos/{id}/especialidad")
    public ResponseEntity<?> actualizarEspecialidad(
            @PathVariable Long id,
            @RequestParam String especialidad) {
        try {
            tecnicos tecnicoActualizado = serviceTecnicos.actualizarEspecialidad(id, especialidad);
            return ResponseEntity.ok(tecnicoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para eliminar técnico
    @DeleteMapping("/tecnicos/{id}")
    public ResponseEntity<?> eliminarTecnico(@PathVariable Long id) {
        try {
            serviceTecnicos.eliminarTecnico(id);
            return ResponseEntity.ok().body("Técnico eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoints adicionales para búsquedas especializadas

    // Endpoint para buscar técnicos por especialidad
    @GetMapping("/tecnicos/especialidad/{especialidad}")
    public ResponseEntity<List<tecnicos>> obtenerTecnicosPorEspecialidad(@PathVariable String especialidad) {
        List<tecnicos> tecnicos = serviceTecnicos.buscarPorEspecialidad(especialidad);
        return tecnicos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(tecnicos);
    }

    // Endpoint para buscar técnicos por nombre (búsqueda parcial)
    @GetMapping("/tecnicos/buscar")
    public ResponseEntity<List<tecnicos>> buscarTecnicosPorNombre(@RequestParam String nombre) {
        List<tecnicos> tecnicos = serviceTecnicos.buscarPorNombre(nombre);
        return tecnicos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(tecnicos);
    }

    // Endpoint para obtener todas las especialidades disponibles
    @GetMapping("/tecnicos/especialidades")
    public ResponseEntity<List<String>> obtenerEspecialidades() {
        List<String> especialidades = serviceTecnicos.obtenerEspecialidades();
        return especialidades.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(especialidades);
    }

    // Endpoint para contar técnicos por especialidad
    @GetMapping("/tecnicos/especialidad/{especialidad}/count")
    public ResponseEntity<Long> contarTecnicosPorEspecialidad(@PathVariable String especialidad) {
        long count = serviceTecnicos.contarPorEspecialidad(especialidad);
        return ResponseEntity.ok(count);
    }
}