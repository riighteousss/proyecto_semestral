package com.example.gestionreportes.Controller;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Service.reporteservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

}
