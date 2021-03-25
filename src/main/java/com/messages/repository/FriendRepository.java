package com.messages.repository;

import com.messages.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
     //check conversation đã có 2 user chat chưa
    @Query(value = "select * " +
            " from Friend f " +
            " where ((f.friend_send = :friend_send and f.friend_reply = :friend_reply) or (f.friend_send = :friend_reply and f.friend_reply = :friend_send))", nativeQuery = true)
    Friend checkFriend (@Param("friend_send") Integer friend_send, @Param("friend_reply") Integer friend_reply);

    @Modifying // jpa không hỗ trợ native insert nên phải dùng kèm Annotation @Modifying
    @Transactional
    @Query(value = "insert into Friend (friend_send, friend_reply, status) values (:friend_send, :friend_reply, 0)", nativeQuery = true)
    void saveFriend (@Param("friend_send") Integer friend_send, @Param("friend_reply") Integer friend_reply);

    // Count the user's friends
    @Query(value = "select count(*) from friend f where (f.friend_send = :idUser or f.friend_reply = :idUser) and f.status = 2", nativeQuery = true)
    Integer countFriend(@Param("idUser") Integer idUser);

    // Check status
    @Query(value = "select * " +
            " from Friend f " +
            " where (f.friend_send = :friend_send and f.friend_reply = :friend_reply) or (f.friend_send = :friend_reply and f.friend_reply = :friend_send)", nativeQuery = true)
    Friend checkStatus(@Param("friend_send") Integer friend_send, @Param("friend_reply") Integer friend_reply);

    // List user block
    @Query(value = "select * " +
            " from Friend f " +
            " where f.friend_send = :friend_send and f.status = 3", nativeQuery = true)
    List<Friend> listBlock(@Param("friend_send") Integer friend_send);

    // List user block
    @Query(value = "select * " +
            " from Friend f " +
            " where (f.friend_send = :friend_send or f.friend_reply = :friend_send) and f.status = 2", nativeQuery = true)
    List<Friend> listFriend(@Param("friend_send") Integer friend_send);

}
