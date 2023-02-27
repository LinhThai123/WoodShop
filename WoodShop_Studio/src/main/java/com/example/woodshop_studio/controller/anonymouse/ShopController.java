package com.example.woodshop_studio.controller.anonymouse;

import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.service.CategoryService;
import com.example.woodshop_studio.service.PostService;
import com.example.woodshop_studio.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {

    @Autowired
    private PostService postService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageRepository imageRepository ;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String getHomePage (Model model) {
        // get new products
        // get
        return "client/index";
    }
}
