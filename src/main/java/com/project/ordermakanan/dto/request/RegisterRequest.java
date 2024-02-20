package com.project.ordermakanan.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "{username.required}")
    private String username;

    @NotEmpty(message = "{fullname.required}")
    private String fullname;

    @NotEmpty(message = "{password.required}")
    @Size(min = 6, message = "{password.length}")
    private String password;

    @NotEmpty(message = "{retype.password.required}")
    private String retypePassword;
}