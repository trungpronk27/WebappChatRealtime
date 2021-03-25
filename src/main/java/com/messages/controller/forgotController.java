package com.messages.controller;

import com.messages.service.UserService;
import com.messages.service.impl.UserNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class forgotController {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping ("/forgot")
    public String forgot(){
        return "/auth/forgotPassword";
    }



    @PostMapping("/forgot")
    public String processForgotPasswordForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(25);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset?token=" + token;
//            System.out.println(resetPasswordLink);
            sendEmail(email, resetPasswordLink, token);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email.");
        }
        model.addAttribute("pageTilte", "Forgot Password");
        return "/auth/forgotPassword";
    }



    private void sendEmail(String email, String resetPasswordLink, String token) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("chuategame12345@gmail.com", "Massages Support");
        helper.setTo(email);

        String subject = "Here's the reset password link to login.";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Coppy the token and click the link below to change your password:</p>"
                + "<p><b>" + token + "</b></p>"
                + "<p><b><a href= \"" + resetPasswordLink + "\">Change my password</a></b></p>"
                + "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
