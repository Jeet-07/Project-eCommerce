package com.manjeet.admin.category;

import com.manjeet.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.parent = null")
    public List<Category> findRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.parent = null")
    public Page<Category> findRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% OR c.alias LIKE %?1%")
    public Page<Category> search(String keyword,Pageable pageable);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id =?1")
    @Modifying
    public void updateEnableStatus(Integer id, boolean enabled);

    public Category findByName(String name);
    public Category findByAlias(String alias);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.enabled = true")
    public Long countEnabled();
}
