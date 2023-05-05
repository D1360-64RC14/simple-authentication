package com.example.simpleauthentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.str.Password;

@Data @AllArgsConstructor @NoArgsConstructor
public class RegistrationUserDTO {
    @Email @NotBlank @Size(max = 254)
    private String email;

    @NotBlank @Size(max = 50)
    private String firstName;

    @NotBlank @Size(max = 50)
    private String lastName;

    @NotBlank @Size(min = 8, max = 74) // https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html#input-limits
    @Password(allowSpace = true, maxSize = 74)
    private String password;
}