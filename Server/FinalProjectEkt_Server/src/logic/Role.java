package logic;

/**
 * Role is an enum that designates the role of a user. It includes a list of
 * possible roles that a user can have such as CUSTOMER, REGIONAL_MANAGER,
 * LOGISTICS_MANAGER, SERVICE_REPRESENTATIVE, CEO, DIVISION_MANAGER,
 * SALES_MANAGER, SALES_WORKER, LOGISTICS_EMPLOYEE, SUBSCRIBER,
 * UNAPPROVED_CUSTOMER, DELIVERY_WORKER, INVENTORY_WORKER,
 * UNAPPROVED_SUBSCRIBER. Also it provides methods such as getRoleFromString()
 * and toString() to convert the role from string to Role enum and vice versa.
 * 
 * @author Rotem
 *
 */

public enum Role {
	CUSTOMER, REGIONAL_MANAGER, LOGISTICS_MANAGER, SERVICE_REPRESENTATIVE, CEO, DIVISION_MANAGER, SALES_MANAGER,
	SALES_WORKER,
	// Rotem: added the logistics employee AND SUBSCRIBER - he is the one to
	// actually re stock the machines.
	LOGISTICS_EMPLOYEE, SUBSCRIBER, UNAPPROVED_CUSTOMER, // added Rotem 1.12

	DELIVERY_WORKER, // Rotem also added 1.13
	INVENTORY_WORKER, UNAPPROVED_SUBSCRIBER, // added Rotem 1.13 ( sad smiley?)
	;

	// get role object from string
	/**
	 * Returns the {@link Role} corresponding to the given string.
	 * 
	 * @param roleAsString the role as a string
	 * @return the {@link Role} corresponding to the given string
	 */
	public static Role getRoleFromString(String roleAsString) {
		return Role.valueOf(roleAsString.toUpperCase());
	}

	// we want it in lowercase for the sql table
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

}
