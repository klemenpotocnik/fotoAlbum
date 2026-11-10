package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.*;
import fotoAlbum.fotoAlbum.model.AppUser;
import fotoAlbum.fotoAlbum.repository.UserRepository;
import fotoAlbum.fotoAlbum.service.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final CurrentUser current;

    public AuthController(UserRepository users, PasswordEncoder encoder, CurrentUser current) {
        this.users = users; this.encoder = encoder; this.current = current;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken(), "headerName", token.getHeaderName());
    }

    @GetMapping("/me")
    public UserResponse me() { return UserResponse.from(current.get()); }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geslo je predolgo.");
        }
        String email = request.email().strip().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) throw duplicateEmail();
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(request.password()));
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(users.saveAndFlush(user)));
        } catch (DataIntegrityViolationException ex) {
            throw duplicateEmail();
        }
    }

    private ResponseStatusException duplicateEmail() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Račun s tem e-poštnim naslovom že obstaja.");
    }
}
