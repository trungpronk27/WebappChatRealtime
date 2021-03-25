package com.messages.service;

import com.messages.entity.Messengers;

import java.util.Date;
import java.util.List;

public interface MessengersService {
    List<Messengers> getAllMessByIdCvt(Integer id); // lấy nội dung tin nhắn từ kênh chat

}
