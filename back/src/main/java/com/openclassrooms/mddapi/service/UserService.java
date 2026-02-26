package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service métier responsable de la gestion des utilisateurs.
 *
 * <p>
 * Gère :
 * <ul>
 *     <li>La récupération des utilisateurs par email ou nom</li>
 *     <li>La vérification de l’unicité du nom et de l’email</li>
 *     <li>La création ou mise à jour d’un utilisateur</li>
 * </ul>
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructeur avec injection du repository utilisateur.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email email de l’utilisateur
     * @return {@link User} correspondant
     * @throws RuntimeException si l’utilisateur n’existe pas
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
    }

    /**
     * Vérifie que l’email n’est pas déjà utilisé.
     *
     * @param email email à vérifier
     * @throws ResponseStatusException si l’email est déjà utilisé
     */
    public void checkEmailNotUsed(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    Constants.EMAIL + Constants.COLON_SEPARATOR + Constants.EMAIL_USED
            );
        }
    }

    /**
     * Vérifie que le nom d’utilisateur n’est pas déjà utilisé.
     *
     * @param name nom à vérifier
     * @throws ResponseStatusException si le nom est déjà utilisé
     */
    public void checkNameNotUsed(String name) {
        if (userRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    Constants.NAME + Constants.COLON_SEPARATOR + Constants.NAME_USED
            );
        }
    }

    /**
     * Crée ou met à jour un utilisateur.
     *
     * @param user utilisateur à créer ou mettre à jour
     */
    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Récupère un utilisateur par email ou nom.
     *
     * @param identifier email ou nom
     * @return {@link User} correspondant
     * @throws IllegalArgumentException si aucun utilisateur ne correspond
     */
    public User getByEmailOrName(String identifier) {
        return userRepository.findByEmailOrName(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException(Constants.INVALID_CREDENTIALS));
    }
}
