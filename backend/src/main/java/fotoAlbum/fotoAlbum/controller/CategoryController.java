package fotoAlbum.fotoAlbum.controller;

import fotoAlbum.fotoAlbum.dto.CategoryResponse;
import fotoAlbum.fotoAlbum.model.Category;
import fotoAlbum.fotoAlbum.repository.CategoryRepository;
import fotoAlbum.fotoAlbum.service.CurrentUser;
import fotoAlbum.fotoAlbum.service.DefaultCategories;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categories;
    private final CurrentUser current;

    public CategoryController(CategoryRepository categories, CurrentUser current) {
        this.categories = categories;
        this.current = current;
    }

    @GetMapping
    public List<CategoryResponse> all() {
        return categories.findByOwnerIdOrderByNameAsc(current.id()).stream()
                .sorted(Comparator.comparingInt((Category category) -> {
                    int index = DefaultCategories.NAMES.indexOf(category.getName());
                    return index < 0 ? DefaultCategories.NAMES.size() : index;
                }).thenComparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
                .map(CategoryResponse::from).toList();
    }
}
