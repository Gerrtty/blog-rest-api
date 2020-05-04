package ru.itis.blog.service;


import ru.itis.blog.dto.SignInDto;
import ru.itis.blog.dto.SignUpDto;
import ru.itis.blog.dto.TokenDto;
import ru.itis.blog.models.User;

import java.nio.file.AccessDeniedException;

public interface SignInService {

    TokenDto signIn(SignInDto signInData) throws AccessDeniedException;

    User signUp(SignUpDto signUpDto);

}

