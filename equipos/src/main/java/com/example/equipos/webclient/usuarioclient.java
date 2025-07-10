package com.example.equipos.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class usuarioclient {

    private final WebClient webClient;

    public usuarioclient(@Value("${usuario-service.url}") String usuarioServidor) {
        this.webClient = WebClient.builder()
                .baseUrl(usuarioServidor)
                .build();
    }

    // Método para obtener un usuario por id enviando token JWT para autenticación
    public Map<String, Object> obtenerUsuarioPorId(Long id, String token) {
        return this.webClient.get()
                .uri("/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))  // Aquí se agrega el token JWT
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Usuario no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }

}