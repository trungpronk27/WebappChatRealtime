package com.messages.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Entity
@Table(name = "groupUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUser extends BaseEntity
{

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "group_id")
    private Group group_id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(nullable = false)
    private Integer isAdmin;
}
