package health.mental.controller;

import health.mental.Exception.TokenExpiredExceptions;
import health.mental.Exception.TokenInvalidException;
import health.mental.Utils;
import health.mental.domain.User.*;
import health.mental.infra.security.TokenService;
import health.mental.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and management")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Authenticate user", description = "Receives login and password and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), Utils.decodeJwt(authDTO.password()));

        try {
            authenticationManager.authenticate(usernamePassword);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("{\"error\": \"Invalid login or password\"}");
        }

        var authentication = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Get authenticated user", description = "Returns the details of the authenticated user.")
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        String userLogin = tokenService.validateToken(token);
        return ResponseEntity.ok(UserMapper.toUserMeDTO((User) userRepository.findByLogin(userLogin)));
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account on the platform.")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthRegisterDTO authDTO) {
        if (this.userRepository.findByLogin(authDTO.login()) != null) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("{\"error\": \"Login/Username already in use\"}");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(Utils.decodeJwt(authDTO.password()));
        try{
            User user = new User(authDTO.login(), encodedPassword, authDTO.role());
            userRepository.save(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(e.getMessage());
        }


        return ResponseEntity.ok().build();
    }
}