
package fotoAlbum.fotoAlbum.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String filename;

    @Column(name = "file_path")
    private String filePath;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "creation_date")
    private LocalDateTime dateTaken;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean favorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @PrePersist
    public void prePersist() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }
}
