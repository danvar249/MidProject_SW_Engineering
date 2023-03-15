package logic;

/**
 * Role is an enum that designates the role of a user
 * TODO:
 * add role field to the table we need to make (table: username, password, id, role, status) [tbd]
 * @author Rotem
 *
 */

public enum Role {
	CUSTOMER, REGIONAL_MANAGER, LOGISTICS_MANAGER, SERVICE_REPRESENTATIVE, CEO, DIVISION_MANAGER, SALES_MANAGER, SALES_WORKER,
	// Rotem: added the logistics employee AND SUBSCRIBER - he is the one to actually re stock the machines.
	// ?
	LOGISTICS_EMPLOYEE, SUBSCRIBER,
	UNAPPROVED_CUSTOMER, // added Rotem 1.12

	DELIVERY_WORKER, // Rotem also added 1.13
	INVENTORY_WORKER, UNAPPROVED_SUBSCRIBER, // added Rotem 1.13 ( sad smiley?)
	; 
	
	// get role object from string
	public static Role getRoleFromString(String roleAsString) {
		return Role.valueOf(roleAsString.toUpperCase());
	}
	
	// we want it in lowercase for the sql table
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
}
