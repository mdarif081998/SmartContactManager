package com.cognizant.md.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.md.entity.User;
import com.cognizant.md.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	public User getUserById(int id) {
		return userRepository.findById(id).get();
	}
	
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
	public User updateUser(User user) {
		User user1= getUserById(user.getId());
		user1.setName(user.getName());
		user1.setAbout(user.getAbout());
		user1.setAccountActive(user.isAccountActive());
		user1.setEmail(user.getEmail());
		user1.setImage(user.getImage());
		user1.setPassword(user.getPassword());
		user1.setPhoneNo(user.getPhoneNo());
		user1.setRole(user.getRole());
		user1.setContacts(user.getContacts());
		return userRepository.save(user1);
	}
	
	public void deleteUser(int id) {
		 userRepository.deleteById(id);
	}
	
	public User getUserByUserName(String email) {
		return userRepository.getUserByUserName(email);
	}
	

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
}
