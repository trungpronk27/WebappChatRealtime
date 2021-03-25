package com.messages.service;

import com.messages.entity.User;
import com.messages.service.impl.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService
{
    void saveReg(User user); // Tạo phương thức đăng kí.

    User getUserById(Integer id);

    List<User> getListExceptUserChat(Integer exceptUsername); // lấy user nằm trong list bạn bè ngoại trừ user login

    void uploadUserImg(String img, Integer id); // upload img user

    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;



    void updatePassword(String token, String newPassword) throws UserNotFoundException;

    User getByResetPasswordToken(String token);

    void changePassword(Integer id, String newPassword, String oldPassword) throws UserNotFoundException;


}
