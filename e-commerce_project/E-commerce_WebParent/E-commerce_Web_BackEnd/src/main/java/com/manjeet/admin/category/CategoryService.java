package com.manjeet.admin.category;

import com.manjeet.common.entity.Category;
import com.manjeet.common.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private static final int ROOT_CATEGORIES_PER_PAGE=3;
    private static final int SEARCH_CATEGORIES_PER_PAGE=5;

    private final CategoryRepository categoryRepo;

    public Category getCategoryById(Integer id) throws CategoryNotFoundException {
        Optional<Category> optCat=categoryRepo.findById(id);
        if(optCat.isPresent()){
             return optCat.get();
        }else{
            throw new CategoryNotFoundException("No such Category present.");
        }
    }

    public List<Category> searchByKeyword(CategoryPageInfo pageInfo,String keyword,int pageNum){
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNum-1,SEARCH_CATEGORIES_PER_PAGE,sort);
        Page<Category> categoriesPage = categoryRepo.search(keyword,pageable);
        pageInfo.setTotalPages(categoriesPage.getTotalPages());
        pageInfo.setTotalElements(categoriesPage.getTotalElements());
        List<Category> list = categoriesPage.getContent();
        return list;
    }

    public Category save(Category cat) {
        Category parent = null;
        if(cat.getParent() != null) {
            parent = categoryRepo.findById(cat.getParent().getId()).get();
        }
        if(parent != null){
            String allParents = parent.getAllParentIDs() == null ? "-": parent.getAllParentIDs();
            allParents += String.valueOf(parent.getId())+"-";
            cat.setAllParentIDs(allParents);
        }
        return categoryRepo.save(cat) ;
    }

    public List<Category> listByPage(CategoryPageInfo pageInfo,int pageNum){
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNum-1,ROOT_CATEGORIES_PER_PAGE,sort);
        Page<Category> categoriesPage = categoryRepo.findRootCategories(pageable);
        pageInfo.setTotalPages(categoriesPage.getTotalPages());
        pageInfo.setTotalElements(categoriesPage.getTotalElements());
        List<Category> list = categoriesPage.getContent();
        List<Category> formList = new ArrayList<>();
        for(Category cat:list){
            if(cat.getParent()==null){
                cat.setHierarchicalName(cat.getName());
                formList.add(cat);
                addSubCategory(formList,cat.getChildren()," --");
            }
        }
        return formList;
    }
    public List<Category> getAllCategories(){
        List<Category> list = categoryRepo.findRootCategories(Sort.by("name").ascending());
        List<Category> formList = new ArrayList<>();
        for(Category cat:list){
            if(cat.getParent()==null){
                cat.setHierarchicalName(cat.getName());
                formList.add(cat);
                addSubCategory(formList,cat.getChildren()," --");
            }
        }
        return formList;
    }
    private static void addSubCategory(List<Category> formList,Set<Category> children, String sublevel){
        for(Category cat:children){
            cat.setHierarchicalName(sublevel+" "+cat.getName());
            formList.add(cat);
            addSubCategory(formList,cat.getChildren(),sublevel+"--");
        }
    }

    public void deleteById(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepo.countById(id);
        if(countById == null | countById==0) {
            throw new CategoryNotFoundException("No Such User Present With ID : "+id);
        }
        System.out.println("deleting id:"+id);
        categoryRepo.deleteById(id);
    }
    public void updateCategoryEnabledStatus(Integer id,boolean status) {
        categoryRepo.updateEnableStatus(id, status);
    }

    public String checkUniqueNameAndAlias(Integer id,String name,String alias){
        boolean isExisting = (id!=null && id>0);
        Category byName = categoryRepo.findByName(name);
        Category byAlias = categoryRepo.findByAlias(alias);

        if(isExisting){
            if (byName == null && byAlias == null)
                return "Unique";

            if (byName != null && byAlias != null)
                if(byName.getId()==id && byAlias.getId()==id)
                    return "Unique";
        }else{
            if (byName == null && byAlias == null)
                return "Unique";
        }
        return "Duplicated";
    }

    public Long countAll(){
        return categoryRepo.count();
    }
    public Long countEnabled(){
        return categoryRepo.countEnabled();
    }
}
