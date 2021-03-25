package com.messages.repository;

import com.messages.entity.Comment;
import com.messages.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // lấy comment từ bài post
    @Query(value = "select c.* , u.* " +
            "from Comment c inner join post p on c.post_id = p.id left join user u on u.id = c.user_id " +
            "where p.id = :postId ", nativeQuery = true)
    List<Comment> getComment(@Param("postId") Integer postId);
   // List<Object[]> getComment(@Param("postId") Integer postId);

}
