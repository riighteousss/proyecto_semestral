package com.example.gestionseguimiento.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class solicitudclient {

    private final WebClient webClient;

    public solicitudclient(@Value("${solicitud-service.url}") String solicitudservidor) {
        this.webClient = WebClient.builder().baseUrl(solicitudservidor).build();
    }

    // Devuelve un Map que representa el JSON recibido
    public Map<String, Object> obtenersolicitudporid(Long id) {
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Usuario no encontrado")))
                .bodyToMono(Map.class)
                .block(); // bloquear para esperar la respuesta
    }
}