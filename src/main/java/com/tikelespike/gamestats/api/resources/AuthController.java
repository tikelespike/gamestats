package com.tikelespike.gamestats.api.resources;

import com.tikelespike.gamestats.api.entities.JwtDTO;
import com.tikelespike.gamestats.api.entities.SignInDTO;
import com.tikelespike.gamestats.api.entities.SignUpDTO;
import com.tikelespike.gamestats.api.mapper.SignupMapper;
import com.tikelespike.gamestats.api.security.TokenProvider;
import com.tikelespike.gamestats.businesslogic.AuthService;
import com.tikelespike.gamestats.businesslogic.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService service;
    private final TokenProvider tokenService;
    private final SignupMapper signupMapper;

    public AuthController(AuthenticationManager authenticationManager, AuthService service,
                          TokenProvider tokenService, SignupMapper signupMapper) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
        this.signupMapper = signupMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO data) {
        service.signUp(signupMapper.toBusinessObject(data));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDTO> signIn(@RequestBody SignInDTO data) {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtDTO(accessToken));
    }
}
