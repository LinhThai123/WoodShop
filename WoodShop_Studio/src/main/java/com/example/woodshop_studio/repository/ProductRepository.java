package com.example.woodshop_studio.repository;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Kiểm tra tên
    boolean existsByName (String name ) ;

    Product findByName(String name) ;

    Product getProductById(String id);

    Product getProductBySlug (String slug);

    Product getProductByIdAndSlug (String slug, String id);

    // lấy tất cả sản phẩm tìm kiếm theo id và name và
    Page<Product> findProductByIdOrNameContainingOrCategory_Name(String id , String name , String category , Pageable pageable) ;

}
