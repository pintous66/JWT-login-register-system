package health.mental.controller;

import health.mental.domain.User.AuthDTO;
import health.mental.domain.User.AuthRegisterDTO;
import health.mental.domain.User.LoginResponseDTO;
import health.mental.domain.User.User;
import health.mental.infra.security.TokenService;
import health.mental.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var authentication = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthRegisterDTO authDTO) {

        if(this.userRepository.findByLogin(authDTO.login()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encodedPassword =  new BCryptPasswordEncoder().encode(authDTO.password());
        User user = new User(authDTO.login(), encodedPassword, authDTO.role());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
