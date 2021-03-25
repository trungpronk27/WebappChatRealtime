package com.messages.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Conversation extends BaseEntity
{
    @ManyToOne()
    @JsonIgnore

    @JoinColumn(name = "user1")
    private User user1;

    @ManyToOne()
    @JsonIgnore

    @JoinColumn(name = "user2")
    private User user2;

    @OneToMany(mappedBy = "cvt_id", cascade = CascadeType.ALL)
    private List<Messengers> messengers = new ArrayList<>();

}
