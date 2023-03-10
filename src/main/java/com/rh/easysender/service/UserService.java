package com.rh.easysender.service;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rh.easysender.entity.User;
import com.rh.easysender.repo.UserRepository;



@Service
@Transactional
public class UserService {
 
    @Autowired
    private UserRepository UserRepo;
   
    @Autowired
    private JavaMailSender mailSender;
    public void updateResetPasswordToken(String token, String email)  {
        User User = UserRepo.findByEmail(email);
        if (User != null) {
            User.setResetPasswordToken(token);
            UserRepo.save(User);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return UserRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User User, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        User.setPassword(encodedPassword);
         
        User.setResetPasswordToken(null);
        UserRepo.save(User);
    }
}
