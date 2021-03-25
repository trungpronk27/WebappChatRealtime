package com.messages.repository;

import com.messages.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    // Tìm username login
    User findByUsername(String username);

    // lấy user nằm trong list bạn bè ngoại trừ user login
//    @Query(value = "select * " +
//            "from User u, friend f " +
//            "where ((f.friend_send = ?1 and u.id = friend_reply) or (f.friend_reply = ?1 and u.id = friend_send)) and (f.status = 0 or f.status = 1 or f.status = 2) ", nativeQuery = true)
//    List<User> listExceptUserChat (Integer exceptUsername);

    // lấy user nằm trong list bạn bè ngoại trừ user login
    @Query(value = "SELECT u.* from user u, friend f "
            + "WHERE ((f.friend_send = :idLogin and u.id = friend_reply) OR (f.friend_reply = :idLogin and u.id = friend_send)) "
            + "AND (f.status = 0 or f.status = 1 or f.status = 2) "
            + "AND u.id NOT IN (SELECT u.id FROM user u, (SELECT c.id, m.cvt_id, c.user1, c.user2, m.time_send "
            + "FROM Messenger m , conversation c "
            + "WHERE m.cvt_id = c.id AND m.time_send = (SELECT MAX(m.time_send) FROM Messenger m "
            + "WHERE m.cvt_id = c.id)) ex "
            + "WHERE (u.id = ex.user1 AND ex.user2 = :idLogin) OR (ex.user1 = :idLogin AND u.id = ex.user2) "
            + "ORDER BY ex.time_send DESC) ", nativeQuery = true)
    List<User> listExceptUserChat(@Param("idLogin")Integer exceptUsername);

    // Lấy user theo thời gian chat (mới nhất -> lâu nhất)
    @Query(value = "SELECT u.*, ex.time_send FROM user u, friend f, "
            +"(SELECT c.id, m.cvt_id, c.user1, c.user2, m.time_send FROM Messenger m , conversation c "
            +"WHERE m.cvt_id = c.id AND m.time_send =(SELECT MAX(m.time_send) FROM Messenger m WHERE m.cvt_id = c.id)) ex "
            +"WHERE ((u.id = ex.user1 AND ex.user2 = :idLogin) OR (ex.user1 = :idLogin AND u.id = ex.user2)) "
            +"GROUP BY u.id "
            +"ORDER BY ex.time_send DESC ", nativeQuery = true)
    List<User> listSoftByTimeChat(@Param("idLogin") Integer idLogin);


    // search user
    @Query(value = "select * " +
            "from User u, friend f " +
            "where ((f.friend_send = ?1 and u.id = friend_reply) or (f.friend_reply = ?1 and u.id = friend_send)) and (f.status <> 3) and u.first_name = ?2", nativeQuery = true)
    List<User> search(Integer idUser, String key);

    // update img
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set user_img = :userImg where u.id = :userId", nativeQuery = true)
    void updateUserImg(@Param("userImg") String userImg, @Param("userId") Integer userId);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.resetPasswordToken = ?1")
    User findByResetPasswordToken(String resetPasswordToken);

    @Query("select u from User u where u.id = ?1")
    User findByUserId(Integer userId);

    // update trạng thái hoạt động
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.time_active = ?1 where u.id = ?2", nativeQuery = true)
    void updateTimeActive(@Param("time_active") Date time_active, @Param("id_user") int id_user);
}
