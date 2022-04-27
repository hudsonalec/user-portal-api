package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="users")
@Data // creates getters, setter, toString, equals(), and hashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Length(min=2)
	private String firstName;
	private String lastName;
	
	@NotBlank
	@Length(min=5)
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]") // regex - regular expressions to make sure a username is alphanumeric only
	private String username;
	
	@NotBlank
	private String password;
	
	@Email // checks that the value of this field has an @ in it
	private String email;

	public User(@Length(min = 2) String firstName, String lastName,
			@NotBlank @Length(min = 5) @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]") String username,
			@NotBlank String password, @Email String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	/** Regarding the validation constraints on some of the fields
	 * @NotNull: a constrained CharSequence, Collection, Map, or Array is valid as
	 * 			long as it's not null, but it can be empty
	 * @NotEmpty: a constrained CharSequence, Collection, Map, or Array is valid as 
	 * 			long as it's not null, and its size/length is greater than zero
	 * @NotBlank: a constrained String is valid as long as it's not null, and the trimmed length is greater than zero
	 */
	
	

}
