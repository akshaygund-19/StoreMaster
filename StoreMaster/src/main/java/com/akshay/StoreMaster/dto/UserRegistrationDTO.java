package com.akshay.StoreMaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserRegistrationDTO {
    private String name;

    @Email
    private String email;
    @NotNull
    @Size(min = 8)
    private String password;
}
