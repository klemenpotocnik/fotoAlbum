
package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.repository.AlbumRepository;
import fotoAlbum.fotoAlbum.service.PhotoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    public AlbumController(
            AlbumRepository albumRepository,
            PhotoService photoService
    ) {
        this.albumRepository = albumRepository;
        this.photoService = photoService;
    }

    // USTVARI ALBUM
    @PostMapping
    public Album createAlbum(@RequestBody Album album) {
        album.setId(null);
        return albumRepository.save(album);
    }

    // PRIDOBI VSE ALBUME
    @GetMapping
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    // PRIDOBI POSAMEZEN ALBUM
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POSODOBI ALBUM
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(
            @PathVariable Long id,
            @RequestBody Album updatedAlbum) {

        return albumRepository.findById(id)
                .map(album -> {
                    album.setTitle(updatedAlbum.getTitle());
                    album.setDescription(updatedAlbum.getDescription());

                    Album savedAlbum = albumRepository.save(album);
                    return ResponseEntity.ok(savedAlbum);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // IZBRIŠI POSAMEZEN ALBUM
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {

        if (!albumRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Najprej izbrišemo vse fotografije albuma
        photoService.deleteAllPhotosByAlbumId(id);

        // Nato izbrišemo še album
        albumRepository.deleteById(id);

        return ResponseEntity.ok("Album uspešno izbrisan");
    }

    // IZBRIŠI VSE ALBUME
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllAlbums() {

        List<Album> albums = albumRepository.findAll();

        for (Album album : albums) {

            // Izbrišemo vse fotografije posameznega albuma
            photoService.deleteAllPhotosByAlbumId(album.getId());

            // Izbrišemo album
            albumRepository.delete(album);
        }

        return ResponseEntity.ok(
                "Vsi albumi so bili uspešno izbrisani"
        );
    }
}
