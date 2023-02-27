package com.example.woodshop_studio.repository;

import com.example.woodshop_studio.entity.Post;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    @Query(nativeQuery = true, name = "adminGetListPost")
    public List<PostDTO> adminGetListPost(@Param("title") String title, @Param("status") String status, @Param("limit") int limit, @Param("offset") int offset, @Param("order") String order, @Param("direction") String direction);

    // count theo id
    @Query(nativeQuery = true, value = "SELECT count(id)\n" +
            "FROM post\n"+
            "WHERE title LIKE CONCAT('%',:title,'%') AND status LIKE CONCAT('%',:status,'%')\n")
    public int countPostFilter(@Param("title") String title, @Param("status") String status);

    // find post by id
    Post getPostById (String id) ;

    // find post by title
    Post getPostByTitle (String title);

    Post getPostByTitleAndId (String title , String id);
}
