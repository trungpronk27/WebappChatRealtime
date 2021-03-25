package com.messages.config;

import com.messages.entity.Role;
import com.messages.entity.User;
import com.messages.repository.RoleRepository;
import com.messages.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_MEMBER") == null) {
            roleRepository.save(new Role("ROLE_MEMBER"));
        }

        // Admin account
        if (userRepository.findByUsername("hoanghai") == null) {
            User admin = new User();
            admin.setUsername("hoanghai");
            admin.setFirstName("Hải");
            admin.setLastName("Hoàng");
            admin.setUserImg("hai.jpg");
            admin.setEmail("hoanghai@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
//            HashSet<Role> roles = new HashSet<>();
//            roles.add(roleRepository.findByName("ROLE_ADMIN"));
//            roles.add(roleRepository.findByName("ROLE_MEMBER"));
//            admin.setRoles(roles);
            userRepository.save(admin);
        }
//
//        // Member account
        if (userRepository.findByUsername("hai") == null) {
            User user = new User();
            user.setUsername("hai");
            user.setFirstName("Hoàng");
            user.setLastName("Văn");
            user.setUserImg("default.jpg");
            user.setEmail("hai@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
//            HashSet<Role> roles = new HashSet<>();
//            roles.add(roleRepository.findByName("ROLE_MEMBER"));
//            user.setRoles(roles);
            userRepository.save(user);
        }

    }
}
