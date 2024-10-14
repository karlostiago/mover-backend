package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.SubCategoryApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.domain.SubCategory;
import com.ctsousa.mover.request.SubCategoryRequest;
import com.ctsousa.mover.response.SubCategoryResponse;
import com.ctsousa.mover.service.CategoryService;
import com.ctsousa.mover.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/subcategories")
public class SubCategoryResource extends BaseResource<SubCategoryResponse, SubCategoryRequest, SubCategoryEntity> implements SubCategoryApi {

    @Autowired
    private SubCategoryService subCategoryService;

    public SubCategoryResource(SubCategoryService subCategoryService) {
        super(subCategoryService);
    }

    @Override
    public ResponseEntity<SubCategoryResponse> add(SubCategoryRequest request) {
        SubCategory subcategory = toMapper(request, SubCategory.class);
        SubCategoryEntity entity = subCategoryService.save(subcategory.toEntity());
        return ResponseEntity.ok(toMapper(entity, SubCategoryResponse.class));
    }

    @Override
    public ResponseEntity<SubCategoryResponse> update(Long id, SubCategoryRequest request) {
        SubCategoryEntity entity = subCategoryService.findById(id);
        SubCategory subcategory = toMapper(request, SubCategory.class);
        subCategoryService.save(subcategory.toEntity());
        return ResponseEntity.ok(toMapper(entity, SubCategoryResponse.class));
    }

    @Override
    public void delete(Long id) {
        subCategoryService.existsById(id);
        super.delete(id);
    }

    @Override
    public ResponseEntity<List<SubCategoryResponse>> findBy(String search) {
        List<SubCategoryEntity> entities = subCategoryService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, SubCategoryResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return SubCategoryResponse.class;
    }
}
