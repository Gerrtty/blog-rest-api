package ru.itis.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDto {

    private String email;
    private String password;

}