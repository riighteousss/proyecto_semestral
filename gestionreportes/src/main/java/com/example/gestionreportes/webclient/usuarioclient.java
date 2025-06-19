package com.example.gestionreportes.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class usuarioclient {
    private final WebClient webclient;
    
    public usuarioclient(@Value("${usuario-service.url}") String usuarioServidor){
        this.webclient = WebClient.builder().baseUrl(usuarioServidor).build();
    }

    //metodo para pder comunicarme con el microservicio
    //usuario y verificar si el usuario existe
    //devoler una estructura de mapa que representa el json
    public Map<String, Object> obtenerusuarioid(Long id){
        //realizar una consulta HTTP de tipo get al microservicio usuarios
        return this.webclient.get()
        .uri("/{id}", id)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(),
         response -> response.bodyToMono(String.class)
         .map(body -> new RuntimeException("usuario no encontrado"))).bodyToMono(Map.class).block();   

    }
    
}
