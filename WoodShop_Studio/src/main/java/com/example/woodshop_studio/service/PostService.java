package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.Post;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.model.dto.PageableDto;
import com.example.woodshop_studio.model.request.PostReq;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    //  // Lấy danh sách tin tức có phân trang
    public PageableDto adminGetListPost(String title, String status, int page, String order, String direction);


    // tạo tin tức theo user
    Post createPost (PostReq req , User user) ;

    // update tin tức theo id
    public Post updatePost (String id , PostReq req , User user);

    // delete post
    public void deletePost (Post post) ;
}
