package ru.itis.blog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.blog.dto.SignInDto;
import ru.itis.blog.dto.SignUpDto;
import ru.itis.blog.dto.TokenDto;
import ru.itis.blog.service.SignInService;

import java.nio.file.AccessDeniedException;

@RestController
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInDto signInDto) throws AccessDeniedException {
        return ResponseEntity.ok(signInService.signIn(signInDto));
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody SignUpDto signUpDto) {

        signInService.signUp(signUpDto);

        return ResponseEntity.ok("200");

    }

}
