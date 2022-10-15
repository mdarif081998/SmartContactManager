package com.cognizant.md.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cognizant.md.entity.User;
import com.cognizant.md.service.EmailService;
import com.cognizant.md.service.UserService;

@Controller
public class PasswordController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@GetMapping("/forgot")
	public String openEmailForm(Model model) {
		model.addAttribute("title", "Forgot Password");
		return "forgot-password-form";
	}

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, Model model, HttpSession session) {

		System.out.println("inside Send OTP");
		// generating otp of 4 digits
		User user = userService.getUserByUserName(email);
		System.out.println(user.getEmail()+"  "+email);
		if (!user.getEmail().isEmpty()) {
			
			Random random = new Random(100000);
			int otp = random.nextInt(999999); // else Math.random()*((999999-100000)+1)+100000;
			String body ="Hello User please use the below otp to reset your password"+otp;
			/*
			 * String body = "<h1><b> Dear " + user.getName() +
			 * ",</b></h1><br><p>Thank you For using our Smart Contact Manager Services. " +
			 * "Please Use the Following OTP to Reset Your PassWord.</p>" +
			 * "<br><h1><b>OTP: " + otp + ".</b></h1>" +
			 * "<br><br><p><b>Regards and Thanks,</b><br>Smart Contact Manager Team</p>";
			 */
			try {
		//		emailService.sendMailBuilder(email, "SCM: Reset Password", body);
				model.addAttribute("otp",otp);
				model.addAttribute("title", "Verify OTP");
				session.setAttribute("success","OTP Sent Successfully");
				System.out.println("OTP Sent Successfully" +otp);
				return "enter-otp";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("inside Else");
			model.addAttribute("title", "Forgot Password");
			session.setAttribute("danger","Invalid Username/User Doesn't Exist.");

		}
		return "forgot-password-form";

	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp() {
		return "";
	}
	
}