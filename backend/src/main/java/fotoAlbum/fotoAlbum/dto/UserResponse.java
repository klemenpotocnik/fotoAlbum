package fotoAlbum.fotoAlbum.dto;

import fotoAlbum.fotoAlbum.model.AppUser;

public record UserResponse(Long id, String email) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}
