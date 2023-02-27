package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.model.request.ProductReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {

    // upload file
    String uploadFile (MultipartFile file);

    // read file
    byte[] readFile( String fileId);

    // get list product
    public List<Product> getListProduct();

    // Lấy danh sách sản phẩm có phân trang
    Page<Product> adminGetListProduct(String id, String name, String category ,  Integer page);

    // create new product
    public Product createProduct (ProductReq req);

    // get product by id
    public Product getProductById (String id) ;

    // get product by slug
    public Product getProductBySlug (String slug);

    // update product by id
    public Product updateProduct (String id , ProductReq req);

    // delete product by id , slug
    public void deleteProduct (Product product);
}
