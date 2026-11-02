
package fotoAlbum.fotoAlbum.dto;

import java.time.LocalDateTime;

public record PhotoResponse(
        Long id,
        Long albumId,
        String albumTitle,
        String filename,
        String filePath,
        LocalDateTime uploadedAt,
        LocalDateTime dateTaken,
        boolean favorite
) {}
