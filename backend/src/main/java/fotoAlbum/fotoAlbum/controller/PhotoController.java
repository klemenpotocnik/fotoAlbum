
package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.PhotoResponse;
import fotoAlbum.fotoAlbum.service.PhotoService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Operation(summary = "Naloži eno ali več fotografij v album")
    @PostMapping(
            value = "/{albumId}/photos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<PhotoResponse>> uploadPhotos(
            @PathVariable Long albumId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        List<PhotoResponse> photos =
                photoService.uploadPhotos(albumId, files);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(photos);
    }

    @Operation(summary = "Pridobi vse fotografije določenega albuma")
    @GetMapping("/{albumId}/photos")
    public ResponseEntity<List<PhotoResponse>> getPhotosByAlbum(
            @PathVariable Long albumId
    ) {
        List<PhotoResponse> photos =
                photoService.getPhotosByAlbumId(albumId);

        return ResponseEntity.ok(photos);
    }

    @Operation(summary = "Izbriši vse fotografije določenega albuma")
    @DeleteMapping("/{albumId}/photos")
    public ResponseEntity<Void> deleteAllPhotosByAlbum(
            @PathVariable Long albumId
    ) {
        photoService.deleteAllPhotosByAlbumId(albumId);

        return ResponseEntity.noContent().build();
    }




}
