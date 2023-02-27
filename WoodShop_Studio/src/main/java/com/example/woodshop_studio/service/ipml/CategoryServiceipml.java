package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.config.Contant;
import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.exception.BadRequestException;
import com.example.woodshop_studio.exception.InternalServerException;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.request.CategoryReq;
import com.example.woodshop_studio.repository.CategoryRepository;
import com.example.woodshop_studio.service.CategoryService;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;

@Component
public class CategoryServiceipml implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getListCategory() {
        List<Category> category = categoryRepository.findAll() ;
        return category ;
    }

    @Override
    public Category getCategoryById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new NotFoundException("Category do not exits");
        }
        return category.get();
    }

    @Override
    public Category createCategory(CategoryReq req) {
        // create category
        Category category = new Category();
        category.setName(req.getName());

        Slugify slg = new Slugify();
        category.setSlug(slg.slugify(req.getName()));

        // check trùng name
        if (categoryRepository.existsByName(req.getName())) {
            throw new BadRequestException("Tên danh mục đã tồn tại");
        }
        try {
            categoryRepository.save(category);
            return category;
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi thêm category");
        }
    }

    @Override
    public void updateCategory(String id, CategoryReq req) {

        // Check category exist
        Optional<Category> rs = categoryRepository.findById(id);

        // if not found id =>
        if (rs.isEmpty()) {
            throw new NotFoundException("Category không tồn tại");
        }

        Category category = rs.get();

        if (!req.getName().equalsIgnoreCase(rs.get().getName())) {
            if (categoryRepository.existsByName(req.getName())) {
                throw new BadRequestException("Tên danh mục đã tồn tại");
            }
        }
        category.setName(req.getName());

        Slugify slg = new Slugify();
        category.setSlug(slg.slugify(req.getName()));
        try {
            categoryRepository.save(category);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi chỉnh sửa category");
        }
    }

    @Override
    public void deleteCategory(String id) {
        Optional<Category> opt = categoryRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Category không tồn tại");
        }
        Category category = opt.get();
        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi xóa category");
        }
    }

    @Override
    public Page<Category> adminGetListCategory(String name, int page) {
        page--;
        if (page <= 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_CATEGORY , Sort.by("createdAt").descending());
        return categoryRepository.findByNameContaining( name,  pageable);
    }

}
