package com.messages.controller;

import com.messages.entity.Conversation;
import com.messages.entity.Messengers;
import com.messages.entity.User;
import com.messages.repository.MessengersRepository;
import com.messages.repository.UserRepository;
import com.messages.service.MessengersService;
import com.messages.service.UserService;
import com.messages.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessengersService messengersService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessengersRepository messengersRepository;

    // hiển thị list user trừ user login
    @GetMapping("/chat")
    public String homeChat(@AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl, Model model) {
//        model.addAttribute("list",userService.getListExceptUserChat(userDetailServiceImpl.getId()));

        model.addAttribute("listByTime",userRepository.listSoftByTimeChat(userDetailServiceImpl.getId()));
        model.addAttribute("listFr",userRepository.listExceptUserChat(userDetailServiceImpl.getId()));

        return "chats/chat";
    }
    // hiển thị list user search
    @PostMapping("/chat")
    public String search(Model model, String key, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl) {
        model.addAttribute("listFr", userRepository.search(userDetailServiceImpl.getId(),key));
        return "chats/chat";
    }
    // show tin nhắn từ conversation
    @GetMapping("/conversation/{id}/mess/{idUser}")
    public String getMessByIdConversation(@AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl, @PathVariable(value = "id") Integer id,  @PathVariable(value = "idUser") Integer idUser, Model model){
        Date date = new Date();
        model.addAttribute("listByTime",userRepository.listSoftByTimeChat(userDetailServiceImpl.getId()));
        model.addAttribute("listFr",userRepository.listExceptUserChat(userDetailServiceImpl.getId()));
        model.addAttribute("mess", messengersService.getAllMessByIdCvt(id));
        messengersRepository.updateMessStatus(id, date, userDetailServiceImpl.getId());
        User user =  userService.getUserById(idUser);
        model.addAttribute("user", user);
        return "chats/chatConversation";
    }
    // gởi nhận, lưu tin nhắn
    @MessageMapping("/conversations/{id}")
    @SendTo("/topic/conversation/{id}")
    public Messengers message(@DestinationVariable(value = "id") Integer id, Messengers message, Principal principal)
    {
        User user = new User();
        user.setId(userRepository.findByUsername(principal.getName()).getId());

        Conversation conversation = new Conversation();
        conversation.setId(id);

        Date date = new Date();

        // format date để check trường startime trong database
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);

        // kiểm tra đã tồn tại tin nhắn giữa 2 user chưa
        Optional<Messengers> check = messengersRepository.checkStartTime(id, strDate); // check xem đã có startime chưa

        if(!check.isPresent()){ // nếu chưa có tin nhắn đầu tiên của 2 user thì nhập vào
            message.setStartTime(date);
            message.setContent(message.getContent());
            message.setTimeSend(date);
            message.setCvt_id(conversation);
            message.setUser_send(user);
            message.setIsRead(0);
            messengersRepository.updateMessStatus(id, date, message.getId_user_send());
            messengersRepository.save(message);
        }else{
            // format dữ date startime để check với date ngày hiện tại
            String startime = formatter.format(check.get().getStartTime());

            message.setContent(message.getContent());
            message.setTimeSend(date);
            message.setCvt_id(conversation);
            message.setUser_send(user);
            message.setIsRead(0);
            messengersRepository.updateMessStatus(id, date, message.getId_user_send());
            //nếu starttime khác ngày hiện tại thì lưu
            if(startime.compareTo(strDate) < 0){
                message.setStartTime(date);
            }
            messengersRepository.save(message);
        }
        return message;
    }

    @GetMapping("/chat/loadMessageIsRead")
    @ResponseBody
    public Messengers loadMessagerIsRead(@Param("user1") Integer user1, @Param ("user2") Integer user2){
        return messengersRepository.loadMessageIsRead(user1, user2);
    }

    @GetMapping("/chat/countNewMessage")
    @ResponseBody
    public Integer countNewMessage(@Param ("user1") Integer user1, @Param ("user2") Integer user2){
        return messengersRepository.countNewMessage(user1, user2);
    }



}
