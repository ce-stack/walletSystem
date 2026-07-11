package com.system.wallet.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {



    @NotBlank(message = "the full name is required")
    @Size(max = 30)
    private String full_name;


    @Email(message = "invalid email format")
    @NotBlank(message = "the email is required")
    @Size(max = 30)
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6 , message = "password is must be at least 6 characters")
    private String password;


    public RegisterRequest() {

    }

    public RegisterRequest(String full_name, String email, String password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
