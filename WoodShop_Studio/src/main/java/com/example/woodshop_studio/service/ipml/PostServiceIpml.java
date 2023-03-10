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
                throw new BadRequestException("????? c??ng khai b??i vi???t vui l??ng nh???p m?? t??? ");
            }
            if (req.getThumbnail().isEmpty()) {
                throw new BadRequestException("Vui l??ng ch???n ???nh cho b??i vi???t tr?????c khi c??ng khai");
            }
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            if (req.getStatus() != Contant.DRAFT_POST) {
                throw new BadRequestException("Trang th??i b??i vi???t kh??ng h???p l??? ");
            }
        }
        post.setDescription(req.getDescription());
        post.setThumbnail(req.getThumbnail());
        post.setStatus(req.getStatus());

        postRepository.save(post);
        return post;
    }

    //TODO update tin t???c
    @Override
    public Post updatePost(String id, PostReq req, User user) {
        Optional<Post> rs = postRepository.findById(id);
        Post post = rs.get();
        if (rs.isEmpty()) {
            throw new NotFoundException("B??i vi???t kh??ng t???n t???i");
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
                throw new BadRequestException("????? c??ng khai b??i vi???t vui l??ng nh???p m?? t???");
            }
            if (req.getThumbnail().isEmpty()) {
                throw new BadRequestException("Vui l??ng ch???n ???nh b??i vi???t tr?????c khi c??ng khai");
            }
            if (post.getPublishedAt() == null) {
                post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            }
        } else {
            if (req.getStatus() != Contant.DRAFT_POST) {
                throw new BadRequestException("Trang th??i b??i vi???t kh??ng h???p l???");
            }
        }
        post.setDescription(req.getDescription());
        post.setThumbnail(req.getThumbnail());
        post.setStatus(req.getStatus());
        try {
            postRepository.save(post);
        } catch (Exception ex) {
            throw new InternalServerException("L???i khi c???p nh???t tin t???c");
        }
        return post;
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
