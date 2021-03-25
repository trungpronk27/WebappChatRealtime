package com.messages.repository;

import com.messages.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<Group, Integer> {

    // hiển thị group có bản thân nằm trong đó
//    @Query(value = "select distinct g.* " +
//            "from Groups g INNER JOIN group_user gu ON g.id = gu.group_id inner join mess_group mg on g.id = mg.group_id " +
//            "where gu.user_id = :userId group by g.id order by mg.time_send ASC  ", nativeQuery = true)
//    List<Group> getUserGroup(@Param("userId") Integer userId);

    // Get List Group Normal
    @Query(value = "SELECT g.* FROM groups g "
            + "INNER JOIN group_user gu "
            + "ON gu.group_id = g.id "
            + "WHERE gu.user_id = :userId "
            + "AND g.id NOT IN (SELECT ex.id FROM (SELECT groups.* "
            + "FROM group_user c, mess_group m "
            + "INNER JOIN groups ON m.group_id = groups.id "
            + "WHERE m.group_id = c.group_id AND m.time_send =(SELECT MAX(m.time_send)  FROM mess_group "
            + "WHERE m.group_id = c.group_id) "
            + "GROUP BY c.group_id "
            + "ORDER BY m.time_send DESC) ex "
            + "INNER JOIN group_user gu ON ex.id = gu.group_id "
            + "WHERE gu.user_id = :userId) ", nativeQuery = true)
    List<Group> getUserGroup(@Param("userId") Integer userId);

    // Get List Group By ChatTime
    @Query(value = "SELECT ex.* FROM (SELECT groups.*, m.time_send "
            + "FROM group_user c, mess_group m "
            + "INNER JOIN groups ON m.group_id = groups.id "
            + "WHERE m.group_id = c.group_id AND m.time_send =(SELECT MAX(mg.time_send) FROM  mess_group mg "
            + "WHERE mg.group_id = c.group_id) "
            + "GROUP BY m.group_id "
            + "ORDER BY m.time_send DESC) ex "
            + "INNER JOIN group_user gu ON ex.id = gu.group_id "
            + "WHERE gu.user_id = :userId "
            + "ORDER BY ex.time_send DESC", nativeQuery = true)
    List<Group> getGroupByTimeChat(@Param("userId") Integer userID);


    // Lấy tất cả tin nhắn từ Mess Group bởi id group
    @Query(value = "select distinct g.* from Groups g inner join mess_group mg on g.id = mg.group_id where mg.group_id = :idGroup", nativeQuery = true)
    List<Group> getMessByIdGroupChat(@Param("idGroup") Integer idGroup);

    // Kiểm tra user có nằm trong group không
    @Query(value = "select * " +
            "from group_user gu " +
            "where gu.group_id = :idGroup and gu.user_id = :userId ", nativeQuery = true)
    String checkGroup(@Param("idGroup") Integer idGroup, @Param("userId") Integer userId);

    // Tìm kiếm group
    @Query(value = "select distinct g.* from Groups g inner join group_user gu on g.id = gu.group_id where g.group_name like %:key% and gu.user_id=:user_id", nativeQuery = true)
    List<Group> searchGroup(@Param("key") String key, @Param("user_id") Integer user_id);
}
