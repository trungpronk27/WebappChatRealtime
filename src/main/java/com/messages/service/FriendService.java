package com.messages.service;

import com.messages.entity.Friend;

import java.util.List;

public interface FriendService {
//    Friend checkFriends(Integer friend_send, Integer friend_reply);

    Integer countFriend(Integer id);

//    Friend checkUserBlock(Integer friend_send, Integer friend_reply);
    Friend checkFriendStatus(Integer friend_send, Integer friend_reply);

    List<Friend> ListUserBlock(Integer id_friend_send);
}
