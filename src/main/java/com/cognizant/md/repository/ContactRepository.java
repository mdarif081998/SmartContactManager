package com.cognizant.md.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognizant.md.entity.Contact;
import com.cognizant.md.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	// Method with Pagination
	// we need to add two values current page number and number of contacts per page in pageable object
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

	/*// Method without pagination
	 * @Query("from Contact as c where c.user.id =:userId") public List<Contact>
	 * findContactsByUser(@Param("userId") int userId);
	 */
	
	
	public List<Contact> findByNameContainingAndUser(String keywords, User user);
	
	
}
