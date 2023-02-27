package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.entity.Image;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.BadRequestException;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.repository.ProductRepository;
import com.example.woodshop_studio.security.CustomUserDetails;
import com.example.woodshop_studio.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ImageServiceIpml implements ImageService {

    private final Path rootDir = Paths.get("src/main/resources/static/uploads");

    @Autowired
    private ImageRepository imageRepository ;

    public ImageServiceIpml() {
        createFolder(rootDir.toString());
    }

    @Override
    public void createFolder(String path) {
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {

        String user_id = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        Path pathDir = Paths.get("src/main/resources/static/uploads");
        createFolder(pathDir.toString());

        // B2 CheckValidate

        // Kiểm tra tên file
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.equals("")) {
            throw new BadRequestException(" Name File do not exits");
        }
        // Kiểm tra đuôi file
        String fileExtenSion = getFileExtension(fileName);
        if(!checkFileExtension(fileExtenSion)){
            throw  new BadRequestException("Vui lòng chỉ upload file có các định đạng png,jpg,jpeg");
        }
        // Kiểm tra size
        if((double)file.getSize() / 1_000_000L >2){
            throw  new BadRequestException(("File không được vượt quá 2 MB"));
        }

        //Tạo Path tương ứng với file Upload lên
        String generateFileName = UUID.randomUUID().toString() + fileName;
        File serverFile = new File(pathDir.toString()+"/"+ generateFileName);

        try {
            // Sử dụng Buffer để lưu dữ liệu
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(file.getBytes());
            stream.close();

            String filePath = "/api/files/" + generateFileName;

            Image image = Image.builder().id(generateFileName).name(fileName).link(filePath).size(file.getSize()).createdBy(user_id).build();
            imageRepository.save(image);
            return filePath;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Lỗi Khi upload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf+1);
    }

    @Override
    public boolean checkFileExtension(String fileExtension) {
        List<String> fileExtensions = Arrays.asList("png","jpg","jpeg","webp");
        return fileExtensions.contains(fileExtension);
    }

    @Override
    public byte[] readFile(String fileId) {
        // Lấy đường dẫn file tương ứng với user_id
        Path userPath = rootDir;

        // Kiểm tra đường dẫn file có tồn tại hay không
        if (!Files.exists(userPath)) {
            throw new RuntimeException("Không thể đọc file : " + fileId);
        }
        try {
            // Lấy đường dẫn file tương ứng với user_id và file_name
            Path file = userPath.resolve(fileId);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException("Không thể đọc file: " + fileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể đọc file : " + fileId);
        }
    }

    @Override
    public void saveImage(Image image) {
        imageRepository.save(image);
    }
}
