package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service métier responsable de l’authentification et de la gestion
 * du compte utilisateur.
 *
 * <p>
 * Gère :
 * <ul>
 *     <li>La génération des JWT</li>
 *     <li>L’inscription et la connexion</li>
 *     <li>La récupération du profil utilisateur</li>
 *     <li>La mise à jour du compte</li>
 * </ul>
 * </p>
 *
 * <p>
 * Authentification basée sur JWT signé en HS256 avec expiration à 24h.
 * </p>
 */
@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TopicService topicService;

    /**
     * Constructeur avec injection des dépendances.
     */
    public AuthService(JwtEncoder jwtEncoder, UserService userService, BCryptPasswordEncoder passwordEncoder, TopicService topicService) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.topicService = topicService;
    }

    /**
     * Génère un JWT pour l’utilisateur authentifié.
     *
     * @param authentication contexte d’authentification
     * @return token JWT signé
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    /**
     * Inscrit un nouvel utilisateur et retourne un JWT.
     *
     * @param request données d’inscription
     * @return token JWT
     */
    public String register(RegisterRequest request) {
        userService.checkNameNotUsed(request.getName());
        userService.checkEmailNotUsed(request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userService.createOrUpdateUser(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        return generateToken(authentication);
    }

    /**
     * Authentifie un utilisateur existant et retourne un JWT.
     *
     * @param request données de connexion
     * @return token JWT
     */
    public String login(LoginRequest request) {
        User user = userService.getByEmailOrName(request.getIdentifier());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    Constants.INVALID_CREDENTIALS
            );
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return generateToken(authentication);
    }

    /**
     * Retourne les informations du profil utilisateur authentifié.
     *
     * @param authentication contexte d’authentification courant
     * @return données utilisateur enrichies (topics inclus)
     */
    public UserResponse me(Authentication authentication) {

        User user = userService.getByEmail(authentication.getName());

        List<Integer> topicIds = user.getSubscriptions().stream()
                .map(sub -> sub.getTopic().getId())
                .toList();

        List<TopicDto> topicDtos = topicService.getByIdAndUserId(topicIds, user.getId());

        UserResponse userResponse = new UserResponse(user);
        userResponse.setTopics(topicDtos);

        return userResponse;
    }

    /**
     * Met à jour les informations du compte utilisateur et génère un nouveau JWT.
     *
     * @param request nouvelles données utilisateur
     * @param authentication utilisateur authentifié
     * @return nouveau token JWT
     */
    public String update(RegisterRequest request, Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());

        if (!Objects.equals(request.getEmail(), user.getEmail())) {
            userService.checkEmailNotUsed(request.getEmail());
        }
        if (!Objects.equals(request.getName(), user.getName())) {
            userService.checkNameNotUsed(request.getName());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.createOrUpdateUser(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );

         authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        return generateToken(authentication);
    }

}
