package uz.mini_market.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mini_market.market.dto.CategoryRequest;
import uz.mini_market.market.dto.CategoryResponse;
import uz.mini_market.market.exception.CategoryAlreadyExistsException;
import uz.mini_market.market.model.Category;
import uz.mini_market.market.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryAlreadyExistsException("Категория с таким именем уже существует.");
        }
        Category category = Category.builder().name(request.getName()).build();
        category = categoryRepository.save(category);
        return toResponse(category);
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}

