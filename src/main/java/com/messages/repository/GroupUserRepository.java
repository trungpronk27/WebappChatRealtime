package com.messages.repository;

import com.messages.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Integer> {

    @Query(value = "select * from group_user gu where gu.group_id = :group_id", nativeQuery = true)
    List<GroupUser> getUserByIdGroup(@Param("group_id") Integer group_id);

    @Modifying // jpa không hỗ trợ native insert nên phải dùng kèm Annotation @Modifying
    @Transactional
    @Query(value = "insert into group_user (group_id, user_id, is_admin, created_at, updated_at) values (:group_id, :user_id, :is_admin, :created_at, :updated_at)", nativeQuery = true)
    void saveUser(@Param("group_id") Integer group_id, @Param("user_id") Integer user_id, @Param("is_admin") Integer is_admin, @Param("created_at") Date created_at, @Param("updated_at") Date updated_at);

    @Query(value = "select * from group_user gu where gu.group_id = :group_id and gu.user_id =:user_id", nativeQuery = true)
    String check(@Param("group_id") Integer group_id, @Param("user_id") Integer user_id);
}
