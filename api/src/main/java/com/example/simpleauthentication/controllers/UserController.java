package com.example.simpleauthentication.controllers;

import com.example.simpleauthentication.dtos.InformationUserDTO;
import com.example.simpleauthentication.dtos.RegistrationUserDTO;
import com.example.simpleauthentication.models.UserModel;
import com.example.simpleauthentication.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUser(
            @PathVariable String id
    ) {
        var userUUID = UUID.fromString(id);
        var maybeUser = service.findById(userUUID);

        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var user = maybeUser.get();

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<InformationUserDTO> registerUser(
            @RequestBody @Valid RegistrationUserDTO registrationUser,
            HttpServletRequest request
    ) {
        var user = new UserModel();
        BeanUtils.copyProperties(registrationUser, user);

        var hashedPassword = hashPassword(registrationUser.getPassword());
        user.setPasswordHash(hashedPassword);

        var savedUser = service.save(user);

        var informationUser = new InformationUserDTO();
        BeanUtils.copyProperties(savedUser, informationUser);

        var getUri = generateGetUri(request.getRequestURI(), savedUser.getId());

        return ResponseEntity.created(getUri).body(informationUser);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private URI generateGetUri(String stringUri, UUID id) {
        return generateGetUri(URI.create(stringUri), id);
    }
    private URI generateGetUri(URI uri, UUID id) {
        var path = uri.getPath();

        var splitPath = new ArrayList<>(Arrays.stream(path.split("/")).toList());
        splitPath.add(id.toString());

        var pathWithId = String.join("/", splitPath);

        var uriBuilder = UriComponentsBuilder.fromUri(uri);
        uriBuilder.path(pathWithId);

        return uriBuilder.build().toUri();
    }
}
