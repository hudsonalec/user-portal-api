package com.revature.Aspect;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.revature.errorhandling.ApiError;
import com.revature.errorhandling.ApiValidationError;
import com.revature.exception.UserNotFoundException;

/**
 * All methods within this class will intercept certain
 * exceptions that are sent to the client
 * as HTTP responses from any class annotated with @Controller or @RestController
 */

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * The following method will be what constructs a NEW response entity
	 * to send back to the client when something goes wrong.
	 */
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		
		// we use the apiError obj passed through to scrape the status and body of the apiError
		return ResponseEntity.status(apiError.getStatus()).body(apiError);
		
	}
	
	
	/**
	 * Intercept exceptions that are caused by validation issues...
	 * 
	 * This will intercept any object passed to the controller's add(User u) method
	 */	
	@Override // just informing myself and any future devs that I overrode this from a Parent Class
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// TODO: build my OWN buildResponseEntity() method to return a custom response
		String error = "Request failed validation";
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex, error);
		
		// Iterate over the fields of the object we're trying to persist and 
		// return the fields that are NOT valid
		for (FieldError e : ex.getFieldErrors()) {
			// below we instantiate an ApiValidationError based on the erroneous field the user passed through
			apiError.addSubError(new ApiValidationError(e.getObjectName(),
					e.getDefaultMessage(), e.getField(), e.getRejectedValue()));			
		}
				
		// we created this method at the top of this class
		return buildResponseEntity(apiError); // intercept what otherwise would have been returned
	}
	
	
	/**
	 * Intercept exceptions that are caused by INvalid JSON.. 
	 * Send back some 4xx error telling the client that it's their fault and the server
	 * doesn't know how to read that request
	 */
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String error = "Malformed JSON Request";
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex, error);
			
		return buildResponseEntity(apiError);
	}
	
	/*
	 * Intercept a User Not Found Exception from the findByUsername() method in the controller
	 * 
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
		
		String error = "No User found with that username";
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex, error);
			
		return buildResponseEntity(apiError);
	}
	
	
	
	

}
