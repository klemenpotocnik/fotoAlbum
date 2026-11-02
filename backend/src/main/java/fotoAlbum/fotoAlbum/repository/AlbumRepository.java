package fotoAlbum.fotoAlbum.repository;

import fotoAlbum.fotoAlbum.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

}
