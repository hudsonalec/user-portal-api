package com.revature.util;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.revature.exception.AuthenticationException;
import com.revature.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenManager {
	
	private final Key key; // from java.security
	private final Logger logger = LoggerFactory.getLogger(JwtTokenManager.class);

	public JwtTokenManager(){
		// what is a key?
		//a set of public keys used to verify a token and have it be parsed by our server
		key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	// this builds the payload which is encrypted info about the user that we're authenticating
	public String issueToken(User user){
		return Jwts.builder() // io.jsonwebtoken
				// payload
				.setId(String.valueOf(user.getId()))
				.setSubject(user.getUsername())
				.setIssuer("User Portal API") // the source that generated the token
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(key).compact();
	}
	
	public int parseUserIdFromToken(String token){
		
		try {
			return Integer.parseInt(Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					// this is the way in which we can READ user data from a token
					.parseClaimsJws(token).getBody().getId());
			
		} catch (Exception e){
			logger.warn("JWT error parsing user id from token");
			throw new AuthenticationException("Unable to parse user id from JWT. Please sign in again");
		}
	}

}
