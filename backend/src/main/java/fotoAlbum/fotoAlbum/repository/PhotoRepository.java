package fotoAlbum.fotoAlbum.repository;

import fotoAlbum.fotoAlbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.time.LocalDateTime;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByAlbumId(Long albumId);

    @Query("SELECT p FROM Photo p WHERE (:favoritesOnly = false OR p.favorite = true) "
            + "ORDER BY COALESCE(p.dateTaken, p.uploadedAt) DESC, p.id DESC")
    List<Photo> findAllByCreationDateDesc(boolean favoritesOnly);

    @Query("SELECT p FROM Photo p WHERE p.dateTaken >= :start AND p.dateTaken < :endExclusive "
            + "AND (:favoritesOnly = false OR p.favorite = true) ORDER BY p.dateTaken DESC, p.id DESC")
    List<Photo> findByCreationDateRange(
            LocalDateTime start, LocalDateTime endExclusive, boolean favoritesOnly);

}
