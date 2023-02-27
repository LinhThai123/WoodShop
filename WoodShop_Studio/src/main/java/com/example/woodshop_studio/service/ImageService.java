package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public interface ImageService {

    void createFolder (String path);

    String uploadFile (MultipartFile file) ;

    String getFileExtension(String  fileName);

    boolean checkFileExtension(String fileExtension );

    byte[] readFile(String fileId);

    void saveImage (Image image) ;

}
