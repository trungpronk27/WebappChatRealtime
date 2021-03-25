package com.messages.service;

import com.messages.entity.Conversation;

import java.util.List;

public interface ConversationService {
    Conversation checkCvt(Integer user1, Integer user2);
}
