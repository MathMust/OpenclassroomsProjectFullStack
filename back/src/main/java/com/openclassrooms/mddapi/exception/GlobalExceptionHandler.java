package com.openclassrooms.mddapi.exception;

import com.openclassrooms.mddapi.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions de l’application.
 *
 * <p>
 * Centralise le traitement des erreurs afin de :
 * <ul>
 *     <li>Standardiser les réponses JSON via {@link ErrorResponse}</li>
 *     <li>Retourner des codes HTTP cohérents</li>
 *     <li>Journaliser les erreurs selon leur niveau de criticité</li>
 * </ul>
 * </p>
 *
 * <p>
 * Intercepte les exceptions métier, de validation, d’authentification,
 * d’autorisation et les erreurs techniques non prévues.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Logger applicatif. */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les erreurs métier simples.
     *
     * @param ex exception levée
     * @return réponse HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        logger.debug("Handled IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Gère les exceptions contenant déjà un statut HTTP.
     *
     * @param ex exception Spring avec statut intégré
     * @return réponse HTTP correspondante
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        logger.debug("Handled ResponseStatusException: {}", ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(ex.getReason()));
    }

    /**
     * Gère les erreurs de validation liées à {@code @Valid}.
     *
     * @param ex exception de validation
     * @return réponse HTTP 400 avec détail des champs invalides
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        String message = String.join(", ", errors);
        logger.debug("Validation failed: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    /**
     * Gère les erreurs liées aux opérations IO (upload, fichiers).
     *
     * @param ex exception IO
     * @return réponse HTTP 500
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        logger.error("IO Exception: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("IO error: " + ex.getMessage()));
    }

    /**
     * Gère les erreurs d’authentification (JWT invalide, credentials incorrects).
     *
     * @param ex exception d’authentification
     * @return réponse HTTP 401
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        logger.debug("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication failed: " + ex.getMessage()));
    }

    /**
     * Gère les erreurs d’autorisation (accès interdit).
     *
     * @param ex exception d’accès refusé
     * @return réponse HTTP 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        logger.debug("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Access denied: " + ex.getMessage()));
    }

    /**
     * Gestion générique pour toute exception non prévue.
     *
     * @param ex exception inattendue
     * @return réponse HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected error: " + ex.getMessage()));
    }
}
