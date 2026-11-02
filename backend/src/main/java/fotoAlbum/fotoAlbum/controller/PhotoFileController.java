package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.PhotoResponse;
import fotoAlbum.fotoAlbum.dto.PhotoFavoriteRequest;
import jakarta.validation.Valid;
import fotoAlbum.fotoAlbum.service.PhotoService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/photos")
public class PhotoFileController {

    private final PhotoService photoService;

    public PhotoFileController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Operation(summary = "Pridobi slikovno datoteko")
    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> getPhotoFile(@PathVariable Long id) {

        Resource resource = photoService.getPhotoFile(id);

        String filename = resource.getFilename();
        MediaType mediaType = filename != null
                && filename.toLowerCase().endsWith(".png")
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }


    @Operation(summary = "Izbriši posamezno fotografijo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {

        photoService.deletePhoto(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pridobi podatke posamezne fotografije")
    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhotoById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(photoService.getPhotoById(id));
    }

    @Operation(summary = "Pridobi vse fotografije")
    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getAllPhotos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean favoritesOnly
    ) {

        List<PhotoResponse> photos = photoService.getAllPhotos(startDate, endDate, favoritesOnly);

        return ResponseEntity.ok(photos);
    }

    @Operation(summary = "Označi ali odstrani fotografijo iz priljubljenih")
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<PhotoResponse> setFavorite(
            @PathVariable Long id, @Valid @RequestBody PhotoFavoriteRequest request) {
        return ResponseEntity.ok(photoService.setFavorite(id, request.favorite()));
    }

}
