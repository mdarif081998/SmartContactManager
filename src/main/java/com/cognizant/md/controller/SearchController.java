package com.cognizant.md.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.md.entity.Contact;
import com.cognizant.md.entity.User;
import com.cognizant.md.service.ContactService;
import com.cognizant.md.service.UserService;

@RestController
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		System.out.println(query);
		User user = userService.getUserByUserName(principal.getName());
		List<Contact> contacts = contactService.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
