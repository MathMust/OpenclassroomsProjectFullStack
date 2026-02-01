package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
    }

    public void checkEmailNotUsed(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    public void checkNameNotUsed(String name) {
        if (userRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Name already exists");
        }
    }

    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public User getByEmailOrName(String identifier) {
        return userRepository.findByEmailOrName(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException(Constants.INVALID_CREDENTIALS));
    }
}
