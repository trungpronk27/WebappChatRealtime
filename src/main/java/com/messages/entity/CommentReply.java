package com.messages.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "commentReply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReply extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(nullable = false)
    private String content;
}
