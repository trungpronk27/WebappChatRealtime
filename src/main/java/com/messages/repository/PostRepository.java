package com.messages.repository;

import com.messages.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "select * from Post p where p.user_id = :userId order by p.id desc" , nativeQuery = true)
    List<Post> getPost(@Param("userId") Integer userId);



    @Modifying // jpa không hỗ trợ native insert nên phải dùng kèm Annotation @Modifying
    @Transactional
    @Query(value = "insert into post (post_text, post_img, user_id, created_at, updated_at) values (:post_text, :post_img, :user_id, :created_at, :updated_at)", nativeQuery = true)
    void savePosts(@Param("post_text") String post_text, @Param("post_img") String post_img, @Param("user_id") Integer user_id, @Param("created_at") Date created_at, @Param("updated_at") Date updated_at);
}
