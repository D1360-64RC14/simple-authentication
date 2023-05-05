package com.example.simpleauthentication.services;

import com.example.simpleauthentication.models.UserModel;
import com.example.simpleauthentication.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public UserModel save(UserModel user) {
        return userRepo.save(user);
    }

    @Transactional
    public void deleteById(UUID id) {
        userRepo.deleteById(id);
    }

    public Optional<UserModel> findById(UUID id) {
        return userRepo.findById(id);
    }
}
