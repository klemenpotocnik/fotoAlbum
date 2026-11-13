package fotoAlbum.fotoAlbum.dto;

import fotoAlbum.fotoAlbum.model.Album;
import java.time.LocalDate;

public record AlbumResponse(Long id, String title, String description, LocalDate createdAt, CategoryResponse category) {
    public static AlbumResponse from(Album album) {
        return new AlbumResponse(album.getId(), album.getTitle(), album.getDescription(), album.getCreatedAt(), album.getCategory() == null ? null : CategoryResponse.from(album.getCategory()));
    }
}

