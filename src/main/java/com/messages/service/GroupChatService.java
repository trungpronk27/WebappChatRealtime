package com.messages.service;

import com.messages.entity.Group;
import com.messages.entity.GroupUser;

import java.util.List;

public interface GroupChatService {

    List<Group> getListGroupChat(Integer userID);
    List<Group> getMessGroupChat(Integer idGroup);
}
