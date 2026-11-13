package fotoAlbum.fotoAlbum.repository;

import fotoAlbum.fotoAlbum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOwnerIdOrderByNameAsc(Long ownerId);
    Optional<Category> findByNameIgnoreCaseAndOwnerId(String name, Long ownerId);
    Optional<Category> findByIdAndOwnerId(Long id, Long ownerId);
}
