package com.messages.controller;

import com.messages.entity.User;
import com.messages.repository.UserRepository;
import com.messages.service.UserService;
import com.messages.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Nếu chưa login thì chuyển về trang login
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "auth/sign-in";
        }
        return "redirect:/chat";
    }

    @GetMapping("/index")
    public String getHome() {
        return "index";
    }

    @GetMapping("/updateTimeActive")
    @ResponseBody
    public void updateTimeActive(@Param("id_user") Integer id_user) throws ParseException {
        Date date = new Date();
        userRepository.updateTimeActive(date, id_user);
    }

    @GetMapping("/checkOnline")
    @ResponseBody
    public Optional<User> checkOnline(@Param("id_user") Integer id_user){
        return userRepository.findById(id_user);
    }



}