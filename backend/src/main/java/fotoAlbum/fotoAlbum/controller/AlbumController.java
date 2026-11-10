package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.*;
import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.repository.AlbumRepository;
import fotoAlbum.fotoAlbum.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumRepository albums;
    private final PhotoService photos;
    private final CurrentUser current;
    public AlbumController(AlbumRepository albums, PhotoService photos, CurrentUser current) {
        this.albums = albums; this.photos = photos; this.current = current;
    }
    private Album owned(Long id) {
        return albums.findByIdAndOwnerId(id, current.id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Album ni na voljo."));
    }
    @PostMapping
    public AlbumResponse create(@Valid @RequestBody AlbumRequest request) {
        Album album = new Album();
        album.setTitle(request.title());
        album.setDescription(request.description());
        album.setOwner(current.get());
        return AlbumResponse.from(albums.save(album));
    }
    @GetMapping
    public List<AlbumResponse> all() {
        return albums.findByOwnerId(current.id()).stream().map(AlbumResponse::from).toList();
    }
    @GetMapping("/{id}")
    public AlbumResponse one(@PathVariable Long id) { return AlbumResponse.from(owned(id)); }
    @PutMapping("/{id}")
    public AlbumResponse update(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        Album album = owned(id);
        album.setTitle(request.title());
        album.setDescription(request.description());
        return AlbumResponse.from(albums.save(album));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Album album = owned(id);
        photos.deleteAllPhotosByAlbumId(id);
        albums.delete(album);
        return ResponseEntity.ok("Album uspešno izbrisan");
    }
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAll() {
        for (Album album : albums.findByOwnerId(current.id())) {
            photos.deleteAllPhotosByAlbumId(album.getId());
            albums.delete(album);
        }
        return ResponseEntity.ok("Vsi tvoji albumi so bili uspešno izbrisani");
    }
}
