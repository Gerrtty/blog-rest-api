package ru.itis.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInDto {

    private String email;
    private String password;

}

