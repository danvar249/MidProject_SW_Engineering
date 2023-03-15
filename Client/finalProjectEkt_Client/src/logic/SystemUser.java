package logic;

import java.io.Serializable;

/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each system user.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
// added serializble 
public class SystemUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *system user logic part.
	 *all the following details, will be saved for each system user
	 */
	private String firstName;
	private String lastName;
	private Integer id;
	private String phoneNumber;
	private String emailAddress;
	private String creditCard;
	private String username;
	private String password;
	private Role role;
	
	/**
	 * system user constructor
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param emailAddress
	 * @param creditCard
	 * @param username
	 * @param password
	 * @param roleString TODO
	 * @throws IllegalArgumentException TODO
	 */
	
	// Rotem modified constructor (and added another one) to fit the current form of the database
	// (this means adding the role to the sysuser, and allowing it to be a string or a role in the constructor
	public SystemUser(Integer id, String firstName, String lastName, String phoneNumber, String emailAddress,
			String creditCard, String username, String password, String roleString)
					throws IllegalArgumentException 
	{
		//super(); totem temoved it it was unnecc
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.creditCard = creditCard;
		this.username = username;
		this.password = password;
		// allow inserting a role as string (with input check)
		try {
			this.setRole(Role.valueOf(roleString.toUpperCase()));
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Cannot create a SystemUser with role=" + roleString);
		}
	}

	// added a constructor with Role input
	public SystemUser(Integer id, String firstName, String lastName, String phoneNumber, String emailAddress,
			String creditCard, String username, String password, Role role) {
		//super(); totem temoved it it was unnecc
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.creditCard = creditCard;
		this.username = username;
		this.password = password;
		// allow inserting a role as itself
		this.setRole(role);

	}
	


	/**
	 * getting user first name
	 * @return user first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * setting user first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * getting user last name
	 * @return user last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * setting user last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * getting user id
	 * @return user id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setting user id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * getting user phone number
	 * @return user phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * setting user phone number
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * getting user email address
	 * @return user email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * setting user email address
	 * @param emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * getting user credit card number 
	 * @return credit card number
	 */
	public String getCreditCard() {
		return creditCard;
	}
	/**
	 * setting user credit card number
	 * @param credit Card number
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	/**
	 * getting user username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * setting user username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * getting user password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * setting user password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/**
	 * toString method, returns system user details
	 */
	@Override
	public String toString() {
		return sqlFormatObject();
	}
	
	public String sqlFormatObject() {
		return "(" + id + ", "
				+ "\"" + firstName + "\", "
				+"\""+lastName+ "\", " +
				"\"" + phoneNumber + "\", " +
				"\""+emailAddress +"\", " +
				"\""+creditCard + "\", " +
				"\""+username + "\", " +
				"\""+password +"\", \"" + this.role + "\")";
	}

	// added getter and setter for ROle
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
