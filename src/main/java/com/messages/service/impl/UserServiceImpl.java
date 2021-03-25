package com.messages.service.impl;

import com.messages.entity.Employee;
import com.messages.entity.User;
import com.messages.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements com.messages.service.UserService {

    @Autowired //inject bean
    private UserRepository userRepository;

    @Override // Đăng kí user
    public void saveReg(User user) {
         userRepository.save(user);
    }

    @Override // Cấu hình đăng nhập
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserDetailServiceImpl(user);
    }

    @Override // lấy user nằm trong list bạn bè ngoại trừ user login
    public List<User> getListExceptUserChat(Integer exceptUsername) {
        return userRepository.listExceptUserChat(exceptUsername);
    }


    @Override
    public void uploadUserImg(String img, Integer id) {
        userRepository.updateUserImg(img, id);
    }

    @Override
    public User getUserById(Integer id){
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        }else{
            throw new RuntimeException("User not found for id : " + id);
        }
        return user;
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException{
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public void updatePassword(String token, String newPassword) throws UserNotFoundException{
        User user = userRepository.findByResetPasswordToken(token);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Invalid token!");
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void changePassword(Integer id, String newPassword, String oldPassword) throws UserNotFoundException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//      String encodedPassword = passwordEncoder.encode(newPassword);
        User user = userRepository.findByUserId(id);
        if(user != null && passwordEncoder.matches(oldPassword, user.getPassword())==true) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }else {
            throw new UserNotFoundException("Old password incorrect!");
        }
    }



}
