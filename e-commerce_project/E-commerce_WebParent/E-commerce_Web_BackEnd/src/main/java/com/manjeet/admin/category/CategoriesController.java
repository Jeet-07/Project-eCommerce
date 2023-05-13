package com.manjeet.admin.category;

import com.manjeet.common.exception.CategoryNotFoundException;
import com.manjeet.admin.util.FileUploadUtil;
import com.manjeet.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoryService service;

    @GetMapping("/categories")
    public String categories(Model model){
        CategoryPageInfo pageInfo=new CategoryPageInfo();
        List<Category> categoryList = service.listByPage(pageInfo,1);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("currPage",1);
        model.addAttribute("itemShown",categoryList.size());
        model.addAttribute("moduleURL","/categories");
        return "categories/categories";
    }
    @GetMapping("/categories/page/{pageNum}")
    public String categoriesByPage(Model model,
                                   @PathVariable("pageNum")int pageNum,
                                   @Param("keyword")String keyword){
        CategoryPageInfo pageInfo=new CategoryPageInfo();
        List<Category> categoryList;
        if(keyword != null && !keyword.isEmpty()){
            categoryList = service.searchByKeyword(pageInfo,keyword,pageNum);
        }else{
            categoryList = service.listByPage(pageInfo,pageNum);
        }

        model.addAttribute("categoryList",categoryList);
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("currPage",pageNum);
        model.addAttribute("itemShown",categoryList.size());
        model.addAttribute("keyword",keyword);
        model.addAttribute("moduleURL","/categories");

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> categoriesList = service.getAllCategories();
        model.addAttribute("category",new Category());
        model.addAttribute("categoriesList",categoriesList);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category-form";
    }
    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name="id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = service.getCategoryById(id);
            model.addAttribute("category", category);
            List<Category> categoriesList = service.getAllCategories();
            model.addAttribute("categoriesList",categoriesList);
            model.addAttribute("pageTitle", "Edit Category ( ID : "+id+" )");
            return "categories/category-form";
        }catch(CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               @RequestParam("parent") String parent){
        boolean isUpdating = category.getId() != null ;
        Integer parentId = Integer.valueOf(parent);
        if(parentId > 0){
            Category parentCat = new Category(parentId);
            category.setParent(parentCat);
        }else{
            category.setParent(null);
        }
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = service.save(category);
            String uploadDir = "category-photos/"+savedCategory.getId();
            FileUploadUtil.clean(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            if(category.getImage().isEmpty())
                category.setImage(null);

            service.save(category);
        }
        String message = isUpdating ? "Category Updated successfully.":"New category created successfully.";
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name="id") Integer id,RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("message","Category with ID : "+id+" deleted successfully.");
            String catDir = "category-photos/"+id;
            FileUploadUtil.clean(catDir);
            return "redirect:/categories";
        }catch(CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable(name="id") Integer id,@PathVariable(name="status")boolean enabled,RedirectAttributes redirectAttributes) {
        service.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled.":"disabled.";
        String message = "Category with ID : "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

}
