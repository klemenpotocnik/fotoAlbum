package fotoAlbum.fotoAlbum.dto;

import jakarta.validation.constraints.NotNull;

public record PhotoFavoriteRequest(@NotNull Boolean favorite) {}
