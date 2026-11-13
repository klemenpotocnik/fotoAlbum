package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.*;
import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.model.Category;
import fotoAlbum.fotoAlbum.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;
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
    private final CategoryRepository categories;
    public AlbumController(AlbumRepository albums, PhotoService photos, CurrentUser current, CategoryRepository categories) {
        this.albums = albums; this.photos = photos; this.current = current;
        this.categories = categories;
    }
    private Album owned(Long id) {
        return albums.findByIdAndOwnerId(id, current.id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Album ni na voljo."));
    }
    private Category category(AlbumRequest request) {
        Long id = request.categoryId();
        String customName = request.customCategoryName();
        if (id == null) {
            if (customName != null) throw invalidCategory();
            return null;
        }
        Category selected = categories.findByIdAndOwnerId(id, current.id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategorija ni na voljo."));
        if (!selected.getName().equals("Drugo")) {
            if (customName != null) throw invalidCategory();
            return selected;
        }
        if (customName == null || customName.isBlank()) throw invalidCategory();
        String name = customName.strip();
        if (name.equalsIgnoreCase("Drugo")) throw invalidCategory();
        return categories.findByNameIgnoreCaseAndOwnerId(name, current.id()).orElseGet(() -> {
            Category created = new Category();
            created.setName(name);
            created.setOwner(current.get());
            return categories.saveAndFlush(created);
        });
    }

    private ResponseStatusException invalidCategory() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Pri možnosti Drugo vpiši ime svoje kategorije (največ 100 znakov). Pri drugih možnostih izberi kategorijo s seznama.");
    }
    @PostMapping
    @Transactional
    public AlbumResponse create(@Valid @RequestBody AlbumRequest request) {
        Album album = new Album();
        album.setTitle(request.title());
        album.setDescription(request.description());
        album.setCategory(category(request));
        album.setOwner(current.get());
        return AlbumResponse.from(albums.save(album));
    }
    @GetMapping
    @Transactional(readOnly = true)
    public List<AlbumResponse> all() {
        return albums.findByOwnerId(current.id()).stream().map(AlbumResponse::from).toList();
    }
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public AlbumResponse one(@PathVariable Long id) { return AlbumResponse.from(owned(id)); }
    @PutMapping("/{id}")
    @Transactional
    public AlbumResponse update(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        Album album = owned(id);
        album.setTitle(request.title());
        album.setDescription(request.description());
        album.setCategory(category(request));
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

