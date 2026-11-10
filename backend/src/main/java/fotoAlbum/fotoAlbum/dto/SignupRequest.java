package fotoAlbum.fotoAlbum.dto;

import jakarta.validation.constraints.*;

public record SignupRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotNull @Size(min = 8, max = 72) String password) {}
