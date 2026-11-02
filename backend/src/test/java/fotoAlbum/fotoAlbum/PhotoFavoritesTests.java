package fotoAlbum.fotoAlbum;

import fotoAlbum.fotoAlbum.controller.PhotoFileController;
import fotoAlbum.fotoAlbum.model.Album;
import fotoAlbum.fotoAlbum.model.Photo;
import fotoAlbum.fotoAlbum.repository.AlbumRepository;
import fotoAlbum.fotoAlbum.repository.PhotoRepository;
import fotoAlbum.fotoAlbum.service.PhotoService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:photo-favorites;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@TestExecutionListeners(
        listeners = {DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS
)
class PhotoFavoritesTests {
    @Autowired PhotoService service;
    @Autowired PhotoRepository photos;
    @Autowired AlbumRepository albums;
    @Autowired EntityManager entityManager;

    @Test
    void favoriteIsPersistedAndCanBeRemoved() {
        Photo photo = createPhoto(null);
        assertThat(service.getPhotoById(photo.getId()).favorite()).isFalse();
        assertThat(service.setFavorite(photo.getId(), true).favorite()).isTrue();
        service.setFavorite(photo.getId(), true);
        entityManager.flush();
        entityManager.clear();
        assertThat(service.getPhotoById(photo.getId()).favorite()).isTrue();
        service.setFavorite(photo.getId(), false);
        entityManager.flush();
        entityManager.clear();
        assertThat(service.getPhotoById(photo.getId()).favorite()).isFalse();
        assertThat(service.getAllPhotos(null, null, true)).isEmpty();
    }

    @Test
    void combinesFavoritesAndDateRangeAndPreservesOrdering() {
        LocalDate date = LocalDate.of(2026, 6, 4);
        Photo first = createPhoto(date.atStartOfDay());
        Photo last = createPhoto(date.plusDays(6).atTime(23, 59, 59, 999999000));
        Photo outside = createPhoto(date.plusDays(7).atStartOfDay());
        Photo noDate = createPhoto(null);
        createPhoto(date.atTime(12, 0));
        for (Photo photo : new Photo[]{first, last, outside, noDate}) {
            service.setFavorite(photo.getId(), true);
        }
        assertThat(service.getAllPhotos(null, null, true)).hasSize(4);
        assertThat(service.getAllPhotos(date, date.plusDays(6), true))
                .extracting(photo -> photo.id()).containsExactly(last.getId(), first.getId());
        assertThat(service.getAllPhotos(date, date.plusDays(6), false)).hasSize(3);
        assertThat(service.getAllPhotos(null, null, false)).hasSize(5);
    }

    @Test
    void apiUpdatesFavoriteAndValidatesRequests() throws Exception {
        Photo photo = createPhoto(null);
        var mvc = MockMvcBuilders.standaloneSetup(new PhotoFileController(service)).build();
        mvc.perform(patch("/api/photos/{id}/favorite", photo.getId())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"favorite\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.favorite").value(true));
        mvc.perform(get("/api/photos").param("favoritesOnly", "true"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(patch("/api/photos/{id}/favorite", photo.getId())
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(patch("/api/photos/{id}/favorite", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"favorite\":true}"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/photos").param("startDate", "2026-06-10").param("endDate", "2026-06-04"))
                .andExpect(status().isBadRequest());
    }

    private Photo createPhoto(LocalDateTime dateTaken) {
        Photo photo = new Photo();
        photo.setAlbum(albums.save(new Album("Priljubljene")));
        photo.setFilename("test.jpg");
        photo.setFilePath("test.jpg");
        photo.setDateTaken(dateTaken);
        photo.setUploadedAt(LocalDateTime.of(2026, 10, 29, 12, 0));
        return photos.save(photo);
    }
}
