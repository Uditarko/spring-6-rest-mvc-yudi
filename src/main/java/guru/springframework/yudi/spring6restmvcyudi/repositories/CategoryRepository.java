package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
