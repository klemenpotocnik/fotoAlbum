package fotoAlbum.fotoAlbum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlbumRequest(@NotBlank @Size(max = 255) String title, String description, Long categoryId, @Size(max = 100) String customCategoryName) {}

