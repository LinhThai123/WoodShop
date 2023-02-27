package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.config.Contant;
import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.exception.BadRequestException;
import com.example.woodshop_studio.exception.InternalServerException;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.mapper.ProductMapper;
import com.example.woodshop_studio.model.request.ProductReq;
import com.example.woodshop_studio.repository.CategoryRepository;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.repository.ProductRepository;
import com.example.woodshop_studio.service.CategoryService;
import com.example.woodshop_studio.service.ImageService;
import com.example.woodshop_studio.service.ProductService;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceIpml implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository ;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageRepository imageRepository ;

    @Autowired
    private ImageService imageService ;

    @Override
    public Page<Product> adminGetListProduct(String id, String name,String category, Integer page) {

        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_PRODUCT, Sort.by("createdAt").descending());
        return productRepository.findProductByIdOrNameContainingOrCategory_Name(id, name,category, pageable);
    }

    @Override
    public String uploadFile( MultipartFile file) {
        return imageService.uploadFile(file);
    }

    @Override
    public byte[] readFile( String fileId) {
        return imageService.readFile(fileId);
    }

    @Override
    public List<Product> getListProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductReq req) {
        Product product = new Product();

        // kiểm tra tên product có bị trùng ko
        if(productRepository.existsByName(req.getName())){
            throw new BadRequestException("Name already exists");
        }
        product.setName(req.getName());

        //set slug
        Slugify slg = new Slugify();
        product.setSlug(slg.slugify(req.getName()));

        product.setPrice(req.getPrice());

        product.setQuantity(req.getQuantity());

        // get link in image
        if(req.getImage().isEmpty()) {
            throw new NotFoundException("Cannot image isEmpty");
        }
        product.setImage(req.getImage());

        product.setDescription(req.getDescription());
        //set status
        product.setStatus(req.getStatus());

        if(req.getCategory_id().isEmpty()) {
            throw new BadRequestException("Category not isEmpty");
        }
        Category category = categoryService.getCategoryById(req.getCategory_id());
        product.setCategory(category);
        try {
            productRepository.save(product);

        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi thêm sản phẩm");
        }
        return product;
    }

    @Override
    public Product getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()) {
            throw new NotFoundException("Product does not exist") ;
        }
        return product.get();
    }

    @Override
    public Product getProductBySlug(String slug) {
        Optional<Product> product = Optional.ofNullable(productRepository.getProductBySlug(slug));
        if(!product.isPresent()) {
            throw new NotFoundException("Slug of product does not exist") ;
        }
        return product.get();
    }

    @Override
    public Product updateProduct(String id, ProductReq req) {
        // Check product exits
        Product product;
        Optional<Product> rs = productRepository.findById(id);
        product = rs.get();
        if(!rs.isPresent()) {
            throw new NotFoundException("Product do not exits");
        }
//        if(req.getImage().isEmpty()) {
//            throw new BadRequestException("Image not isEmpty") ;
//        }
        product.setName(req.getName());
        //set slug
        Slugify slg = new Slugify();
        product.setSlug(slg.slugify(req.getName()));

        product.setPrice(req.getPrice());

        product.setImage(req.getImage());

        product.setStatus(req.getStatus());

        product.setDescription(req.getDescription());

        product.setQuantity(req.getQuantity());

        // Validate info
        if(req.getCategory_id().isEmpty()) {
            throw new BadRequestException("Category not isEmpty");
        }
        Category category = categoryService.getCategoryById(req.getCategory_id());
        product.setCategory(category);
        try {
            productRepository.save(product);
        }
        catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật thông tin sản phẩm");
        }
        return product;
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }


}
