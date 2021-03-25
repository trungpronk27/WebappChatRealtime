package com.messages.service.impl;

import com.messages.entity.Conversation;
import com.messages.entity.Friend;
import com.messages.repository.ConversationRepository;
import com.messages.repository.FriendRepository;
import com.messages.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired //inject bean
    private ConversationRepository conversationRepository;

    @Autowired //inject bean
    private FriendRepository friendRepository;

    @Override // check cuộc hội thoại có 2 id user chưa, nếu chưa thì lưu và hiển thị
    public Conversation checkCvt(Integer user1, Integer user2) {
        Conversation conversations = conversationRepository.checkConversation(user1, user2);
        Friend friend = friendRepository.checkFriend(user1, user2);
        if(conversations == null || friend == null){
            friendRepository.saveFriend(user1, user2);
            conversationRepository.saveConversation(user1, user2);
        }
        return  conversations;
    }
}
