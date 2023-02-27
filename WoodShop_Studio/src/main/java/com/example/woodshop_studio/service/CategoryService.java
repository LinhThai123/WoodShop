package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.model.request.CategoryReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    public List<Category> getListCategory();

//    public List<CategoryInfo> getListCategoryAndProductCount();

    public Category getCategoryById (String id);

    public Category createCategory(CategoryReq req);

    public void updateCategory(String id, CategoryReq req);

    public void deleteCategory(String id);

    Page<Category> adminGetListCategory( String name, int page);

}
