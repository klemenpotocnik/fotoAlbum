
package fotoAlbum.fotoAlbum.service;

import fotoAlbum.fotoAlbum.dto.PhotoResponse;
import fotoAlbum.fotoAlbum.model.*;
import fotoAlbum.fotoAlbum.repository.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;


@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final Path uploadDirectory;

    public PhotoService(
            PhotoRepository photoRepository,
            AlbumRepository albumRepository,
            @Value("${app.upload-dir:uploads}") String uploadDir
    ) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.uploadDirectory = Path.of(uploadDir).toAbsolutePath();
    }


    private PhotoResponse toPhotoResponse(Photo photo) {
        return new PhotoResponse(
                photo.getId(),
                photo.getAlbum().getId(),
                photo.getAlbum().getTitle(),
                photo.getFilename(),
                photo.getFilePath(),
                photo.getUploadedAt(),
                photo.getDateTaken(),
                photo.isFavorite()
        );
    }


    public PhotoResponse uploadPhoto(Long albumId, MultipartFile file) {

        // Preverimo, ali album obstaja
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Album ne obstaja"
                ));

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Datoteka je prazna"
            );
        }

        // Dovolimo samo izbrane slikovne formate
        Set<String> allowedTypes = Set.of(
                "image/jpeg",
                "image/png"
        );

        if (!allowedTypes.contains(file.getContentType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dovoljene so samo JPG in PNG fotografije"
            );
        }

        // Ustvarimo unikatno ime datoteke
        String extension = file.getContentType().equals("image/png")
                ? ".png"
                : ".jpg";

        String storedFilename = UUID.randomUUID() + extension;
        Path filePath = uploadDirectory.resolve(storedFilename);

        try {
            Files.createDirectories(uploadDirectory);

            // Shranimo fotografijo na disk
            file.transferTo(filePath);

            // Shranimo podatke v PostgreSQL
            Photo photo = new Photo();
            photo.setAlbum(album);
            photo.setFilename(file.getOriginalFilename());
            photo.setFilePath(storedFilename);
            photo.setDateTaken(getDateTaken(file));

            Photo savedPhoto = photoRepository.save(photo);

            return toPhotoResponse(savedPhoto);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Napaka pri shranjevanju fotografije",
                    e
            );
        } catch (RuntimeException e) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException cleanupError) {
                e.addSuppressed(cleanupError);
            }
            throw e;
        }
    }

    private LocalDateTime getDateTaken(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            ExifSubIFDDirectory directory =
                    metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory != null) {
                String date = directory.getString(
                        ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
                );

                if (date != null) {
                    return LocalDateTime.parse(
                            date,
                            java.time.format.DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Napaka pri branju datuma: " + e.getMessage());
        }

        return null;
    }


    public List<PhotoResponse> getPhotosByAlbumId(Long albumId) {

        if (!albumRepository.existsById(albumId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Album ne obstaja"
            );
        }

        return photoRepository.findByAlbumId(albumId)
                .stream()
                .map(this::toPhotoResponse)
                .toList();
    }

    public List<PhotoResponse> uploadPhotos(
            Long albumId,
            List<MultipartFile> files
    ) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Izbrati moraš vsaj eno fotografijo"
            );
        }

        List<PhotoResponse> uploadedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            PhotoResponse response = uploadPhoto(albumId, file);
            uploadedPhotos.add(response);
        }

        return uploadedPhotos;
    }


    public Resource getPhotoFile(Long photoId) {

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fotografija ne obstaja"
                ));

        try {
            Path filePath = uploadDirectory
                    .resolve(photo.getFilePath())
                    .normalize();

            if (!filePath.startsWith(uploadDirectory)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Neveljavna pot do fotografije"
                );
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Datoteka fotografije ne obstaja"
                );
            }

            return resource;

        } catch (java.net.MalformedURLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Napaka pri branju fotografije",
                    e
            );
        }
    }



    public void deletePhoto(Long photoId) {

        // poišče fotografijo v podatkovni bazi
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fotografija ne obstaja"
                ));

        // pridobi pot do datoteke
        Path filePath = uploadDirectory
                .resolve(photo.getFilePath())
                .normalize();

        if (!filePath.startsWith(uploadDirectory)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Neveljavna pot do fotografije"
            );
        }

        // izbriše datoteko z diska
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Napaka pri brisanju datoteke",
                    e
            );
        }

        // odstrani zapis iz PostgreSQL
        photoRepository.delete(photo);
    }

    public void deleteAllPhotosByAlbumId(Long albumId) {

        if (!albumRepository.existsById(albumId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Album ne obstaja"
            );
        }

        List<Photo> photos = photoRepository.findByAlbumId(albumId);

        for (Photo photo : photos) {
            deletePhoto(photo.getId());
        }
    }

    public PhotoResponse getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fotografija ne obstaja"
                ));

        return toPhotoResponse(photo);
    }

    public List<PhotoResponse> getAllPhotos() {
        return photoRepository.findAllByCreationDateDesc(false)
                .stream()
                .map(this::toPhotoResponse)
                .toList();
    }

    public List<PhotoResponse> getAllPhotos(LocalDate startDate, LocalDate endDate) {
        return getAllPhotos(startDate, endDate, false);
    }

    public List<PhotoResponse> getAllPhotos(LocalDate startDate, LocalDate endDate, boolean favoritesOnly) {
        if (startDate == null && endDate == null) {
            return photoRepository.findAllByCreationDateDesc(favoritesOnly)
                    .stream().map(this::toPhotoResponse).toList();
        }
        if (startDate == null || endDate == null || startDate.isAfter(endDate)
                || endDate.equals(LocalDate.MAX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Izberi začetni in končni datum. Začetni datum mora biti pred končnim ali enak.");
        }

        return photoRepository
                .findByCreationDateRange(
                        startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), favoritesOnly)
                .stream()
                .map(this::toPhotoResponse)
                .toList();
    }

    @Transactional
    public PhotoResponse setFavorite(Long id, boolean favorite) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fotografija ne obstaja"));
        photo.setFavorite(favorite);
        return toPhotoResponse(photoRepository.save(photo));
    }


}
