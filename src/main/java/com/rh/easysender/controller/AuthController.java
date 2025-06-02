package com.rh.easysender.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.rh.easysender.entity.*;
import com.rh.easysender.repo.UserRepository;
import com.rh.easysender.service.UserService;
import com.rh.easysender.service.Utility;
import net.bytebuddy.utility.RandomString;

@Controller
public class AuthController {
	
	private final  UserService userService;

	public AuthController( UserService userService) {
	      this.userService = userService;
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		return "easysender/login";
	}
	
	@PostMapping("/changepassword")
	public String Editpassword(Model model,@ModelAttribute("user") @Validated  User user ,  BindingResult result
	
		) throws IOException {
		
		User useredit =userService.getUser(user.getId());
		
		useredit.setId(useredit.getId());
		useredit.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		userService.SaveUser(useredit);
		   
		   return "redirect:/profiluser";
	}
	
	 @GetMapping("/forgot_password")
	    public String showForgotPasswordForm() {
	    	
	        return "elearning/forgetpassword";
	    }
	  @Autowired
	    private JavaMailSender mailSender;
	    
	    public void sendEmail(String recipientEmail, String link)
	            throws MessagingException, UnsupportedEncodingException {
	    	
	        MimeMessage message = mailSender.createMimeMessage();              
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	         
	        helper.setFrom("contact@easy.com", "Easy  Plateforme Support");
	        helper.setTo(recipientEmail);
	         
	        String subject = "Here's the link to reset your password";
	         
	        String content = "<p>Hello,</p>"
	                + "<p>You have requested to reset your password.</p>"
	                + "<p>Click the link below to change your password:</p>"
	                + "<p><a href=\"" + link + "\">Change my password</a></p>"
	                + "<br>"
	                + "<p>Ignore this email if you do remember your password, "
	                + "or you have not made the request.</p>";
	         
	        helper.setSubject(subject);
	         
	        helper.setText(content, true);
	         
	        mailSender.send(message);
	    }
	    @PostMapping("/forgot_password")
	    public String processForgotPassword(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
	        String email = request.getParameter("email");
	        String token = RandomString.make(30);
	         
	      
	        	userService.updateResetPasswordToken(token, email);
	            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
	            sendEmail(email, resetPasswordLink);
	            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
	             
	       
	            return "redirect:/forgot_password";   
	       
	    }
	    @GetMapping("/reset_password")
	    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	        User user  = userService.getByResetPasswordToken(token);
	        model.addAttribute("token", token);
	         
	        if (user == null) {
	            model.addAttribute("message", "Invalid Token");
	        
	            model.addAttribute("display", "true");
	            return "elearning/resetpassword";
	        }else {
	        	 model.addAttribute("display", "true");
	        	return "elearning/resetpassword";
	        }
	        
	       
	    }
	    

}

