package com.openclassrooms.mddapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Implémentation personnalisée de {@link AuthenticationEntryPoint}.
 *
 * <p>
 * Cette classe est utilisée par Spring Security pour gérer les tentatives
 * d'accès non authentifiées aux ressources sécurisées de l'application.
 * </p>
 *
 * <p>
 * Lorsqu'un utilisateur tente d'accéder à une ressource protégée sans être
 * authentifié (ou avec un token invalide/expiré), cette classe intercepte
 * la requête et renvoie une réponse HTTP 401 (Unauthorized) au format JSON.
 * </p>
 *
 * <p>
 * La réponse contient un objet {@link ErrorResponse} sérialisé grâce à
 * {@link ObjectMapper}.
 * </p>
 *
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Instance utilisée pour sérialiser les objets Java en JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Méthode appelée automatiquement lorsqu'une requête non authentifiée
     * tente d'accéder à une ressource protégée.
     *
     * <p>
     * Définit le statut HTTP à {@code 401 Unauthorized} et retourne
     * un message d'erreur au format JSON.
     * </p>
     *
     * @param request       la requête HTTP ayant déclenché l'exception
     * @param response      la réponse HTTP à modifier
     * @param authException l'exception d'authentification levée
     * @throws IOException en cas d'erreur lors de l'écriture de la réponse
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse error = new ErrorResponse("Authentication required or token invalid");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
