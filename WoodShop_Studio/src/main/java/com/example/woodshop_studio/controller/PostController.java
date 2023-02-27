package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.entity.Post;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.dto.PageableDto;
import com.example.woodshop_studio.model.request.PostReq;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.repository.PostRepository;
import com.example.woodshop_studio.security.CustomUserDetails;
import com.example.woodshop_studio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("admin/posts")
    public String getPostManagePage(Model model,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "post.created_at") String order,
                                    @RequestParam(defaultValue = "desc") String direction,
                                    @RequestParam(defaultValue = "") String title,
                                    @RequestParam(defaultValue = "") String status) {
        if (!status.equals("") && !status.equals("0") && !status.equals("1")) {
            return "error/err";
        }
        if (!direction.toLowerCase().equals("desc")) {
            direction = "asc";
        }
        PageableDto result = postService.adminGetListPost(title, status, page, order, direction);
        model.addAttribute("posts", result.getItems());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "admin/blog/list";
    }
//    //get list posts
//    @PostMapping("/api/admin/posts")
//    public ResponseEntity<Object> createPost(@Valid @RequestBody PostReq req) {
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        Post post = postService.createPost(req, user);
//        return ResponseEntity.ok(post);
//    }

    // get page create posts
    @GetMapping("/admin/posts/create")
    public String getPostCreatePage (Model model , PostReq req) {

        model.addAttribute("post", req);
        return "admin/blog/create";
    }

    // create new post server side
    @PostMapping("/admin/posts/create")
    public ModelAndView createPost(ModelMap model, @Valid @ModelAttribute(value = "post") PostReq req, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/blog/create");
        }
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Post post = postService.createPost(req,user);
        model.addAttribute("post", post);
        return new ModelAndView("redirect:/admin/posts", model);
    }
    // detail post by id and title
    @GetMapping("/admin/posts/{title}/{id}")
    public String getDetailPostPage (Model model , @PathVariable("title") String title , @PathVariable("id") String id) {
        try {
            // get info post
            Post post = postRepository.getPostByTitleAndId(title, id);
            model.addAttribute("post", post);

            // Get list image of user
            User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            List<String> images = imageRepository.getListImage(user.getId());
            model.addAttribute("images", images);

            return "admin/blog/detail";
        }
        catch (NotFoundException ex) {
            return "admin/error/err";
        }
    }
    // update post
    @PostMapping ("/admin/posts/update/{id}")
    public ModelAndView updateProduct(@Valid @PathVariable String id, @ModelAttribute("post") PostReq req, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            req.setId(id);
            return new ModelAndView("redirect:/admin/blog/detail", model);
        }
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Post post = postService.updatePost(id, req , user);
        model.addAttribute("product", post);
        return new ModelAndView("redirect:/admin/posts", model);
    }
    // delete post
    @GetMapping("/admin/posts/delete/{id}")
    public ModelAndView deletePost (ModelMap model , @PathVariable("id") String id) {
        Optional<Post> rs = postRepository.findById(id);
        if(!rs.isEmpty()){
            postService.deletePost(rs.get());
            model.addAttribute("message", "Xóa thành công");
            return new ModelAndView("redirect:/admin/posts", model);
        }
        else {
            model.addAttribute("message", "Xóa thất bại");
            return new ModelAndView("redirect:/admin/posts", model);
        }
    }
}
