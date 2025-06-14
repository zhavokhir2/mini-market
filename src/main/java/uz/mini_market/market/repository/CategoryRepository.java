package uz.mini_market.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mini_market.market.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}

