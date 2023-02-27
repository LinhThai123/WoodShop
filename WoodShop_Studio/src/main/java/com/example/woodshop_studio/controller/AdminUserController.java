package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.dto.UserDTO;
import com.example.woodshop_studio.model.request.UserReq;
import com.example.woodshop_studio.repository.ImageRepository;
import com.example.woodshop_studio.repository.UserRepository;
import com.example.woodshop_studio.security.CustomUserDetails;
import com.example.woodshop_studio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository ;

    @GetMapping("/admin/users")
    public String usersPages(Model model,
                             @RequestParam(defaultValue = "", required = false) String name,
                             @RequestParam(defaultValue = "", required = false) String email,
                             @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<User> users = userService.adminGetListUsers(name, email, page);
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", users.getPageable().getPageNumber() + 1);
        return "admin/user/list";
    }
    // Thêm sản phẩm client side
//    @PostMapping("/api/admin/users")
//    public ResponseEntity<?> createProduct(@Valid @RequestBody UserReq req) {
//        User user = userService.createUser(req);
//        return ResponseEntity.ok(user);
//    }
    @GetMapping("/admin/users/create")
    public String getUserCreatePage (Model model , UserReq req){
        model.addAttribute("user" , req);
        return "admin/user/create";
    }
    // create user for admin
    @PostMapping("/admin/users/create")
    public ModelAndView createUser (ModelMap model, @Valid @ModelAttribute(value = "user") UserReq req , BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/user/list");
        }
        User user = userService.createUser(req);
        model.addAttribute("user", user);
        return new ModelAndView("redirect:/admin/users", model);
    }

    // detail user
    @GetMapping("/admin/users/{name}/{id}")
    public String getDetailUserPage(Model model, @PathVariable("id") String id , @PathVariable("name") String name) {
        try {
            // Get info
            User user = userService.getUserById(id);
            model.addAttribute("user", user);

            // get info follow name
            User username = userService.getUserByName(name);
            model.addAttribute("username" ,username);

            // Get list image of user
            User user1 = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            List<String> images = imageRepository.getListImage(user1.getId());
            model.addAttribute("images", images);

            return "admin/user/detail";
        } catch (NotFoundException ex) {
            return "admin/error/err";
        }
    }
    // delete user
    @GetMapping("admin/users/delete/{name}/{id}")
    public ModelAndView deleteUser (ModelMap model, @PathVariable("id") String id, @PathVariable("name") String name) {
        Optional<User> rs = Optional.ofNullable(userRepository.getUserByIdAndName(id,name));
        if(rs.isPresent()){
            userService.deleteUser(rs.get());
            model.addAttribute("message", "Xóa thành công");
            return new ModelAndView("redirect:/admin/users", model);
        }
        else {
            model.addAttribute("message", "Xóa thất bại");
            return new ModelAndView("redirect:/admin/users", model);
        }
    }
    @GetMapping("/api/auth/login")
    public String getLoginPage() {
        return "client/login";
    }

    @GetMapping("/api/auth/signup")
    public String getRegisterPage() {
        return "client/register";
    }
}
