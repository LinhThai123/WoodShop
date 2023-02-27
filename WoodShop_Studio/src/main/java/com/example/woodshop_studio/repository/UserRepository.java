package com.example.woodshop_studio.repository;

import com.example.woodshop_studio.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail (String email);

    User getByEmail(String email);

    User getUserById (String id);

    User getUserByName (String name);

    User getUserByIdAndName (String id , String name) ;

    // lấy thông tin user
//    @Query (nativeQuery = true , value = "select u from User u where CONCAT (u.name = :name LIKE %?1%  AND u.email = :email LIKE %?2% AND u.phone = :phone LIKE %?3%)")
//    Page<User> adminGetUserList (@Param("name") String name , @Param("email") String email , @Param("phone") String phone , Pageable pageable);

    Page<User> findByNameContainingOrEmail(String name, String email, Pageable pageable);
}
