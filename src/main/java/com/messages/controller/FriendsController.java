package com.messages.controller;

import com.messages.entity.Friend;
import com.messages.entity.User;
import com.messages.repository.FriendRepository;
import com.messages.service.FriendService;
import com.messages.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class FriendsController {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendService friendService;


    @PostMapping("/addFriend/{id}") // gửi lời mời kết bạn khi user chưa có trong table friend
    public String addFriend(@PathVariable(value = "id") Integer id, @ModelAttribute("friend") Friend friend){
        friend.setStatus(1);
        friendRepository.save(friend);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/addFriends/{id}") // gửi lời mời kết bạn khi user có trong table friend (status = 0)
    public String addFriends(@PathVariable(value = "id") Integer id, @ModelAttribute("update") Friend friend){
        friend.setStatus(1);
        friendRepository.save(friend);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/confirmFriend/{id}") // xác nhận lời mời
    public String confirmFriend(@PathVariable(value = "id") Integer id, @ModelAttribute("update") Friend friend){
        friend.setStatus(2);
        friendRepository.save(friend);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/cancelFriend/{id}") // Huỷ kết bạn
    public String cancelFriend(@PathVariable(value = "id") Integer id, @ModelAttribute("update") Friend friend, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl){
        Optional<Friend> status = friendRepository.findById(friend.getId());

        // nếu user Huỷ là user_send thì update status = 0, nếu không thì hoán đổi vị trí 2 user
        if(status.get().getFriend_send().getId().equals(userDetailServiceImpl.getId()))
        {
            friend.setStatus(0);
        }else{
            friend.setStatus(0);
            User user_send = friend.getFriend_send(); // biến tạm để hoán đổi
            friend.setFriend_send(friend.getFriend_reply());
            friend.setFriend_reply(user_send);
        }
        friendRepository.save(friend);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/blockFriend") // Chặn
    public String blockFriend(@ModelAttribute("update") Friend friend, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl){
        Optional<Friend> status = friendRepository.findById(friend.getId());

        // nếu user block là user_send thì update status = 3, nếu không thì hoán đổi vị trí 2 user
        if(status.get().getFriend_send().getId().equals(userDetailServiceImpl.getId()))
        {
            friend.setStatus(3);
        }else{
            friend.setStatus(3);
            User user_send = friend.getFriend_send(); // biến tạm để hoán đổi
            friend.setFriend_send(friend.getFriend_reply());
            friend.setFriend_reply(user_send);
        }
        friendRepository.save(friend);
        return "profile/block";
    }

    @GetMapping("/listBlock/{id}/edit") // danh sách Chặn
    public String listBlock(@PathVariable(value = "id") Integer id, Model model,  @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl){

        if(id.equals(userDetailServiceImpl.getId())){
            model.addAttribute("listBlock", friendService.ListUserBlock(id));
            return "profile/listBlock";
        }else{
            return "404";
        }
    }

    @PostMapping("/unBlock/{id}/{idUser}") // Bỏ chặn
    public String unBlock(@PathVariable(value = "id") Integer id, @PathVariable(value = "idUser") Integer idUser, @ModelAttribute("update") Friend friend){
        friend.setId(id);
        friend.setStatus(0);
        friendRepository.save(friend);

        return "redirect:/listBlock/{idUser}/edit/";
    }

}
