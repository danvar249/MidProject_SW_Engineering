package common;


/**
 * The ServerClientRequestTypes enumeration is used to determine the type of
 * message being sent in the SCCP protocol. This enumeration is used by the
 * server and client to determine the necessary action to be taken for a given
 * message. It includes special messages like CONNECT_TO_SERVER, LOGIN, LOGOUT,
 * ERROR_MESSAGE, ACK, CRASH and database-related messages like CREATE, UPDATE,
 * REMOVE, ADD, DISPLAY, FETCH_PRODUCTS_BY_CATEGORY, FETCH_ORDERS,
 * UPDATE_ONLINE_ORDERS,ADD_PROMOTION, DISPLAY_PROMOTIONS,
 * DISPLAY_SELECTED_PROMOTIONS, INSERT_ORDER_TO_DATABASE,
 * DISPLAY_PROMOTIONS_TO_ACTIVE, UPDATE_PROMOTION_STATUS, SELECT, EK_LOGIN,
 * LOGIN_FAILED_ALREADY_LOGGED_IN, LOGIN_FAILED_ILLEGAL_INPUT,
 * REQUEST_ALL_MACHINES, FETCH_PRODUCTS_IN_MACHINE_ROTEM,
 * FETCH_MACHINES_BY_LOCATION, FETCH_PRODUCTS_IN_MACHINE,
 * UPDATE_PRODUCTS_IN_MACHINE.
 * 
 * @author Rotem
 *
 */

public enum ServerClientRequestTypes {
	// special messages:
	CONNECT_TO_SERVER, LOGIN, LOGOUT, ERROR_MESSAGE, ACK, CRASH,
	// database-related messages
	CREATE, UPDATE, REMOVE, ADD, DISPLAY, FETCH_PRODUCTS_BY_CATEGORY, FETCH_ORDERS, UPDATE_ONLINE_ORDERS,ADD_PROMOTION, 
	DISPLAY_PROMOTIONS, DISPLAY_SELECTED_PROMOTIONS, INSERT_ORDER_TO_DATABASE, DISPLAY_PROMOTIONS_TO_ACTIVE, UPDATE_PROMOTION_STATUS,
	
	SELECT, EK_LOGIN, LOGIN_FAILED_ALREADY_LOGGED_IN, LOGIN_FAILED_ILLEGAL_INPUT, REQUEST_ALL_MACHINES, // Rotem hadded all of these
	FETCH_PRODUCTS_IN_MACHINE_ROTEM, // Rotem added this too
	
	FETCH_MACHINES_BY_LOCATION, FETCH_PRODUCTS_IN_MACHINE, UPDATE_PRODUCTS_IN_MACHINE,
	//these belong to daniel but I removed them (ONLY INSERT HERE WHAT IS IMPLEMENTED IN ServerMessageHandler!!!!!)
	GET_PROMOTIONS_BY_LOCATION, GET_ORDERS_FOR_CANCELLATION, 
	;
	/**
	 * Legend:
	 * CONNECT_TO_SERVER - sends an empty object, used by server to maintain a list of connected clients (better than my original way)
	 * LOGIN - message to be sent when authentication is performed, pass tuple[user,pass] along it
	 * LOGOUT - we maintain an object for the current logged user - disconnect the user, pass[user,pass] along it
	 * ERROR_MESSAGE - whenever communication leads to an error, send this type and a code for the error - either String or enum (tbd)
	 * ACK - whenever a message does not require a response, return this to indicate the operation was successful (enum, to represent the exact operation)
	 * CRASH - if client crashes - perform log-out, set database to default state (tbd), and show special window.
	 * 		 - if server crashes - disconnect all clients (send this message to all clients) and show proper crash message on both sides (server and client)
	 * 
	 * CREATE - create a database schema/table, might not be required (tbd)
	 * UPDATE - update entry/entries in a table - object passed: tuple[table_name, what_column_to_change, which_row_to_change, what_to_change_to]
	 * REMOVE - remove an entry/entries from a table - object passed: tuple[table_name, columns_to_remove_by, value_to_remove] 
	 * ADD - 	add an entry/entries to a table - object passed: tuple[table_name, object_to_add(array of strings)]
	 * DISPLAY -display entry/entries from a table - object passed: tuple[table_name, columns_to_search_by, value_to_search] 
	 */
	
	
	// old (from prototype)
	//UPDATE_CUSTOMER, ADD_CUSTOMER, DISPLAY_CUSTOMER;
	/**
	 * This method overrides the {@link java.lang.Enum#toString()} method and
	 * returns the name of the enumeration constant.
	 * 
	 * @return the name of the enumeration constant.
	 */
	@Override
	public String toString(){
	    return name();
	}

}
