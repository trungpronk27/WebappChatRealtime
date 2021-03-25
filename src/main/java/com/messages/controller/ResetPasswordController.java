package com.messages.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import com.messages.entity.User;
import com.messages.service.UserService;
import com.messages.service.impl.UserNotFoundException;
@Controller
@RequestMapping("/reset")
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        return "auth/resetPassword";
    }

    @PostMapping
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            userService.updatePassword(token, password);
            model.addAttribute("message", "You have successfully changed your password!");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "auth/resetPassword";
    }
}