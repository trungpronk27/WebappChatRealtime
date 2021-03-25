package com.messages.service.impl;

import com.messages.entity.Group;
import com.messages.entity.GroupUser;
import com.messages.repository.GroupChatRepository;
import com.messages.repository.GroupUserRepository;
import com.messages.service.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    @Autowired //inject bean
    private GroupChatRepository groupChatRepository;


    @Override
    public List<Group> getListGroupChat(Integer userID) {
        return groupChatRepository.getUserGroup(userID);
    }

    @Override
    public List<Group> getMessGroupChat(Integer idGroup) {
        return groupChatRepository.getMessByIdGroupChat(idGroup);
    }
}
