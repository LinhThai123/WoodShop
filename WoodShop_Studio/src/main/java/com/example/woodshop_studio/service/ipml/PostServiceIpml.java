package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.config.Contant;
import com.example.woodshop_studio.entity.Post;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.BadRequestException;
import com.example.woodshop_studio.exception.InternalServerException;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.dto.PageableDto;
import com.example.woodshop_studio.model.dto.PostDTO;
import com.example.woodshop_studio.model.request.PostReq;
import com.example.woodshop_studio.repository.PostRepository;
import com.example.woodshop_studio.service.PostService;
import com.example.woodshop_studio.util.PageUtill;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class PostServiceIpml implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public PageableDto adminGetListPost(String title, String status, int page, String order, String direction) {
        int limit = 15;
        PageUtill pageInfo = new PageUtill(limit, page);

        // Get list posts and totalItems
        List<PostDTO> posts = postRepository.adminGetListPost(title, status, limit, pageInfo.calculateOffset(), order, direction);
        int totalItems = postRepository.countPostFilter(title, status);

        int totalPages = pageInfo.calculateTotalPage(totalItems);

        return new PageableDto(posts, totalPages, pageInfo.getPage());
    }

    @Override
    public Post createPost(PostReq req, User user) {
        Post post = new Post();

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());

        Slugify slugify = new Slugify();
        post.setSlug(slugify.slugify(req.getTitle()));
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setCreatedBy(user);

        if (req.getStatus() == Contant.PUBLIC_POST) {
            // Public post
            if (req.getDescription().isEmpty()) {
                throw new BadRequestException("Để công khai bài viết vui lòng nhập mô tả ");
            }
            if (req.getThumbnail().isEmpty()) {
                throw new BadRequestException("Vui lòng chọn ảnh cho bài viết trước khi công khai");
            }
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            if (req.getStatus() != Contant.DRAFT_POST) {
                throw new BadRequestException("Trang thái bài viết không hợp lệ ");
            }
        }
        post.setDescription(req.getDescription());
        post.setThumbnail(req.getThumbnail());
        post.setStatus(req.getStatus());

        postRepository.save(post);
        return post;
    }

    //TODO update tin tức
    @Override
    public Post updatePost(String id, PostReq req, User user) {
        Optional<Post> rs = postRepository.findById(id);
        Post post = rs.get();
        if (rs.isEmpty()) {
            throw new NotFoundException("Bài viết không tồn tại");
        }
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        Slugify slg = new Slugify();
        post.setSlug(slg.slugify(req.getTitle()));
        post.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        post.setModifiedBy(user);
        if (req.getStatus() == Contant.PUBLIC_POST) {
            // public post
            if (req.getDescription().isEmpty()) {
                throw new BadRequestException("Để công khai bài viết vui lòng nhập mô tả");
            }
            if (req.getThumbnail().isEmpty()) {
                throw new BadRequestException("Vui lòng chọn ảnh bài viết trước khi công khai");
            }
            if (post.getPublishedAt() == null) {
                post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            }
        } else {
            if (req.getStatus() != Contant.DRAFT_POST) {
                throw new BadRequestException("Trang thái bài viết không hợp lệ");
            }
        }
        post.setDescription(req.getDescription());
        post.setThumbnail(req.getThumbnail());
        post.setStatus(req.getStatus());
        try {
            postRepository.save(post);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi cập nhật tin tức");
        }
        return post;
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
