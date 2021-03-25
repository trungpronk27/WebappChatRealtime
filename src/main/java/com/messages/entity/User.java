package com.messages.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// @JsonIdentityReference(alwaysAsId = true)
public class User extends BaseEntity
{
    @Column
    private String userImg;

    @Column(length = 100, nullable = false, unique = true)
    @NotBlank(message = "Username cannot be null")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "First name cannot be null")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name cannot be null")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be null")
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Must be email")
    private String email;

    @Column(length = 1)
    private String gender;

    @Column()
    private java.sql.Date dateOfBirth;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timeActive;

    @Column
    private String rememberToken;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user_send", cascade = CascadeType.ALL)
    private List<Messengers> messengers = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<GroupUser> groupUsers = new ArrayList<>();

    @OneToMany(mappedBy = "friend_send", cascade = CascadeType.ALL)
    private List<Friend> friend_send = new ArrayList<>();

    @OneToMany(mappedBy = "friend_reply", cascade = CascadeType.ALL)
    private List<Friend> friend_reply = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "group_user_send", cascade = CascadeType.ALL)
    private List<MessGroup> messGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("costMaterials")
//    @JsonIgnoreProperties("user1")
    private List<Conversation> conversations = new ArrayList<>();

    @OneToMany(mappedBy = "user2",  cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("costMaterials")
//    @JsonIgnoreProperties("user2")
    private List<Conversation> conversations2 = new ArrayList<>();

}
