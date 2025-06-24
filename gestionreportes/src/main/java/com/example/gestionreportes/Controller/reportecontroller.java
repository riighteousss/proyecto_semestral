package com.example.gestionreportes.Controller;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Service.reporteservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1")
public class reportecontroller {
    @Autowired
    private reporteservice Reporteservice;
 //endponit para consultar todos los reportes
   @Operation(summary = "Obtener todos los reportes", description = "Obtiene una lista de todos los reportes disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay reportes disponibles")
    })
    @GetMapping("/reportes")
    public ResponseEntity<List<reporte>> buscarreporte(){
        List<reporte> reporte = Reporteservice.buscarReportes();
        //si la lista esta vacia
        if(reporte.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }
   //endpoint para buscar reporte por id
   @Operation(summary = "Buscar reporte por ID", description = "Obtiene un reporte específico a partir de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte no existente")
    })
   @GetMapping("/reportes/{id}")
   public ResponseEntity<reporte> buscarporid(@PathVariable Long id){
         try {
            reporte reportes = Reporteservice.buscarreporteporid(id);
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

   } 
    //endpoint para crea nuevo reporte
    @Operation(summary = "Crear nuevo reporte", description = "Crea un nuevo reporte con la información proporcionada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reporte creado correctamente"),
        @ApiResponse(responseCode = "404", description = "Error al crear el reporte (usuario no encontrado o datos inválidos)")
    })
    @PostMapping("/reportes")
    public ResponseEntity<?> crearreporte(@RequestBody reporte reporte){
    try {
        reporte newreporte = Reporteservice.crearReporte(
            reporte.getTiporeporte(),
            reporte.getDescripciongeneral(),
            reporte.getIdusuario()
            
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newreporte);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    //endpoint para eliminar reporte
    @Operation(summary = "Eliminar reporte por ID", description = "Elimina un reporte específico a partir de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<?> eliminarreporte(@PathVariable Long id){
        try{
           String mensaje = Reporteservice.eliminarreporte(id);
          return ResponseEntity.ok(mensaje);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }

    }
    //endpoint para buscar todos los reportes de un usuario
     @Operation(summary = "Buscar reportes por ID de usuario", description = "Obtiene todos los reportes asociados a un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes del usuario obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron reportes para el usuario ")
    })
    @GetMapping("/reportes/usuario/{id}")
     public ResponseEntity<?> buscarporidusuario(@PathVariable Long id){
        try {
         List<reporte> reporte = Reporteservice.buscarporidusuario(id);

        return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
