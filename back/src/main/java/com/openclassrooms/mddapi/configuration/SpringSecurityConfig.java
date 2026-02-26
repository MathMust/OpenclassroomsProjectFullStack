package com.openclassrooms.mddapi.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration centrale de Spring Security.
 *
 * <p>
 * Configure :
 * <ul>
 *     <li>La sécurité HTTP (stateless + JWT)</li>
 *     <li>Les endpoints publics et protégés</li>
 *     <li>La gestion des erreurs d’authentification</li>
 *     <li>L’encodage des mots de passe (BCrypt)</li>
 *     <li>L’encodage/décodage des JWT (HS256)</li>
 *     <li>La gestion des rôles via le claim "roles"</li>
 * </ul>
 * </p>
 *
 * <p>
 * Application basée sur une authentification JWT sans session serveur.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig implements WebMvcConfigurer {

    /** Clé secrète utilisée pour signer et vérifier les JWT. */
    @Value("${jwt.secret}")
    private String jwtKey;

    /** Point d’entrée personnalisé pour les accès non authentifiés. */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Constructeur avec injection du point d’entrée d’authentification.
     *
     * @param authenticationEntryPoint gestionnaire des erreurs 401
     */
    public SpringSecurityConfig(CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /**
     * Configure la chaîne de filtres Spring Security :
     * <ul>
     *     <li>Désactivation CSRF</li>
     *     <li>Session stateless</li>
     *     <li>Endpoints publics : /api/auth/register, /api/auth/login</li>
     *     <li>Protection de toutes les autres routes</li>
     *     <li>Support JWT en tant que Resource Server</li>
     * </ul>
     *
     * @param http configuration HTTP
     * @return SecurityFilterChain configurée
     * @throws Exception en cas d’erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Bean responsable de la signature des JWT.
     *
     * @return encodeur JWT basé sur une clé secrète HS256
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    /**
     * Bean responsable de la validation des JWT.
     *
     * @return décodeur JWT configuré en HS256
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Encodeur de mots de passe basé sur BCrypt.
     *
     * @return instance BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la conversion des claims JWT en autorités Spring Security.
     *
     * <p>Utilise le claim "roles" avec le préfixe "ROLE_".</p>
     *
     * @return convertisseur JWT personnalisé
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
