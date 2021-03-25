package com.messages.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Messengers extends BaseEntity
{

    @Column(name= "user_send")
    private Integer id_user_send;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_send", insertable = false, updatable = false)
    private User user_send;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cvt_id")
    private Conversation cvt_id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSend;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeRead;

    @Column(nullable = false)
    private String content;

    @Column()
    private Integer isRead;

    @Override
    public String toString() {
        return "Messengers{" +
                "id_user_send=" + id_user_send +
                ", content='" + content + '\'' +
                '}';
    }
}
