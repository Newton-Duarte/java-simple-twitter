package com.newtonduarte.simple_twitter.controllers;

import com.newtonduarte.simple_twitter.entities.Role;
import com.newtonduarte.simple_twitter.entities.User;
import com.newtonduarte.simple_twitter.entities.dto.CreateUserDto;
import com.newtonduarte.simple_twitter.entities.dto.LoginRequest;
import com.newtonduarte.simple_twitter.entities.dto.LoginResponse;
import com.newtonduarte.simple_twitter.repositories.RoleRepository;
import com.newtonduarte.simple_twitter.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtEncoder jwtEncoder, UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest) {
        User user = userRepository
                .findByName(loginRequest.name())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        boolean passwordMatches = passwordEncoder.matches(loginRequest.password(), user.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid credentials");
        }

        var now = Instant.now();
        var expiresIn = 300L; // 5 minutes

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("simple-twitter")
                .subject(user.getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

    @PostMapping(path = "/sign-up")
    @Transactional
    public ResponseEntity<Void> signUp(@RequestBody CreateUserDto createUserDto) {
        var userRole = roleRepository.findById(Role.Values.USER.getRoleId());

        var existingUser = userRepository.findByName(createUserDto.name());

        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setName(createUserDto.name());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        user.setRoles(Set.of(userRole.get()));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
