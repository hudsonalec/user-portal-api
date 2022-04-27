package com.revature.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.data.UserRepository;
import com.revature.exception.UserNotFoundException;
import com.revature.model.User;

@Service
public class UserService {
	
	@Autowired // Spring IoC container will inject the auto-generated IMPL class of this interface
	private UserRepository userRepo; // as a dependency of this Service Class
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * @Transactional on Service methods:
	 * 		In the case that multiple dao methods were
	 * 		being called, we want to make sure that those methods are
	 * 		fired against the DB in one unit of work (transaction)
	 * 
	 */
	@Transactional(readOnly=true)
	public Set<User> findAll() {
		
		// here we are using the stream API to transform the List to a Set to avoid duplicates
		return userRepo.findAll().stream()
				.collect(Collectors.toSet());	
	}
	
	// Every time that this method is invoked, we want to begin a new Transaction (unit of work against the DB)
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public User add(User u) {
		
		return userRepo.save(u);
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED) // default setting of transactions in Spring
	public void remove(int id) {	
		userRepo.deleteById(id);	
	}
	
	@Transactional(readOnly=true)
	public User getByUsername(String username) {
		
		// set up a custom exception handling in the case that this optional returns a null user
		return userRepo.findByUsername(username) // in the case that no user object can be returned, throw an exception
				.orElseThrow(() -> new UserNotFoundException("No user found with username " + username));	
	}
	
	@Transactional(readOnly=true)
	public User getById(int id) {
		
		if (id <= 0) {
			log.warn("Id cannot be <= 0: Id passed was: {}", id);
			return null;
		} else {
			return userRepo.getById(id);
			
		}
		
	}
	
	

}
