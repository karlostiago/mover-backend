package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.CategoryApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.domain.Category;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.request.CategoryRequest;
import com.ctsousa.mover.response.CategoryResponse;
import com.ctsousa.mover.response.TypeCategoryResponse;
import com.ctsousa.mover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/categories")
public class CategoryResource extends BaseResource<CategoryResponse, CategoryRequest, CategoryEntity> implements CategoryApi {

    @Autowired
    private CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        super(categoryService);
    }

    @Override
    public ResponseEntity<CategoryResponse> add(CategoryRequest request) {
        Category category = toMapper(request, Category.class);
        CategoryEntity entity = categoryService.save(category.toEntity());
        return ResponseEntity.ok(toMapper(entity, CategoryResponse.class));
    }

    @Override
    public ResponseEntity<CategoryResponse> update(Long id, CategoryRequest request) {
        CategoryEntity entity = categoryService.findById(id);
        Category category = toMapper(request, Category.class);
        categoryService.save(category.toEntity());
        return ResponseEntity.ok(toMapper(entity, CategoryResponse.class));
    }

    @Override
    public void delete(Long id) {
        categoryService.existsById(id);
        super.delete(id);
    }

    @Override
    public ResponseEntity<List<TypeCategoryResponse>> findAllTypes() {
        List<TypeCategory> types = Stream.of(TypeCategory.values())
                .sorted(Comparator.comparing(TypeCategory::getCode))
                .toList();
        return ResponseEntity.ok(toCollection(types, TypeCategoryResponse.class));
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> findBy(String search) {
        List<CategoryEntity> entities = categoryService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, CategoryResponse.class));
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> findByTypeCategory(String type) {
        TypeCategory typeCategory = TypeCategory.toDescription(type);
        List<CategoryEntity> entities = categoryService.filterBy(typeCategory);
        return ResponseEntity.ok(toCollection(entities, CategoryResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return CategoryResponse.class;
    }
}
