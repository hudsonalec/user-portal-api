package com.revature.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.model.User;
import com.revature.service.UserService;
import com.revature.util.JwtTokenManager;

/**
 * Expose an endpoint that a user must intitially hit 
 * when logging in from the front end to verfiy whether they are
 * authenticated or not
 * 
 * IF their username and password indeed exists in the DB, we will send
 * back a server-generated JWT token that they can use to continue accessing
 * our server
 */

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController // REST controller because we're going to expose functionality at an endpoint
public class AuthController {

	// we are going to be calling the userSErvice to check whether a username/password combo exists
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenManager tokenManager;
	
	@PostMapping("/login") // http://host:5000/api/login
	public User login(@RequestBody User user, HttpServletResponse response) {
		
		// authenticate the user (call the service to do this)
		user = userService.authenticate(user);
		
		//IF the user is not null, then issue a token
		if (user != null) {
			
			// generate a token
			String token = tokenManager.issueToken(user); // xxxx.yyyyy.zzzzz
			
			// append to the response (in the header) the generated token (the stamp the bouncer gives you)
			response.addHeader("portal-token", token);
			response.addHeader("Access-Control-Expose-Headers", "portal-token");
			response.setStatus(200); // successful connection
			
			// return the user;
			return user;
			
		} else {
			// else, return a 401 code (the request couldn't be completed because the user is not valid)
			response.setStatus(401);
			return null;
		}	
	}
	
}
