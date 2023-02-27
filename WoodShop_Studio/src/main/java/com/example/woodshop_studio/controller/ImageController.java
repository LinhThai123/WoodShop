package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.config.Contant;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.security.CustomUserDetails;
import com.example.woodshop_studio.service.ImageService;
import com.example.woodshop_studio.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ImageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService ;

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/api/upload-file")
    public ResponseEntity<?> uploadFileProduct(@RequestParam("file") MultipartFile file) {
        String path = productService.uploadFile(file);
        return ResponseEntity.ok(path);
    }
    @GetMapping (value = "/api/files/{fileId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] readFileProduct (@PathVariable String fileId) {
        return productService.readFile(fileId);
    }

    @GetMapping(value = "/api/files")
    public ResponseEntity<?> getUserFiles(@RequestParam(defaultValue = "1", required = false) Integer page){
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_IMAGE);
        Page<String> images = imageRepository.getListImageOfUser(user.getId() , pageable);
        return ResponseEntity.ok(images);
    }

}
