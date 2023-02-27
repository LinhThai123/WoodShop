package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.model.request.CategoryReq;
import com.example.woodshop_studio.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller

public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Hiện thị danh mục tìm kiếm theo tên và phân trang
    @GetMapping("/admin/categories")
    public String homePage(Model model,
                           @RequestParam(defaultValue = "" , required = false) String name,
                           @RequestParam(defaultValue = "1",required = false) Integer page) {
        Page<Category> category = categoryService.adminGetListCategory(name,page);
        List<Category> categories = category.getContent();
        model.addAttribute("categories",categories);
        model.addAttribute("totalPages",category.getTotalPages());
        model.addAttribute("currentPage", category.getPageable().getPageNumber() + 1);

        return "admin/category/list";
    }

    @GetMapping("/api/admin/categories")
    public ResponseEntity<Object> adminGetListCategories(@RequestParam(defaultValue = "",required = false) String name,
                                                         @RequestParam(defaultValue = "0",required = false) Integer page){
        Page<Category> categories = categoryService.adminGetListCategory(name,page);
        return ResponseEntity.ok(categories);

    }
    // Thêm danh mục mới
    @PostMapping("/api/admin/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryReq req) {
        Category category = categoryService.createCategory(req);
        return ResponseEntity.ok(category);
    }
    // sửa danh mục
    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryReq req, @PathVariable String id) {
        categoryService.updateCategory(id, req);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    // xóa danh mục
    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
