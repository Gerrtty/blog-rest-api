package ru.itis.blog.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.blog.dto.SignInDto;
import ru.itis.blog.dto.SignUpDto;
import ru.itis.blog.dto.TokenDto;
import ru.itis.blog.models.Role;
import ru.itis.blog.models.User;
import ru.itis.blog.repository.UsersRepository;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {

    @Value("${jwt.secret}")
    private String secret;

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public SignInServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenDto signIn(SignInDto signInData) throws AccessDeniedException {
        Optional<User> optionalUser = usersRepository.findUserByEmail(signInData.getEmail());

        if(optionalUser.isPresent()) {

            User user = optionalUser.get();

            if(passwordEncoder.matches(signInData.getPassword(), user.getPassword())) {

                String token = Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("email", user.getEmail())
                        .claim("role", user.getRole())
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();

                return new TokenDto(token);

            }

            else {

                throw new AccessDeniedException("Wrong email/password");

            }

        }

        else throw new AccessDeniedException("User not found");
    }

    @Override
    public User signUp(SignUpDto signUpDto) {

        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(Role.USER)
                .build();

        user = usersRepository.save(user);

        return user;

    }

}
