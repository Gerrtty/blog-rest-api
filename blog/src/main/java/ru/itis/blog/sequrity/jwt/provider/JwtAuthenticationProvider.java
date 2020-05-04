package ru.itis.blog.sequrity.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itis.blog.sequrity.jwt.authenfication.JwtAuthentication;
import ru.itis.blog.sequrity.jwt.details.UserDetailsImpl;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = authentication.getName();

        Claims claims;

        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }
        catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Bad token");
        }

        if(claims == null || claims.getSubject() == null) {
            throw new AuthenticationCredentialsNotFoundException("Bad token");
        }

        UserDetails userDetails = UserDetailsImpl.builder()
                .userId(Long.parseLong(claims.getSubject()))
                .email(claims.get("email", String.class))
                .role(claims.get("role", String.class))
                .build();

        authentication.setAuthenticated(true);

        ((JwtAuthentication) authentication).setUserDetails(userDetails);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }
}
