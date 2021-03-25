package com.messages.service.impl;

import com.messages.entity.Messengers;
import com.messages.repository.MessengersRepository;
import com.messages.service.MessengersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessegersServiceImpl implements MessengersService {

    @Autowired //inject bean
    private MessengersRepository messengersRepository;

    @Override
    public List<Messengers> getAllMessByIdCvt(Integer id) {
        return messengersRepository.getMessByIdConversation(id);
    }

//    @Override
//    public void saveMess(Integer cvt_id, Integer user_send, Date time_send, String content) {
//        messengersRepository.saveMess(cvt_id, user_send, time_send, content);
//    }
}
