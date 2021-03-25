package com.messages.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mess_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MessGroup extends BaseEntity
{
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "group_id")
    private Group group_id;

    @Column(name= "group_user_send")
    private Integer id_user_send;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "group_user_send", insertable = false, updatable = false)
    private User group_user_send;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timeSend;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timeRead;

    @Column
    private String content;

    @Transient
    private String img;


    @Override
    public String toString() {
        return "MessGroup{" +
                ", id_user_send=" + id_user_send +
                ", img=" + img +
                ", content='" + content + '\'' +
                '}';
    }
}
