package com.messages.controller;

import com.messages.entity.User;
import com.messages.repository.UserRepository;
import com.messages.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sign-up")
public class UserRegistrationController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/sign-up";
    }

    @PostMapping
    public String registerUserAccount(Model model, @Valid @ModelAttribute("user") User user, BindingResult errors)
    {
        if (errors.hasErrors()){
            return "auth/sign-up";
        }

        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setUserImg("default.jpg");

            userService.saveReg(user);
        }catch (Exception e){
            model.addAttribute("isExist", "username or email is exist");
            return "auth/sign-up";
        }

        return "redirect:/sign-up?success";
    }
}
