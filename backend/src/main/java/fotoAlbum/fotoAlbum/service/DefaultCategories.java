package fotoAlbum.fotoAlbum.service;

import fotoAlbum.fotoAlbum.model.AppUser;
import fotoAlbum.fotoAlbum.model.Category;
import fotoAlbum.fotoAlbum.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DefaultCategories {
    public static final List<String> NAMES = List.of(
            "Potovanja", "Družina", "Prijatelji", "Dogodki", "Narava", "Šport", "Vsakdan", "Drugo");

    private final CategoryRepository categories;

    public DefaultCategories(CategoryRepository categories) {
        this.categories = categories;
    }

    @Transactional
    public void createFor(AppUser user) {
        categories.saveAll(NAMES.stream().map(name -> {
            Category category = new Category();
            category.setName(name);
            category.setOwner(user);
            return category;
        }).toList());
    }
}
