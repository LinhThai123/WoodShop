package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.request.CategoryReq;
import com.example.woodshop_studio.model.request.ProductReq;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.repository.ProductRepository;
import com.example.woodshop_studio.security.CustomUserDetails;
import com.example.woodshop_studio.service.CategoryService;
import com.example.woodshop_studio.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/admin/products")
    public String getAdminProducts(Model model,
                                   @RequestParam(defaultValue = "", required = false) String id,
                                   @RequestParam(defaultValue = "", required = false) String name,
                                   @RequestParam(defaultValue = "", required = false) String category,
                                   @RequestParam(defaultValue = "1", required = false) Integer page) {
        // Lấy danh sách categories
        List<Category> categories = categoryService.getListCategory();
        model.addAttribute("categories", categories);

        // Lấy danh sách sản phẩm
        Page<Product> products = productService.adminGetListProduct(id, name, category, page);
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getPageable().getPageNumber() + 1);
        return "admin/product/list";
    }

    // get list category
    @ModelAttribute("categories")
    public List<CategoryReq> getCategories() {
        return categoryService.getListCategory().stream()
                .map(item -> {
                    CategoryReq rep = new CategoryReq();
                    modelMapper.map(item, rep);
                    return rep;
                }).collect(Collectors.toList());
    }

    @GetMapping("/admin/products/save")
    public String getProductCreatePage(Model model, ProductReq req) {

        model.addAttribute("product", req);
        return "admin/product/save";
    }

    // Thêm sản phẩm client side
//    @PostMapping("/api/admin/products")
//    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductReq req) {
//        Product product = productService.createProduct(req);
//        return ResponseEntity.ok(product);
//    }

    // create new product server side
    @PostMapping("/admin/products/save")
    public ModelAndView createProduct(ModelMap model, @Valid @ModelAttribute(value = "product") ProductReq req, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/product/save");
        }

        Product product = productService.createProduct(req);
        model.addAttribute("product", product);
        return new ModelAndView("redirect:/admin/products", model);
    }

    // detail product by id
    @GetMapping("/admin/products/{slug}/{id}")
    public String getDetailProducPage(Model model, @PathVariable("id") String id , @PathVariable("slug") String slug) {
        try {
            // Get info
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            // get info follow slug
            Product productSlug = productService.getProductBySlug(slug);
            model.addAttribute("productSlug" , productSlug);

            // Get list image of user
            User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            List<String> images = imageRepository.getListImage(user.getId());
            model.addAttribute("images", images);

            return "admin/product/detail";
        } catch (NotFoundException ex) {
            return "admin/error/err";
        }
    }
    // update product by id follow client side
//    @PutMapping("/api/admin/products/{id}")
//    public ResponseEntity<?> updateProduct (@PathVariable String id , @Valid @RequestBody ProductReq req) {
//        productService.updateProduct(id,req);
//        return ResponseEntity.ok("Cập nhật thành công");
//    }

    // update product by id follow server side
    @PostMapping ("/admin/products/update/{id}")
    public ModelAndView updateProduct(@Valid @PathVariable String id, @ModelAttribute("product") ProductReq req, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            req.setId(id);
            return new ModelAndView("redirect:/admin/products/detail", model);
        }
        Product product = productService.updateProduct(id, req);
        model.addAttribute("product", product);
        return new ModelAndView("redirect:/admin/products", model);
    }

    // delete product by id follow client side
//    @DeleteMapping("/api/admin/products/{slug}/{id}")
//    public ResponseEntity<?> deleteProduct (@PathVariable("id") String id , @PathVariable("slug") String slug) {
//        productService.deleteProduct(id, slug);
//        return ResponseEntity.ok("Xóa thàng công");
//    }
    // delete product by id , slug follow server side
    @GetMapping("admin/products/delete/{slug}/{id}")
    public ModelAndView deleteProduct (ModelMap model, @PathVariable("id") String id, @PathVariable("slug") String slug) {
        Optional<Product> rs = Optional.ofNullable(productRepository.getProductByIdAndSlug(id,slug));
        if(rs.isPresent()){
            productService.deleteProduct(rs.get());
            model.addAttribute("message", "Xóa thành công");
            return new ModelAndView("redirect:/admin/products", model);
        }
        else {
            model.addAttribute("message", "Xóa thất bại");
            return new ModelAndView("redirect:/admin/products", model);
        }
    }
}
