package com.example.woodshop_studio.model.mapper;

import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.model.dto.UserDTO;
import com.example.woodshop_studio.model.request.UserReq;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;


public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public static User toUser(UserReq userReq) {

        User user = new User();
        user.setName(userReq.getName());
        user.setEmail(userReq.getEmail());
        String hash = BCrypt.hashpw(userReq.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        user.setPhone(userReq.getPhone());
        user.setAddress(userReq.getAddress());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setStatus(true);
        user.setRoles(new ArrayList<>(Arrays.asList("USER")));
        return user;
    }
}
