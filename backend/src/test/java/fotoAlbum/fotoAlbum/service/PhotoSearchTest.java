package fotoAlbum.fotoAlbum.service;

import fotoAlbum.fotoAlbum.controller.PhotoController;
import fotoAlbum.fotoAlbum.controller.PhotoFileController;
import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.model.Photo;
import fotoAlbum.fotoAlbum.repository.AlbumRepository;
import fotoAlbum.fotoAlbum.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PhotoSearchTest {
    private PhotoRepository photos;
    private AlbumRepository albums;
    private PhotoService service;
    private MockMvc mvc;
    private Photo sea;
    private Photo hill;

    @BeforeEach
    void setUp() {
        photos = mock(PhotoRepository.class);
        albums = mock(AlbumRepository.class);
        CurrentUser current = mock(CurrentUser.class);
        when(current.id()).thenReturn(7L);
        service = new PhotoService(photos, albums, current, "uploads");
        Album album = new Album("Izleti");
        album.setId(3L);
        sea = photo(11L, album, "Morje", null);
        hill = photo(12L, album, null, "Izlet na Pohorje");
        when(photos.findAllByCreationDateDesc(7L, false)).thenReturn(List.of(sea, hill));
        when(albums.existsByIdAndOwnerId(3L, 7L)).thenReturn(true);
        when(photos.findByAlbumIdAndAlbumOwnerId(3L, 7L)).thenReturn(List.of(sea, hill));
        mvc = MockMvcBuilders.standaloneSetup(new PhotoFileController(service), new PhotoController(service)).build();
    }

    @Test
    void findsPartialTitleOrDescriptionRegardlessOfCase() {
        assertEquals(11L, service.getAllPhotos(null, null, false, " MOR ").getFirst().id());
        assertEquals(12L, service.getAllPhotos(null, null, false, "POHOR").getFirst().id());
        assertTrue(service.getAllPhotos(null, null, false, "ni zadetka").isEmpty());
    }

    @Test
    void emptySearchReturnsAuthorizedListAndPreservesOrder() {
        assertEquals(List.of(11L, 12L), service.getAllPhotos(null, null, false, " \n ")
                .stream().map(p -> p.id()).toList());
        verify(photos).findAllByCreationDateDesc(7L, false);
        verify(photos, never()).findAll();
        assertTrue(PhotoSearch.matches(null, null, ""));
        assertFalse(PhotoSearch.matches(null, null, "morje"));
    }

    @Test
    void accepts100Rejects101AndTreatsWildcardsLiterally() {
        assertEquals(100, PhotoSearch.normalize("a".repeat(100)).length());
        var error = assertThrows(ResponseStatusException.class,
                () -> service.getAllPhotos(null, null, false, "a".repeat(101)));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(photos);
        assertFalse(PhotoSearch.matches("Morje", "Izlet", "%"));
        assertTrue(PhotoSearch.matches("100%", null, "%"));
    }

    @Test
    void preservesDateAndFavoriteFilters() {
        LocalDate day = LocalDate.of(2026, 8, 1);
        when(photos.findByCreationDateRange(7L, day.atStartOfDay(), day.plusDays(1).atStartOfDay(), true))
                .thenReturn(List.of(sea));
        assertEquals(11L, service.getAllPhotos(day, day, true, "mor").getFirst().id());
        verify(photos).findByCreationDateRange(7L, day.atStartOfDay(), day.plusDays(1).atStartOfDay(), true);
    }

    @Test
    void scopesAlbumSearchAndRejectsForeignAlbum() {
        assertEquals(12L, service.getPhotosByAlbumId(3L, "izlet").getFirst().id());
        var error = assertThrows(ResponseStatusException.class, () -> service.getPhotosByAlbumId(99L, "izlet"));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(photos, never()).findByAlbumIdAndAlbumOwnerId(99L, 7L);
    }

    @Test
    void apiAcceptsQueryInBothGalleriesAndRejectsOversizedInput() throws Exception {
        mvc.perform(get("/api/photos").param("q", "MOR"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(11));
        mvc.perform(get("/api/albums/3/photos").param("q", "pohor"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(12));
        mvc.perform(get("/api/photos").param("q", "x".repeat(101)))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/albums/99/photos").param("q", "morje"))
                .andExpect(status().isNotFound());
    }

    private Photo photo(Long id, Album album, String title, String description) {
        Photo photo = new Photo();
        photo.setId(id);
        photo.setAlbum(album);
        photo.setFilename("original.jpg");
        photo.setTitle(title);
        photo.setDescription(description);
        return photo;
    }
}
