package com.example.woodshop_studio.model.mapper;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.model.request.ProductReq;
import com.github.slugify.Slugify;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public class ProductMapper {
    public static Product toProduct(ProductReq req) {
        Product product = new Product();
        product.setName(req.getName());

        //set slug
        Slugify slg = new Slugify();
        product.setSlug(slg.slugify(req.getName()));

        product.setPrice(req.getPrice());
        product.setImage(req.getImage());
        product.setStatus(req.getStatus());
        product.setDescription(req.getDescription());
        product.setQuantity(req.getQuantity());
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        // Set category
//        Category category = new Category();
//        category.setId(req.getCategory_id());
//        product.setCategory(category);

        return product;
    }
}
