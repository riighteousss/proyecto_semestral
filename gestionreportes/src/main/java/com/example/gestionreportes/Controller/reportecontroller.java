package com.example.gestionreportes.Controller;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Service.reporteservice;

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
    @GetMapping("/reportes/{id}")
     public ResponseEntity<?> buscarporidusuario(@PathVariable Long id){
        try {
         List<reporte> reporte = Reporteservice.buscarporidusuario(id);

        return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
