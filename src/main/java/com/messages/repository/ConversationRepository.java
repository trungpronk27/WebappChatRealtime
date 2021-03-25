package com.messages.repository;

import com.messages.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    // check conversation đã có 2 user chat chưa
    @Query(value = "select * " +
            "from Conversation c " +
            "where ((c.user1 = :user1 and c.user2 = :user2) or (c.user1 = :user2 and c.user2 = :user1))", nativeQuery = true)
    Conversation checkConversation(@Param("user1") Integer user1, @Param("user2") Integer user2);


    @Modifying // jpa không hỗ trợ native insert nên phải dùng kèm Annotation @Modifying
    @Transactional
    @Query(value = "insert into Conversation (user1, user2) values (:user1, :user2)", nativeQuery = true)
    void saveConversation(@Param("user1") Integer user1, @Param("user2") Integer user2);

}
