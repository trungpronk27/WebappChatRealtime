package com.messages.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "friend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend extends BaseEntity
{
    @Column(nullable = false, length = 1)
    private Integer status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "friend_reply")
    private User friend_reply;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "friend_send")
    private User friend_send;
}
