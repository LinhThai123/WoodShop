package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.config.Contant;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.exception.BadRequestException;
import com.example.woodshop_studio.exception.NotFoundException;
import com.example.woodshop_studio.model.dto.UserDTO;
import com.example.woodshop_studio.model.mapper.UserMapper;
import com.example.woodshop_studio.model.request.UserReq;
import com.example.woodshop_studio.repository.UserRepository;
import com.example.woodshop_studio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceIpml implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserReq infoUserByEmail(String email) {
        User user = userRepository.getByEmail(email);
        UserReq userReq = UserReq.builder()
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .role(user.getRoles())
                .build();
        return userReq;
    }

    @Override
    public List<UserDTO> getListUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(UserMapper.toUserDTO(user));
        }
        return userDTOS;
    }

    @Override
    public User getUserByName(String name) {
        Optional<User> user = Optional.ofNullable(userRepository.getUserByName(name));
        if(!user.isPresent()) {
            throw new NotFoundException("Name of user does not exist") ;
        }
        return user.get();
    }

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new NotFoundException("User does not exist") ;
        }
        return user.get();
    }

    @Override
    public Page<User> adminGetListUsers(String name, String email, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_USER, Sort.by("createdAt").descending());
        return userRepository.findByNameContainingOrEmail(name, email, pageable);
    }

    @Override
    public User createUser(UserReq userReq) {
        User user = new User();

        user.setName(userReq.getName());

        String hash = BCrypt.hashpw(userReq.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        // check trungf email
        if(userRepository.existsByEmail(userReq.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        user.setEmail(userReq.getEmail());

        user.setPhone(userReq.getPhone());

        user.setAddress(userReq.getAddress());

        user.setAvatar(userReq.getAvatar());

        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        user.setStatus(true);

        user.setRoles(new ArrayList<>(Arrays.asList("USER")));

        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
