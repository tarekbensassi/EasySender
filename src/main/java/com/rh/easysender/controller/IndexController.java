package com.rh.easysender.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rh.easysender.entity.*;
import com.rh.easysender.repo.ApplicationRepository;
import com.rh.easysender.repo.ProfileRepository;
import com.rh.easysender.service.UserService;

@Controller
public class IndexController {
	
   private Logger logger = LoggerFactory.getLogger(IndexController.class);
	

    private final ApplicationRepository ApplicationRepository;
	private final ProfileRepository ProfileRepository;
	
	public IndexController( 
			  ApplicationRepository ApplicationRepository,
			  ProfileRepository ProfileRepository ) {
	      this.ApplicationRepository = ApplicationRepository;
	      this.ProfileRepository = ProfileRepository;
	}
	
	@GetMapping("/compose")
	public String viewComposePage(Model model,	@RequestParam("id") Long id) {
		Application Applications = ApplicationRepository.findById(id).get();
		model.addAttribute("Applications", Applications);
			
			return "easysender/compose";
	
		
	}
	@GetMapping("/profile")
	public String viewProfile(Model model,    @RequestParam("id") Optional<Long> id) {
		
		   if (id.isPresent()) {
			   Profile Profile = ProfileRepository.findById(1L).get();
			model.addAttribute("Profile", Profile);
			return "easysender/profile";
		}else {
			return "easysender/profile";
		}
			
		
	  
		
	}

	@GetMapping("/profile/display/{id}")
	@ResponseBody
	void showCV(@PathVariable("id") Long id, HttpServletResponse response, Optional<Profile> Profile)
			throws ServletException, IOException {
		
		Profile = ProfileRepository.findById(id);
		response.setContentType("application/pdf");
		response.getOutputStream().write(Profile.get().getCv());

		response.getOutputStream().close();
	}
	@GetMapping("/profile/display1/{id}")
	@ResponseBody
	void showLM(@PathVariable("id") Long id, HttpServletResponse response, Optional<Profile> Profile)
			throws ServletException, IOException {
		
		Profile = ProfileRepository.findById(id);
		response.setContentType("application/pdf");
		response.getOutputStream().write(Profile.get().getLm());

		response.getOutputStream().close();
	}
	@GetMapping("/index")
	public String viewIndexPage(Model model) {
			Iterable<Application> Applications = ApplicationRepository.findAll();
			model.addAttribute("Applications", Applications);
			return "easysender/index";
	}
	@PostMapping("/saveprofile")
	public String saveApps(Model model,@ModelAttribute("profile") @Validated  Profile profile ,  BindingResult result
			 ,@RequestParam("cv") MultipartFile cv,
			 @RequestParam("lm") MultipartFile lm 
			) throws IOException {
		profile.setId(1L);
		profile.setCv(cv.getBytes());
		profile.setLm(lm.getBytes());
		ProfileRepository.save(profile);
	    return "redirect:/profile";
	}

	@PostMapping("/saveapp")
	public String saveApps(Model model,@ModelAttribute("Application") @Validated  Application Application ,  BindingResult result
		) throws IOException {
		Application.setStatus("waiting");
		ApplicationRepository.save(Application);
	    return "redirect:/index";
	}

   
    
    @Autowired
    private JavaMailSender javaMailSender;
 
    // Method 1
    // To write the bytes into a file
    
    public static File convertUsingCommonsIO(byte[] v)
    {
    	
        try
        {
        	OutputStream out = new FileOutputStream("out.pdf");
        	out.write(v);
        	out.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		return null;    
      
		
    }


    @PostMapping("/sendmail")
    public String SendMail(Model model,	@RequestParam("id") Long id,
    	@ModelAttribute("details")  EmailDetails details,HttpServletResponse response) throws MessagingException, IOException {
    	System.out.println(details);
    	Profile Profile = ProfileRepository.findById(1L).get();
    	
    	
    	MimeMessage msg = javaMailSender.createMimeMessage();

 
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(details.getEmail());
        helper.setSubject(details.getSubject());
        String content = details.getMessage();
        helper.setText(content, true);
     
       
     // Convert the byte array to a Spring Resource object
        ByteArrayResource cv = new ByteArrayResource(Profile.getCv());
        ByteArrayResource lm = new ByteArrayResource(Profile.getLm());

        // Add the  file as an attachment to the email
        helper.addAttachment("cv.pdf", cv);
        helper.addAttachment("lm.pdf", lm);

        
      
        
        javaMailSender.send(msg);
		Application Applications = ApplicationRepository.findById(id).get();
		Applications.setStatus("sended");
		ApplicationRepository.save(Applications);
		
	return "redirect:/index";
	}
    

 
   


}

