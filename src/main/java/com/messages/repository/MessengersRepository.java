package com.messages.repository;

import com.messages.entity.Messengers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessengersRepository extends JpaRepository<Messengers, Integer>
{
    // Lấy tất cả tin nhắn từ id Conversation
    //    inner join Conversation c on m.cvt_id = c.id
    @Query(value = "select * from Messenger m  where m.cvt_id=?1", nativeQuery = true)
    List<Messengers> getMessByIdConversation(Integer idConversation);

    @Query(value = "select * from Messenger m  where m.cvt_id=?1 and date(m.start_time)=?2", nativeQuery = true)
    Optional<Messengers> checkStartTime(Integer idConversation, String startTime);

    // Load tin nhắn cuối cùng
    @Query(value = "SELECT * FROM Messenger m WHERE m.cvt_id = (SELECT id FROM Conversation c WHERE "
            + "(c.user1 = ?1 and c.user2 = ?2) OR (c.user1 = ?2 and c.user2 = ?1)) and m.time_send = "
            + "(SELECT MAX(m.time_send) FROM Messenger m WHERE m.cvt_id = (SELECT id FROM Conversation c WHERE "
            + "(c.user1 = ?1 and c.user2 = ?2) OR (c.user1 = ?2 and c.user2 = ?1)))", nativeQuery = true)
    Messengers loadMessageIsRead(@Param("user1") Integer user1, @Param("user2") Integer user2);

    // Count tin nhắn mới
    @Query(value="SELECT COUNT(m.content) FROM Messenger  m WHERE m.cvt_id = (SELECT id FROM Conversation c WHERE "
            + "(c.user1 = ?1 and c.user2 = ?2) OR (c.user1 = ?2 and c.user2 = ?1)) and m.is_read = 0 and m.user_send != ?1", nativeQuery = true)
    Integer countNewMessage(@Param("user1") Integer user1, @Param("user2") Integer user2);

    // Cập nhật trạng thái đã đọc (is_read = 0 ==> is_read = 1)
    @Modifying
    @Transactional
    @Query(value = "UPDATE messenger m SET m.is_read = 1, m.time_read = :time_read WHERE m.cvt_id = :id_cvt AND m.user_send != :id_user_send", nativeQuery = true)
    void updateMessStatus(@Param("id_cvt") Integer id_cvt,@Param("time_read") Date time_read, @Param("id_user_send") Integer user_send);


}
