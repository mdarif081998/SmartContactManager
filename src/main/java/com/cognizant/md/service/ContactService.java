package com.cognizant.md.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cognizant.md.entity.Contact;
import com.cognizant.md.entity.User;
import com.cognizant.md.repository.ContactRepository;

@Service

public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	public Contact getContactById(int id) {
		return contactRepository.findById(id).get();
	}

	public Contact addContact(Contact contact) {
		return contactRepository.save(contact);

	}

	public Contact updateContact(Contact contact) {
		Contact contact1 = getContactById(contact.getCid());
		contact1.setName(contact.getName());
		contact1.setDescription(contact.getDescription());
		contact1.setEmail(contact.getEmail());
		contact1.setAlternatePhoneNo(contact.getAlternatePhoneNo());
		contact1.setImage(contact.getImage());
		contact1.setNickName(contact.getNickName());
		contact1.setPhoneNo(contact.getPhoneNo());
		contact1.setWork(contact.getWork());
		return contactRepository.save(contact1);
	}

	public void deleteById(int id) {
		contactRepository.deleteById(id);
	}
	public  Page<Contact> showContactsByUser(int userId, Pageable pageable){
		
		Page<Contact> userContacts = contactRepository.findContactsByUser(userId, pageable);
		return userContacts;
	}
	
	public List<Contact> findByNameContainingAndUser(String keyword, User user){
		return contactRepository.findByNameContainingAndUser(keyword, user);
	}

	public ContactRepository getContactRepository() {
		return contactRepository;
	}

	public void setContactRepository(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

}
