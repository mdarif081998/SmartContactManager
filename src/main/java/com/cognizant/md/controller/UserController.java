package com.cognizant.md.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.md.entity.Contact;
import com.cognizant.md.entity.User;
import com.cognizant.md.service.ContactService;
import com.cognizant.md.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UserService userService;

	@Autowired
	ContactService contactService;

	// Method to adding common data to Model object
	@ModelAttribute
	public void addCommonDate(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userService.getUserByUserName(userName);
		model.addAttribute("user", user);
		System.out.println("Inside Common Method");
		System.out.println(user);
	}

	@GetMapping("/dashboard")
	public String userDashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/userdashboard";
	}

	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add-contact-form";
	}

	@PostMapping("/process-contact")
	public String processingContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("contactimage") MultipartFile file, Model model, Principal principal, HttpSession session) {
		try {
			if (result.hasErrors()) {
				System.out.println("Error: " + result);
				model.addAttribute("contact", contact);
				return "normal/add-contact-form";
			}
			if (file.isEmpty()) {
				contact.setImage("defaultcontact.png");
			} else {
				contact.setImage(contact.getPhoneNo() + file.getOriginalFilename());
				File file2 = new ClassPathResource("static/img/contactimg").getFile();
				Path path = Paths.get(
						file2.getAbsolutePath() + File.separator + contact.getPhoneNo() + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			String name = principal.getName();
			User user = userService.getUserByUserName(name);
			user.getContacts().add(contact);
			contact.setUser(user);
			Contact addContact = contactService.addContact(contact);
			// System.out.println("AddedData: " + addContact);
			session.setAttribute("success", "Contact Added Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("contact", contact);
			session.setAttribute("danger", "Something went Wrong, Please Try Again...");
		}
		return "normal/add-contact-form";
	}

	// assuming current page no as 0 [page] and number of rows per page as 5
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model) {
		model.addAttribute("title", "All Contacts");
		System.out.println("Inside Show Contacts method");
		User user = (User) model.getAttribute("user");
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = contactService.showContactsByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		System.out.println(contacts);
		return "normal/show-contacts";
	}

	@GetMapping("/contact/{cid}")
	public String showContactDeatails(@PathVariable("cid") Integer cid, Model model) {
		model.addAttribute("title", "Contact-Details");
		Contact contact = contactService.getContactById(cid);
		User user = (User) model.getAttribute("user");
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		}
		return "/normal/contact-details";
	}

	@GetMapping("/deletecontact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) throws IOException {
		User user = (User) model.getAttribute("user");
		Contact contact = contactService.getContactById(cid);
		if (user.getId() == contact.getUser().getId()) {
			// delete old file
			if (!contact.getImage().equals("defaultcontact.png")) {
				File oldfile = new ClassPathResource("static/img/contactimg").getFile();
				File file1 = new File(oldfile, contact.getImage());
				file1.delete();
			}
			user.getContacts().remove(contact);

			userService.addUser(user);
			session.setAttribute("success", "Contact Deleted Successfully");
		} else {
			session.setAttribute("danger", "Something went wrong. Please Try Again...");
		}
		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/updatecontact/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {
		model.addAttribute("title", "Update-Contact-Form");
		Contact contact = contactService.getContactById(cid);
		User user = (User) model.getAttribute("user");
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		}
		return "normal/update-contact-form";
	}

	@PostMapping("/update-contact")
	public String processUpdateContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("contactimage") MultipartFile file, Model model, HttpSession session) {
		try {
			if (result.hasErrors()) {
				System.out.println("Error: " + result);
				model.addAttribute("contact", contact);
				return "normal/update-contact-form";
			}
			if (!file.isEmpty()) {
				// delete old file
				if (!contact.getImage().equals("defaultcontact.png")) {
					File oldfile = new ClassPathResource("static/img/userimage").getFile();
					File file1 = new File(oldfile, contact.getImage());
					file1.delete();
				}
				// upload new file
				File file2 = new ClassPathResource("static/img/contactimg").getFile();
				Path path = Paths.get(
						file2.getAbsolutePath() + File.separator + contact.getPhoneNo() + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(contact.getPhoneNo() + file.getOriginalFilename());
			}

			User user = (User) model.getAttribute("user");
			contact.setUser(user);
			Contact updatedContact = contactService.updateContact(contact);
			// System.out.println("AddedData: " + addContact);
			session.setAttribute("success", "Contact Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("contact", contact);
			session.setAttribute("danger", "Something went Wrong, Please Try Again...");
			return "normal/update-contact-form";
		}
		return "redirect:/user/contact/" + contact.getCid();
	}

	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}

	@GetMapping("/updateuser/{id}")
	public String updateUser(@PathVariable("id") Integer id, Model model, HttpSession session) {
		User user1 = (User) model.getAttribute("user");
		model.addAttribute("user1", user1);
		model.addAttribute("title", "Update-User-Form");
		return "normal/update-user-form";
	}

	@PostMapping("/update-user")
	public String processUpdateUser(@Valid @ModelAttribute("user1") User user1, BindingResult result,
			@RequestParam("userimage") MultipartFile file, Model model, HttpSession session) {
		try {
			User user = (User) model.getAttribute("user");
			if (result.hasErrors()) {
				System.out.println("Error: " + result);
				model.addAttribute("user1", user1);
				return "normal/update-contact-form";
			}
			if (user.getId() == user1.getId()) {
				if (!file.isEmpty()) {
					// delete old file
					if (!user1.getImage().equals("defaultcontact.png")) {
						File oldfile = new ClassPathResource("static/img/userimage").getFile();
						File file1 = new File(oldfile, user1.getImage());
						file1.delete();
					}

					// upload new file
					File file2 = new ClassPathResource("static/img/userimage").getFile();
					Path path = Paths
							.get(file2.getAbsolutePath() + File.separator + user1.getId() + file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					user1.setImage(user1.getId() + file.getOriginalFilename());
				}
				user1.setContacts(user.getContacts());
				userService.updateUser(user1);

				session.setAttribute("success", "User Updated Successfully.");
			} else {
				session.setAttribute("danger", "Something went Wrong, Please Try Again...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("danger", "Something went Wrong, Please Try Again...");
			return "normal/update-contact-form";
		}
		return "redirect:/user/profile/";
	}

	@GetMapping("/settings")
	public String openSettings(Model model) {
		model.addAttribute("title", "Settings");
		return "normal/settings";
	}

	@PostMapping("/change-password")
	public String ChangePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Model model, HttpSession session) {
		model.addAttribute("title", "Settings");
		User user = (User) model.getAttribute("user");
		if(passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userService.updateUser(user);
			session.setAttribute("success", "Password Changed Successfully...");
		}else {
			session.setAttribute("danger", "Invalid old Password. Please Try Again...");
		}
		return "/normal/settings";
	}

}
