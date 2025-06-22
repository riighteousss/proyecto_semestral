package com.example.gestionsolicitudes.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class equipoclient {
    private final WebClient webclient;
    
    public equipoclient(@Value("${equipo-service.url}") String equipoServidor){
        this.webclient = WebClient.builder().baseUrl(equipoServidor).build();
    }

     public Map<String, Object> obtenerequipoid(Long id){
        //realizar una consulta HTTP de tipo get al microservicio equipos
        return this.webclient.get()
        .uri("/{id}", id)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(),
         response -> response.bodyToMono(String.class)
         .map(body -> new RuntimeException("equipo no encontrado"))).bodyToMono(Map.class).block();   

    }
}
