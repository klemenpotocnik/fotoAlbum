package fotoAlbum.fotoAlbum.dto;

public record PhotoDetailsRequest(String title, String description) {
    public PhotoDetailsRequest {
        title = title == null || title.isBlank() ? null : title.strip();
        description = description == null || description.isBlank() ? null : description.strip();
    }
}
