package com.messages.repository;

import com.messages.entity.MessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessGroupRepository extends JpaRepository<MessGroup, Integer> {

    @Query(value = "select * from mess_group mg  where mg.group_id=?1 and date(mg.start_time)=?2", nativeQuery = true)
    Optional<MessGroup> checkStartTime(Integer idGroup, String startTime);

    // Load tin nhắn cuối cùng (chat nhóm)
    @Query(value = "SELECT * FROM mess_group m WHERE m.group_id = :id_cvt and m.time_send = "
            + "(SELECT MAX(m.time_send) FROM mess_group m WHERE m.group_id = :id_cvt)", nativeQuery = true)
    MessGroup loadLastMessageInGroup(@Param("id_cvt") Integer id_cvt);
}
