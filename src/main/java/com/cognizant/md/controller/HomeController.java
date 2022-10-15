package com.cognizant.md.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cognizant.md.entity.User;
import com.cognizant.md.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UserService userService;

	@RequestMapping("/")
	public String homePage(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}

	@RequestMapping("/about")
	public String aboutPage(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}

	@RequestMapping("/signup")
	public String registerPage(Model model) {
		model.addAttribute("title", "Signup");
		model.addAttribute("msg", "");
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("title", "Login");
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {

			if (result.hasErrors()) {
				System.out.println("Error: " + result);
				model.addAttribute("user", user);
				return "signup";
			}
			if (!agreement) {
				throw new Exception("You Have not Accepted the Terms and conditions. Please try again...");
			}
			user.setImage("defaultcontact.png");
			user.setRole("ROLE_USER");
			user.setAccountActive(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userService.addUser(user);
			model.addAttribute("msg", "User Registered Successfully.");
			model.addAttribute("title", "Login");
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", "You Have not Accepted the Terms and conditions. Please try again...");
			model.addAttribute("msg", "You Have not Accepted the Terms and conditions. Please try again...");
			return "signup";
		}

	}

}
