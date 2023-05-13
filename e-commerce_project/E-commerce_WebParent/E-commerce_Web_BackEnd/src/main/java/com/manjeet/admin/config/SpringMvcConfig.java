package com.manjeet.admin.config;

import com.manjeet.admin.paging.PagingAndSortingArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.List;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String userPhotosPath = Paths.get("user-photos").toFile().getAbsolutePath();
        String categoryPhotosPath = Paths.get("category-photos").toFile().getAbsolutePath();
        String brandPhotosPath = Paths.get("brand-logos").toFile().getAbsolutePath();
        String productPhotosPath = Paths.get("product-images").toFile().getAbsolutePath();
        registry.addResourceHandler("/user-photos/**").addResourceLocations("file:/"+userPhotosPath+"/");
        registry.addResourceHandler("/category-photos/**").addResourceLocations("file:/"+categoryPhotosPath+"/");
        registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/"+brandPhotosPath+"/");
        registry.addResourceHandler("/product-images/**").addResourceLocations("file:/"+productPhotosPath+"/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PagingAndSortingArgumentResolver());
    }


}
