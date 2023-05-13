package com.manjeet.admin.category;

import com.manjeet.admin.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoriesRestController {

    private final CategoryService service;
    @PostMapping("/categories/check-unique-category")
    public String checkUniqueness(@Param("id")Integer id,
                                  @Param("name")String name,
                                  @Param("alias")String alias){
        return service.checkUniqueNameAndAlias(id,name,alias);
    }
}
