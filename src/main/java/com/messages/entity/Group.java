package com.messages.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Group extends BaseEntity
{
    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String groupImg;

    @OneToMany(mappedBy = "group_id", cascade = CascadeType.ALL)
    private List<GroupUser> groupUsers;

    @OneToMany(mappedBy = "group_id", cascade = CascadeType.ALL)
    private List<MessGroup> messGroups = new ArrayList<>();
}
