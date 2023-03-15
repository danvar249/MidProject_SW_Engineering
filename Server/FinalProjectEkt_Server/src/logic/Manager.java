package logic;
/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each manager.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Manager extends Worker{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	* Manager logic part.
	* private fields that will contain manager department and the area he is managing
	*/
	private String department;
	private String managingArea;
	/**
	 * Manager constructor, inherits fields from Worker. And have his own fields "department", "managingArea"
	 * @param firstName
	 * @param lastName
	 * @param id
	 * @param phoneNumber
	 * @param emailAddress
	 * @param creditCard
	 * @param username
	 * @param password
	 * @param role
	 * @param department
	 * @param managingArea
	 */
	public Manager(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password, String role, String department, String managingArea) {
		super(firstName, lastName, id, phoneNumber, emailAddress, creditCard, username, password, role);
		this.department = department;
		this.managingArea = managingArea;
	}
	/**
	 * getting manager department
	 * @return department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * setting manager department
	 * @param department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * getting manager managing area
	 * @return managingArea
	 */
	public String getManagingArea() {
		return managingArea;
	}
	/**
	 * setting manager managing area
	 * @param managingArea
	 */
	public void setManagingArea(String managingArea) {
		this.managingArea = managingArea;
	}
	/**
	 * toString method, returns manager details
	 */	
	@Override
	public String toString() {
		return "Manager [department=" + department + ", managingArea=" + managingArea + ", getRole()=" + getRole()
				+ ", toString()=" + super.toString() + ", getFirstName()=" + getFirstName() + ", getLastName()="
				+ getLastName() + ", getId()=" + getId() + ", getPhoneNumber()=" + getPhoneNumber()
				+ ", getEmailAddress()=" + getEmailAddress() + ", getCreditCard()=" + getCreditCard()
				+ ", getUsername()=" + getUsername() + ", getPassword()=" + getPassword() + "]";
	}
}
