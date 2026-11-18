package fotoAlbum.fotoAlbum.service;

import fotoAlbum.fotoAlbum.controller.ApiExceptionHandler;
import fotoAlbum.fotoAlbum.controller.PhotoFileController;
import fotoAlbum.fotoAlbum.dto.PhotoDetailsRequest;
import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.model.Photo;
import fotoAlbum.fotoAlbum.repository.AlbumRepository;
import fotoAlbum.fotoAlbum.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PhotoDetailsTest {
    private PhotoRepository photos;
    private PhotoService service;
    private Photo photo;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        photos = mock(PhotoRepository.class);
        CurrentUser current = mock(CurrentUser.class);
        when(current.id()).thenReturn(7L);
        service = new PhotoService(photos, mock(AlbumRepository.class), current, "uploads");
        Album album = new Album("Izlet");
        album.setId(3L);
        photo = new Photo();
        photo.setId(11L);
        photo.setAlbum(album);
        photo.setFilename("original.jpg");
        photo.setFilePath("stored.jpg");
        photo.setUploadedAt(LocalDateTime.of(2026, 11, 18, 12, 0));
        photo.setDateTaken(LocalDateTime.of(2026, 8, 1, 10, 0));
        photo.setFavorite(true);
        photo.setTitle("Prejšnji naslov");
        photo.setDescription("Prejšnji opis");
        when(photos.findByIdAndAlbumOwnerId(11L, 7L)).thenReturn(Optional.of(photo));
        when(photos.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        mvc = MockMvcBuilders.standaloneSetup(new PhotoFileController(service))
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void updateNormalizesTextAndPreservesPhoto() {
        var result = service.updateDetails(11L, new PhotoDetailsRequest("  Morje  ", "\n Prva vrstica\nDruga vrstica \n"));

        assertEquals("Morje", result.title());
        assertEquals("Prva vrstica\nDruga vrstica", result.description());
        assertEquals("original.jpg", result.filename());
        assertEquals("stored.jpg", result.filePath());
        assertEquals(3L, result.albumId());
        assertEquals(photo.getUploadedAt(), result.uploadedAt());
        assertEquals(photo.getDateTaken(), result.dateTaken());
        assertTrue(result.favorite());
        verify(photos).save(photo);
    }

    @Test
    void acceptsMaximumLengthsAfterTrimming() {
        var result = service.updateDetails(11L,
                new PhotoDetailsRequest(" " + "a".repeat(100) + " ", "b".repeat(2000)));
        assertEquals(100, result.title().length());
        assertEquals(2000, result.description().length());
    }

    @Test
    void clearsOptionalFieldsAndAllowsOnlyOneField() {
        var result = service.updateDetails(11L, new PhotoDetailsRequest(" \n ", null));
        assertNull(result.title());
        assertNull(result.description());
        result = service.updateDetails(11L, new PhotoDetailsRequest(null, "Opis"));
        assertNull(result.title());
        assertEquals("Opis", result.description());
        result = service.updateDetails(11L, new PhotoDetailsRequest("Naslov", "\t"));
        assertEquals("Naslov", result.title());
        assertNull(result.description());
    }

    @Test
    void rejectsOversizedFieldsWithoutChangingEitherValue() {
        for (var request : List.of(new PhotoDetailsRequest("a".repeat(101), "Nov opis"),
                new PhotoDetailsRequest("Nov naslov", "b".repeat(2001)))) {
            var error = assertThrows(ResponseStatusException.class, () -> service.updateDetails(11L, request));
            assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
            assertEquals("Prejšnji naslov", photo.getTitle());
            assertEquals("Prejšnji opis", photo.getDescription());
        }
        verify(photos, never()).save(any());
    }

    @Test
    void rejectsPhotoOutsideCurrentUsersCollection() {
        when(photos.findByIdAndAlbumOwnerId(11L, 7L)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class,
                () -> service.updateDetails(11L, new PhotoDetailsRequest("Naslov", "Opis")));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(photos, never()).save(any());
        assertEquals("Prejšnji naslov", photo.getTitle());
    }

    @Test
    void endpointSavesAndReturnsDetailsOnFollowingRead() throws Exception {
        mvc.perform(put("/api/photos/11").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"  Morje  \",\"description\":\"Opis\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Morje"))
                .andExpect(jsonPath("$.description").value("Opis"))
                .andExpect(jsonPath("$.filename").value("original.jpg"));
        mvc.perform(get("/api/photos/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Morje"))
                .andExpect(jsonPath("$.description").value("Opis"));
    }

    @Test
    void endpointRejectsLongTitleWithUsefulMessage() throws Exception {
        mvc.perform(put("/api/photos/11").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + "a".repeat(101) + "\",\"description\":\"Opis\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Naslov fotografije lahko vsebuje največ 100 znakov."));
        verify(photos, never()).save(any());
    }
}
