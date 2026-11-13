package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.*;
import fotoAlbum.fotoAlbum.model.AppUser;
import fotoAlbum.fotoAlbum.repository.UserRepository;
import fotoAlbum.fotoAlbum.service.CurrentUser;
import fotoAlbum.fotoAlbum.service.DefaultCategories;
import org.springframework.transaction.annotation.Transactional;
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
    private final DefaultCategories categories;

    public AuthController(UserRepository users, PasswordEncoder encoder, CurrentUser current, DefaultCategories categories) {
        this.users = users; this.encoder = encoder; this.current = current;
        this.categories = categories;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken(), "headerName", token.getHeaderName());
    }

    @GetMapping("/me")
    public UserResponse me() { return UserResponse.from(current.get()); }

    @PostMapping("/signup")
    @Transactional
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
            user = users.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            throw duplicateEmail();
        }
        categories.createFor(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    private ResponseStatusException duplicateEmail() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Račun s tem e-poštnim naslovom že obstaja.");
    }
}

