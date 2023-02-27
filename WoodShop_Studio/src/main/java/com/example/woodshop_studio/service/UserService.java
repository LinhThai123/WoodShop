package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.model.dto.UserDTO;
import com.example.woodshop_studio.model.request.UserReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserReq infoUserByEmail (String email);

    List<UserDTO> getListUsers();

    public User getUserByName (String name) ;

    public User getUserById (String id);

    Page<User> adminGetListUsers (String name, String email, Integer page ) ;

    User createUser(UserReq userReq);

    public void deleteUser (User user);
}
