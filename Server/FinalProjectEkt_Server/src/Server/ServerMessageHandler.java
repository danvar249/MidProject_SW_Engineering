package Server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import common.IServerSideFunction;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.TypeChecker;
import database.DatabaseController;
import database.DatabaseOperation;
import database.DatabaseSimpleOperation;
import logic.Promotions;
import logic.Role;
import logic.SystemUser;

/**
 * This class contains various inner classes that implement the
 * IServerSideFunction interface. These inner classes are responsible for
 * handling specific types of messages sent from the client to the server. Each
 * inner class is responsible for handling a specific type of request and
 * interacting with the DatabaseController in order to retrieve or update
 * information in the database. The inner classes include:
 * HandleMessageFetchProducts: Handles messages requesting all products of a
 * certain category. HandleMessageFetchOrders: Handles messages requesting all
 * order types from the order_type table. HandleMessageDisplayPromotions:
 * Handles messages requesting all promotion names from the promotions table.
 * HandleMessageDisplaySelectedPromotions: Handles messages requesting all
 * promotions of a certain name from the promotions table.
 * HandleMessageUpdateOnlineOrders: Handles messages updating an online order in
 * the online_orders table. HandleMessageAddPromotion: Handles messages adding a
 * new promotion to the promotions table. HandleMessageFetchProductsInMachine:
 * Handles messages requesting all products in a certain vending machine from
 * the products_in_machine table. HandleMessageUpdateProductsInMachine: Handles
 * messages updating products in a certain vending machine in the
 * products_in_machine table. HandleMessageRemove: Handles messages removing a
 * certain item from the specified table in the database.
 * 
 * @author Rotem
 */

public class ServerMessageHandler {
	
	
	// Rotem refactoring for JOIN related stuff:
	
	
	private static final class HandleMessageGetOrdersForCancellation implements IServerSideFunction{

		@Override
		public SCCP handleMessage(SCCP message) {
			// assume message sent contains the user region
			String region = (String)message.getMessageSent();
			Object res = DatabaseController.handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[] {"SELECT orders.orderID, orders.total_price, orders.date_received FROM "
					+ "orders "
					+ "JOIN machine ON orders.machineID = machine.machineId "
					+ "JOIN locations ON machine.locationId = locations.locationID "
					+ " WHERE locations.locationName = '" + region + "' AND orders.statusId=4;"});
			if(res == null) {
				return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "Invalid database state");
			}
			return new SCCP(ServerClientRequestTypes.ACK, res);
		}
		
		
	}
	
	private static final class HandleMessageGetPromotionsByLocation implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP message) {

			Object res = DatabaseController.handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[]{"SELECT promotionId, promotionName, "
					+ "promotionDescription, locationName, "
					+ "discountPercentage, startDate, promotionStatus FROM promotions LEFT JOIN locations on locations.locationID=promotions.locationID;"});
			if (res==null) {
				return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "Invalid database state");
			}
			return new SCCP(ServerClientRequestTypes.ACK, res);
		}
	}
	
	
	// Class that handles a message which adds row/rows to database
	private static final class HandleMessageAddToTable implements IServerSideFunction {
		// this is defined as a constant since, for adding to table, we always want a 3
		// element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 3;

		/**
		 * This class handles the ADD message type from the client. It is responsible
		 * for validating the input message, extracting the necessary data from it, and
		 * passing it to the {@link DatabaseController} for processing. The input
		 * message should be in the following format:
		 * Type(ServerClientRequestTypes.ADD), Object[]{String_tableName,
		 * Boolean_addMany, Object[]_whatToAdd} The class will extract the table name,
		 * whether to add multiple objects or just one, and the actual objects to add.
		 * The class also contains a debug section that prints the input data. The class
		 * will return a response object (SCCP) containing the request type and message,
		 * indicating the success or failure of the operation.
		 */
		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName,
			// Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a
			// message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			String tableName;
			Boolean addMany;
			Object[] objectsToAdd;

			/// Start input validation
			// verify type
			if (!(type.equals(ServerClientRequestTypes.ADD))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}
			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// table name:
				if (!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean addMany
				if (!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for addMany (Boolean input) in handleMessage.");
				}
				if (!(formattedMessage[2] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for whatToAdd (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				tableName = (String) formattedMessage[0];
				addMany = (Boolean) formattedMessage[1];
				objectsToAdd = (Object[]) formattedMessage[2];

			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation

			// debug
			System.out.println("Called server with ADD.\nTable name: " + tableName + "\nAdd many (boolean): " + addMany
					+ "\nObjects (to add): ");
			for (Object o : objectsToAdd) {
				System.out.println(o);
				System.out.println("Object we add is of class=" + o.getClass());
			}
			// end debug

			System.out.println("Calling the DB controller now (UNDER TEST)");
			// if addMany = false, the controller will use a different query that will
			// expect an array of size 1 (1 object)
			// now, we pass this three to the database controller.
			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.INSERT,
					new Object[] { tableName, addMany, objectsToAdd });

			// here, we return the proper message to the client
			// we will need some imports to do so (NOT IMPLEMENTED)
			System.out.println("Returning result to client (UNDER TEST)");
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToAdd); // send the array of objects we sent to add to the db, to
														// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
			}
			return response;
		}
	}

	// Rotem - 1/5/23 - select
	// select what from table where what and;
	/**
	 * This class is responsible for handling SELECT messages sent to the server.
	 * The format of the message sent to this class must be an Object array of size
	 * 7, where each element represents specific information for the SELECT
	 * statement. The elements of the object array are:
	 * 
	 * tableName: String - the name of the table to select from. filterColumns:
	 * Boolean - Indicates if the SELECT statement should filter the columns to be
	 * returned. filterColumnsArgs: String - A string containing the names of the
	 * columns to be returned, separated by commas. This element should be provided
	 * only if filterColumns is true. filterRows: Boolean - Indicates if the SELECT
	 * statement should filter the rows to be returned. conditions: String - A
	 * string containing the conditions for the rows to be returned, separated by
	 * the "AND" keyword. This element should be provided only if filterRows is
	 * true. useSpecialArgs: Boolean - Indicates if special arguments such as SORT
	 * BY should be used in the SELECT statement. specialArgs: String - A string
	 * containing the special arguments for the SELECT statement. This element
	 * should be provided only if useSpecialArgs is true.
	 * 
	 * If the provided message does not match this format, an
	 * IllegalArgumentException will be thrown. The class also performs input
	 * validation, and will throw IllegalArgumentException if any of the provided
	 * elements are of the wrong type or do not match the expected values. The class
	 * uses the DatabaseController class to handle the SELECT statement and the
	 * result is returned to the client in the form of an SCCP object containing the
	 * result of the SELECT statement.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageSelectFromTable implements IServerSideFunction {
		/*
		 * The format sent to this class: Object[] { tableName: String filterColumns (if
		 * false: select * from mama, as in ALL COLUMNS): Boolean filterColumnsArgs:
		 * String filterRows (select): Boolean conditions: String (this is the part
		 * where you pass what comes after the "WHERE") conditions = null if filterRows
		 * is false useSpecialArgs: Boolean (self explanatory -read next line)
		 * specialArgs: String (for example: SORT BY amount, you pass it with the SORT
		 * BY inside the string because I can't be bothered to that much of an extent)
		 * specialArgs = null if useSpecialArgs = false }
		 */
		// this is defined as a constant since, for selecting from table, we always want
		// a 5 element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 7;

		/**
		 * Handles a message of type SELECT by validating the message format and
		 * contents, and then executing the SELECT statement on the specified table with
		 * the given parameters.
		 * 
		 * @param message The message to handle, should be of type SELECT and contain an
		 *                Object array with the following format:
		 * 
		 *                Object[] { tableName: String filterColumns: Boolean (if true,
		 *                filter the columns to select, if false select all columns)
		 *                filterColumnsArgs: String (the columns to select if
		 *                filterColumns is true) filterRows: Boolean (if true, filter
		 *                the rows to select based on conditions, if false select all
		 *                rows) conditions: String (the conditions to filter rows by if
		 *                filterRows is true) useSpecialArgs: Boolean (if true, use
		 *                special arguments in the select statement, if false don't use
		 *                special arguments) specialArgs: String (the special arguments
		 *                to use in the select statement if useSpecialArgs is true) }
		 * @throws IllegalArgumentException if the message is not of type SELECT, or the
		 *                                  format or contents of the message is
		 *                                  invalid.
		 * @return SCCP object containing a type indicating success or error, and a
		 *         message containing the selected data.
		 */
		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName,
			// Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a
			// message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			String tableName;
			Boolean filterColumns;
			String filterColumnsArgs;

			Boolean filterRows;
			Boolean useSpecialArgs;
			String conditions;
			String specialArgs;

			/// Start input validation
			// verify type
			if (!(type.equals(ServerClientRequestTypes.SELECT))) {
				throw new IllegalArgumentException("Invalid type assigned in ServerMessageHandler map, type: "
						+ message.getRequestType() + " was passed to SELECT");
			}
			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// table name:
				if (!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean filterColumns
				if (!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for selectAll (Boolean input) in handleMessage.");
				}
				filterColumns = (Boolean) formattedMessage[1];
				if (filterColumns) {
					// String filterColumnsArgs:
					if (!(formattedMessage[2] instanceof String)) {
						throw new IllegalArgumentException(
								"Invalid value for conditions (String input) in handleMessage.");
					}
				}
				// boolean filterRows
				if (!(formattedMessage[3] instanceof Boolean)) {
					throw new IllegalArgumentException(
							"Invalid value for filterRows (Boolean input) in handleMessage.");
				}
				// check if we want specific rows
				filterRows = (Boolean) formattedMessage[3];
				if (filterRows) {
					// String conditions:
					if (!(formattedMessage[4] instanceof String)) {
						throw new IllegalArgumentException(
								"Invalid value for conditions (String input) in handleMessage.");
					}
				}
				// boolean useSpecialArgs
				if (!(formattedMessage[5] instanceof Boolean)) {
					throw new IllegalArgumentException(
							"Invalid value for useSpecialArgs (Boolean input) in handleMessage.");
				}
				// check if want to use special arguments
				useSpecialArgs = (Boolean) formattedMessage[5];
				if (useSpecialArgs) {
					// string specialArgs
					if (!(formattedMessage[6] instanceof String)) {
						throw new IllegalArgumentException(
								"Invalid value for specialArgs (String input) in handleMessage.");
					}
				}
				// assign proper values to from the rest of the message
				tableName = (String) formattedMessage[0];
				filterColumnsArgs = (String) formattedMessage[2];
				conditions = (String) formattedMessage[4];
				specialArgs = (String) formattedMessage[6];

			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation

			// debug
			System.out.print("Called server with SELECT. Table name: " + tableName + " query to be performed=");
			StringBuilder queryBuilder = new StringBuilder("SELECT ");
			// select what (rows)
			if (!filterColumns) {
				queryBuilder.append("*");
			} else {
				queryBuilder.append(filterColumnsArgs).append(" ");
			}
			// table name:
			queryBuilder.append("FROM ").append(tableName).append(" ");
			// select where (columns)
			if (filterRows) {
				queryBuilder.append("WHERE ").append(conditions).append(" ");
			}
			if (useSpecialArgs) {
				queryBuilder.append(specialArgs);
			}
			queryBuilder.append(";");

			String query = queryBuilder.toString();
			System.out.println(query);

			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) DatabaseController
					.handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[] { query });
			if (res == null) {
				// error
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				response.setMessageSent("Invalid input to SELECT query.");
			}
			response.setRequestType(ServerClientRequestTypes.ACK);
			response.setMessageSent(res);
			return response;
		}
	}

	// UPDATE table_name
	// SET column1 = value1, column2 = value2, ...
	// WHERE condition;
	// {tablename, "amuda1 = \"something\", amuda2 = else", "id=3"};
	/**
	 * HandleMessageUpdateInTable is a private class that implements the
	 * IServerSideFunction interface. It is responsible for handling UPDATE requests
	 * from clients, it checks the input for validity, calls the database controller
	 * to perform the update operation, and returns a response to the client. The
	 * input message should be of the format: Object[]{ tableName, setters (String),
	 * conditions};
	 * 
	 * @author Rotem
	 * @see IServerSideFunction
	 * @see SCCP
	 * @see ServerClientRequestTypes
	 * @see DatabaseController
	 * @see DatabaseOperation
	 *
	 */
	private static final class HandleMessageUpdateInTable implements IServerSideFunction {
		private static final int NUMBER_OF_ARGUMENTS_UPDATE = 3;

		@Override
		public SCCP handleMessage(SCCP message) {
			// The input message should look like:
			// Object[]{ tableName, setters (String), conditions};
			// I'm not going to go crazy here, no million billion tests:
			if (!message.getRequestType().equals(ServerClientRequestTypes.UPDATE)) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (Invalid type)");
			}
			if (!(message.getMessageSent() instanceof Object[])) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (not Object[])");
			}
			Object[] input = (Object[]) message.getMessageSent();
			// as I said - 3 arguments stored in
			if (input.length != NUMBER_OF_ARGUMENTS_UPDATE) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (length != 3)");
			}
			// we want three strings: table name, setters, conditions (setters example: "id
			// = 123", conditions example: "name = \"Doodoo\"")
			Class<?> types[] = new Class[] { String.class, String.class, String.class };

			if (TypeChecker.validate(input, (List<Class<?>>) Arrays.asList(types), 0)) {
				// prep response
				SCCP response = new SCCP();
				/**
				 * Now this is important - I can't be bothered to map this inside the database
				 * map - for now it's a direct call to the controller.
				 */
				// send the values to the database map:
				Object dbAnswer = DatabaseController.handleQuery(DatabaseOperation.UPDATE, input);
				Boolean dbAnsBoolean = (Boolean) dbAnswer;
				if (dbAnsBoolean) {
					System.out.println(
							"UPDATE opeartion failed (or the sql controller just didn't set its return statement properly so maybe ignore me!)");
					// failure
					response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
					// maybe we should create a special type for errors too, and pass a dedicated
					// one that will provide valuable info to the client?
					response.setMessageSent("ERROR: updating in DB failed"); // TODO: add some valuable information.
				} else {
					System.out.println("UPDATE operation success!");
					// socc secc
					response.setRequestType(ServerClientRequestTypes.ACK);
					response.setMessageSent(message.getMessageSent());
				}
			} else {
				throw new IllegalArgumentException(
						"Invalid input in HandleMessageUpdateInTable (3 String arguments expected!)");
			}

			return null;
		}

	}


	// EK LOGIN (Electronic Turk machine login)
	/**
	 * HandleMessageLoginEK is a private static final class that implements
	 * IServerSideFunction. It is responsible for handling login requests from the
	 * client side and verifying the credentials provided by the client. It first
	 * retrieves the username and password from the SCCP object passed to the
	 * handleMessage method. Then, it performs a SELECT query on the systemuser
	 * table in the database to check if the provided credentials match any existing
	 * user. If the query returns a result, it checks if the user is already logged
	 * in by performing another query. If the user is not logged in, it logs them in
	 * by updating the logged_users table in the database and returns an SCCP object
	 * with the request type LOGIN. If the user is already logged in, it returns an
	 * SCCP object with the request type LOGIN_FAILED_ALREADY_LOGGED_IN. If the
	 * query does not return any results, it returns an SCCP object with the request
	 * type ERROR_MESSAGE and the message "Invalid input for login".
	 * 
	 * @author Rotem
	 *
	 */
	public static final class HandleMessageLogin implements IServerSideFunction{
		private static final int SYSTEM_USER_TABLE_COL_COUNT = 9;
		private String username;
		private String password;
		private String roleString;

		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			if(loginMessage == null) {
				return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "login message is null");
			}
			setLoginFields(loginMessage);
			
			ArrayList<ArrayList<Object>> result = checkUserCredentials(username, password);
			// now, we expect result to be of size 1, 
			// and contain an array list with 9 columns! (else, we have an invalid login attempt)
			if(result.size() != 1) {
				return getMessageInvalidLogin(new SCCP());
			}
			// invalid database state check
			if(result.get(0).size() != SYSTEM_USER_TABLE_COL_COUNT) {
				throw new IllegalStateException("We expect a systemuser table with "+
			SYSTEM_USER_TABLE_COL_COUNT+" columns - Illegal database state");
			}
			// check if user is currently logged in		
			if(userAlreadyLoggedIn(result)) {
				return userAlreadyLoggedInMessage(new SCCP(), username);
			}
			// finalize login process (add user to logged users table)
			else {
				return finalizeLoginSuccess(result);
			}
		}


		private SCCP finalizeLoginSuccess(ArrayList<ArrayList<Object>> result) {
			SystemUser su = getSystemUserFromArrayList(username, password, 
					result);
			insertUserToLoggedUsersTable();
			return msgUserLoggedIn(new SCCP(), username, su);
		}


		private boolean userAlreadyLoggedIn(ArrayList<ArrayList<Object>> result) {
			// TODO Auto-generated method stub
			// now, check if it is already logged in
			ResultSet rs2 = (ResultSet)DatabaseController.
						handleQuery(DatabaseOperation.SELECT, 
						new Object[]{"SELECT username FROM logged_users WHERE username = '" +
						username + "'"});
			ArrayList<ArrayList<Object>> result2;
			try {
				result2 = lookForUserInLoggedUsersTable(result, rs2);
				if(result2.size() == 0) {
					return false;
				}
				else {
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Error checking logged_users table in LoginEK");
			}
	
			}


		private void insertUserToLoggedUsersTable() {
			boolean tmp = (Boolean)DatabaseController.handleQuery(DatabaseOperation.INSERT, new Object[] {"logged_users", false, new Object[]{"('"+username+"')"}});
			if(!tmp) {
				throw new IllegalStateException("Should never happen (crashed adding "+username+" to db, even though we know he wasn't there).");
			}
			System.out.println("Tried to add user to logged_users, was successful? (1 or 0): " + tmp);
		}


		private void setLoginFields(SCCP loginMessage) {
			username = (String)((Object[])loginMessage.getMessageSent())[0];
			password = (String)((Object[])loginMessage.getMessageSent())[1];
			System.out.println("Server got "+username + ", " + password + " and working on it");

		}


		private ArrayList<ArrayList<Object>> checkUserCredentials(String username, String password) {
			Object res = validateLoginCredentials(username, password);
			ResultSet rs = (ResultSet)res;
			ArrayList<ArrayList<Object>> result;
			try {
				result = convertResultSetToArrayList(rs);
				System.out.println("Finished working on first query");

				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
			
			return result;
		}


		private SCCP getMessageInvalidLogin(SCCP responseFromServer) {
			// invalid login
			responseFromServer.setRequestType(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT);
			responseFromServer.setMessageSent("Invalid input for login");
			return responseFromServer;
		}


		private ArrayList<ArrayList<Object>> lookForUserInLoggedUsersTable(ArrayList<ArrayList<Object>> result,
				ResultSet rs2) throws SQLException {
			ResultSetMetaData rsmd;
			rsmd = rs2.getMetaData();
			int columnsNumber2 = rsmd.getColumnCount();
			ArrayList<ArrayList<Object>> result2 = new ArrayList<>();
			while(rs2.next()) {
				ArrayList<Object> row2 = new ArrayList<>();
				for(int i=0;i<columnsNumber2;i++) {
					row2.add(rs2.getObject(i + 1));
				}
				// ROTEM FIXED THIS
				result2.add(row2);
			}
			// close rs2
			System.out.println("Wrote second query's result: " + result);
			rs2.close();
			return result2;
		}


		private SCCP userAlreadyLoggedInMessage(SCCP responseFromServer, String username) {
			// user aleady logged in
			responseFromServer.setRequestType(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN);
			responseFromServer.setMessageSent("Cannot login twice for user " + username);
			return responseFromServer;
		}


		private SCCP msgUserLoggedIn(SCCP responseFromServer, String username, SystemUser su) {
			System.out.println("Result for second query is empty (which is valid) - continuing");
			// we don't have user in table, good!
			// append username to this table and shut up and close

			responseFromServer.setRequestType(ServerClientRequestTypes.ACK);
			responseFromServer.setMessageSent(su); // pass the SystemUser object of the currently logged in user!
			return responseFromServer;
		}


		private SystemUser getSystemUserFromArrayList(String username, String password,
				ArrayList<ArrayList<Object>> result) {
			roleString = (String)result.get(0).get(8);
			Role role = Role.getRoleFromString(roleString);

			SystemUser su = new SystemUser(
					(Integer)result.get(0).get(0),
					result.get(0).get(1).toString(),
					result.get(0).get(2).toString(),
					result.get(0).get(3).toString(), 
					result.get(0).get(4).toString(), 
					result.get(0).get(5).toString(), 
					username, 
					password, 
					role);
			return su;
		}


		private ArrayList<ArrayList<Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
			ResultSetMetaData rsmd;
			rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			ArrayList<ArrayList<Object>> result = new ArrayList<>();
			while(rs.next()) {
				ArrayList<Object> row = new ArrayList<>();
				for(int i=0;i<columnsNumber;i++) {
					row.add(rs.getObject(i + 1));
				}
				result.add(row);
			}
			// close rs
			rs.close();
			return result;
		}


		private Object validateLoginCredentials(String username, String password) {
			Object res = DatabaseController.
					handleQuery(DatabaseOperation.SELECT, 
							new Object[]{"SELECT * FROM systemuser WHERE username = '" +
					username + "' AND password = '" + password+"';"});
			if(!(res instanceof ResultSet)) {
				throw new RuntimeException("Improbable error in LoginEK");
			}
			return res;
		}
	}

	// simple - get username, remove it from table
	/**
	 * This class represents the handle message function for logout. It implements
	 * the IServerSideFunction interface and overrides the handleMessage method. The
	 * handleMessage method is responsible for handling logout requests from the
	 * client. It takes an SCCP message as an input, which should contain the
	 * username of the user who wants to logout. The method first constructs a SQL
	 * query that deletes the user from the logged_users table in the database. Then
	 * it executes the query using the executeQuery method from the
	 * DatabaseSimpleOperation class. The result of the query is then wrapped in an
	 * SCCP object, with the request type set to ACK and the messageSent field set
	 * to the result of the query. The returned SCCP object is sent back to the
	 * client as a response.
	 * 
	 * @author Rotem
	 *
	 */
	public static final class HandleMessageLogout implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP message) {
			/**
			 * TODO: move this call to a dedicated "DELETE" handleQuery implementation in
			 * DatabaseOperationsMap
			 */
			String sqlQuery = "DELETE FROM " + DatabaseController.getSchemaName() + ".logged_users WHERE (username = '"
					+ message.getMessageSent() + "');";
			Boolean tmp = DatabaseSimpleOperation.executeQuery(sqlQuery);
			SCCP response = new SCCP(ServerClientRequestTypes.ACK, tmp);
			return response;
		}

	}

	/*
	 * This class returns List<String> for all machine names in the database
	 */
	/**
	 * The HandleMessageGetMachineNames class implements the IServerSideFunction
	 * interface and is responsible for handling client requests for getting the
	 * names of all machines in the system. The handleMessage() method takes in an
	 * SCCP message object as input, performs a SELECT query on the 'machine' table
	 * to retrieve all the machine names, and returns an SCCP object with the
	 * request type set to ACK and the message sent set to a list of machine names.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageGetMachineNames implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP message) {

			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) DatabaseController
					.handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[] { "SELECT machineName FROM machine;" });
			List<String> preparedList = new ArrayList<>();
			for (List<Object> row : result) {
				if (row.size() != 1) {
					throw new IllegalStateException("Every machine must have a name!");
				}
				preparedList.add(row.get(0).toString());
			}
			return new SCCP(ServerClientRequestTypes.ACK, preparedList);
		}

	}

	// explain it
	@SuppressWarnings("unused")
	private static final class HandleMessageGet implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			// TODO Auto-generated method stub
			// we are supposed to get this object:
			// SCCP(
			// ServerClientRequestTypes GET, Object[]{where_to_look(tableName),
			// boolean(getMany),
			// what_to_get(String[]{(column = match),(column = match)})}
			// )

			assert loginMessage.getMessageSent() instanceof Object[]; // assertion as shortcut

			String tableName = (String) ((Object[]) loginMessage.getMessageSent())[0];
			String[] columns = (String[]) ((Object[]) loginMessage.getMessageSent())[1];

			Object res = DatabaseController.handleQuery(DatabaseOperation.USER_LOGIN,
					new Object[] { tableName, loginMessage.getMessageSent() });
			if (res instanceof SystemUser) {

				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser) res);
			}

			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}

	}

	// Handle message of fetching all products with the category name contained in
	// fetchProductsMessage
	// fetchProductsMessage.getMessageSent() == "category"
	/**
	 * HandleMessageGet is a class that implements the IServerSideFunction
	 * interface. It handles the GET request from the client and retrieves data from
	 * the specified table in the database. The input message should contain the
	 * table name, the columns to retrieve, and a boolean indicating if multiple
	 * rows should be retrieved. It returns an SCCP object containing the request
	 * type (ACK or ERROR_MESSAGE) and the retrieved data or error message.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageFetchProducts implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchProductsMessage) {
			Object resultSetProducts = DatabaseController.handleQuery(DatabaseOperation.FETCH_PRODUCTS_BY_CATEGORY,
					new Object[] { fetchProductsMessage.getMessageSent() });

			if (resultSetProducts instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY, resultSetProducts);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}
	}

	// Handle message of fetching all order types from the order_type table.
	// fetchProductsMessage.getMessageSent() == "category"
	/**
	 * HandleMessageFetchOrders is a class that implements the IServerSideFunction
	 * interface. Its main purpose is to handle a request from the client to fetch
	 * orders from the database. The class receives a SCCP object, which is expected
	 * to contain a message of type ServerClientRequestTypes.FETCH_ORDERS, and an
	 * Object[] containing the parameters for the query to fetch the orders. The
	 * class then calls the handleQuery method of the DatabaseController class to
	 * execute the query, passing the parameters received in the SCCP object as
	 * arguments. The result of the query is expected to be an ArrayList of orders,
	 * which is then sent back to the client as the messageSent field of the SCCP
	 * object. If the query returns an error, the class sends a SCCP object with a
	 * message of type ServerClientRequestTypes.ERROR_MESSAGE.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageFetchOrders implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchOrdersMessage) {
			Object resultSetOrders = DatabaseController.handleQuery(DatabaseOperation.FETCH_ORDERS,
					new Object[] { fetchOrdersMessage.getMessageSent() });

			if (resultSetOrders instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_ORDERS, resultSetOrders);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}
	}

	public static final class HandleMessageDisplayPromotions implements IServerSideFunction {

		ArrayList<String> promotionNames = new ArrayList<String>();

		@Override
		public SCCP handleMessage(SCCP displayPromotionMessage) {
			promotionNames = new ArrayList<String>();
			try {
				ResultSet resultSet = (ResultSet) DatabaseController.handleQuery(
						DatabaseOperation.INSERT_PROMOTION_NAMES,
						new Object[] { "SELECT DISTINCT promotionName FROM promotions;" });
				resultSet.beforeFirst();
				while (resultSet.next()) {
					String promotionName = resultSet.getString("promotionName");
					promotionNames.add(promotionName);
				}
				resultSet.close();

				return new SCCP(ServerClientRequestTypes.DISPLAY, promotionNames);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}

	}

	/**
	 * HandleMessageDisplayPromotions is a class that implements the
	 * IServerSideFunction interface. The class is responsible for handling the
	 * display promotion request from the client by querying the database for all
	 * the distinct promotion names in the promotions table and returning them to
	 * the client in the form of an ArrayList of Strings. The class overrides the
	 * handleMessage method which takes in a SCCP object as a parameter. The SCCP
	 * object contains information about the display promotion request. The class
	 * uses the DatabaseController to execute a SELECT query to fetch all the
	 * distinct promotion names from the promotions table. The result is then stored
	 * in an ArrayList of Strings and returned to the client in a SCCP object with
	 * ServerClientRequestTypes.DISPLAY as the request type.
	 * 
	 * @author Rotem
	 *
	 */
	public static final class HandleMessageDisplaySelectedPromotions implements IServerSideFunction {

		@SuppressWarnings("unchecked")
		@Override
		public SCCP handleMessage(SCCP displayPromotionMessage) {
			ArrayList<Promotions> promotions;

			String promotionName = (String) displayPromotionMessage.getMessageSent();
			// promotions = (ArrayList<Promotions>) DatabaseController
			// .handleQuery(DatabaseOperation.SELECT,new Object[] {"SELECT * FROM promotions
			// WHERE promotionName = '\" + promotionName +"\';"});
			promotions = (ArrayList<Promotions>) DatabaseController.handleQuery(DatabaseOperation.SELECT_PROMOTION,
					new Object[] { "SELECT * FROM promotions WHERE promotionName = '" + promotionName + "';" });
			return new SCCP(ServerClientRequestTypes.DISPLAY, promotions);
		}
	}

	private static final class HandleMessageUpdateOnlineOrders implements IServerSideFunction {
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 1;

		@Override
		public SCCP handleMessage(SCCP message) {
			ServerClientRequestTypes type = message.getRequestType();
			SCCP response = new SCCP();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			Object[] objectsToUpdate;

			/// Start input validation

			// verify type
			if (!(type.equals(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}
			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// objects to update
				if (!(formattedMessage[0] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for whatToUpdate (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				objectsToUpdate = (Object[]) formattedMessage[0];

			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.UPDATE_ORDERS,
					new Object[] { objectsToUpdate });
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToUpdate); // send the array of objects we sent to add to the db, to
															// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: updaing DB failed"); // TODO: add some valuable information.
			}
			return response;
		}
	}

	/**
	 * HandleMessageUpdateOnlineOrders is a class that implements
	 * IServerSideFunction interface. It processes a message with type
	 * UPDATE_ONLINE_ORDERS and updates the corresponding orders in the database.
	 * The message should be in the form of
	 * SCCP(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS, new
	 * Object[]{objectsToUpdate}). where objectsToUpdate is an array of objects that
	 * needs to be updated in the database.
	 * 
	 * @return SCCP object with type ACK and messageSent as objectsToUpdate if the
	 *         update was successful, else returns SCCP object with type
	 *         ERROR_MESSAGE and messageSent as "ERROR: updaing DB failed"
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageAddPromotion implements IServerSideFunction {
		// this is defined as a constant since, for adding a promotion, we always want a
		// 3 element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 7;

		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName,
			// Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a
			// message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			String tableName;
			Boolean addMany;
			Object[] objectsToAdd;

			/// Start input validation

			// verify type
			if (!(type.equals(ServerClientRequestTypes.ADD))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}

			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// table name:
				if (!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean addMany
				if (!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for addMany (Boolean input) in handleMessage.");
				}
				if (!(formattedMessage[2] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for whatToAdd (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				tableName = (String) formattedMessage[0];
				addMany = (Boolean) formattedMessage[1];
				objectsToAdd = (Object[]) formattedMessage[2];
			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation

			// debug
			System.out.println("Called server with ADD_PROMOTION.\nTable name: " + tableName + "\nAdd many (boolean): "
					+ addMany + "\nObjects (to add): ");
			for (Object o : objectsToAdd) {
				System.out.println(o);
			}
			// end debug
			System.out.println("Calling the DB controller now (UNDER TEST)");
			// if addMany = false, the controller will use a different query that will
			// expect an array of size 1 (1 object)
			// now, we pass this three to the database controller.
			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.INSERT,
					new Object[] { tableName, addMany, objectsToAdd });

			// here, we return the proper message to the client
			// we will need some imports to do so (NOT IMPLEMENTED)
			System.out.println("Returning result to client (UNDER TEST)");
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToAdd); // send the array of objects we sent to add to the db, to
														// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
			}
			return response;
		}

	}

	/**
	 * HandleMessageDisplayPromotionsToActive is a private static final class that
	 * implements IServerSideFunction. This class is responsible for handling the
	 * message sent by the client to display the promotions to the active regional
	 * manager. It first retrieves the workerID from the message sent by the client
	 * and then retrieves the promotions associated with the location of the manager
	 * using the workerID. The class then returns the fetched promotions in a SCCP
	 * object with the request type as ServerClientRequestTypes.DISPLAY.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageDisplayPromotionsToActive implements IServerSideFunction {
		ArrayList<Promotions> promotionNames = new ArrayList<Promotions>();

		@SuppressWarnings("unchecked")
		@Override
		public SCCP handleMessage(SCCP message) {
			Integer workerID = (Integer) message.getMessageSent();
			promotionNames = (ArrayList<Promotions>) DatabaseController.handleQuery(DatabaseOperation.SELECT,
					new Object[] {
							"SELECT * FROM promotions WHERE locationId = (SELECT locationId FROM manager_location WHERE idRegionalManager = "
									+ workerID + ");" });

			return new SCCP(ServerClientRequestTypes.DISPLAY, promotionNames);
		}
	}

	private static final class HandleMessageUpdateStatus implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP message) {
			String promotionID = (String) message.getMessageSent();
			return new SCCP(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS,
					DatabaseController.handleQuery(DatabaseOperation.UPDATE_PROMOTION_STATUS,
							new Object[] { "UPDATE promotions\n" + "SET promotionStatus = NOT promotionStatus\n"
									+ "WHERE promotionId = '" + promotionID + "';\n" + "" }));
		}
	}

	/**
	 * HandleMessageUpdateStatus is a private static final class that implements the
	 * IServerSideFunction interface. It's purpose is to handle the update of status
	 * of a promotion in the database. It receives an SCCP message as an input,
	 * which should contain the promotionID of the promotion whose status is to be
	 * updated. The class then calls the DatabaseController's handleQuery method
	 * with the appropriate arguments to update the status of the promotion in the
	 * database. Finally, it returns an SCCP message with the request type as
	 * UPDATE_PROMOTION_STATUS and the messageSent as the result of the handleQuery
	 * method.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageFetchMachines implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchMachinesMessage) {
			Object resultSetMachines = DatabaseController.handleQuery(DatabaseOperation.FETCH_MACHINES_BY_LOCATION,
					new Object[] { fetchMachinesMessage.getMessageSent() });

			if (resultSetMachines instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION, resultSetMachines);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}
	}

	private static final class HandleMessageFetchProductsInMachine implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchMachinesMessage) {
			Object resultSetProducts = DatabaseController.handleQuery(DatabaseOperation.FETCH_PRODUCTS_IN_MACHINE,
					new Object[] { fetchMachinesMessage.getMessageSent() });

			if (resultSetProducts instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE, resultSetProducts);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}
	}

	/**
	 * HandleMessageFetchProductsInMachine is a private static final class that
	 * implements the IServerSideFunction interface. This class is responsible for
	 * fetching all the products in a specific machine from the database. The class
	 * takes in a SCCP message as input, which must contain the machine name. The
	 * class then executes a FETCH_PRODUCTS_IN_MACHINE operation on the
	 * DatabaseController with the machine name as the parameter. The resulting
	 * object is then checked to see if it is an ArrayList and if it is, it is
	 * returned as a SCCP message with the request type FETCH_PRODUCTS_IN_MACHINE.
	 * If the resulting object is not an ArrayList, an error message SCCP is
	 * returned.
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageUpdateProductsInMachine implements IServerSideFunction {
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 1;

		@Override
		public SCCP handleMessage(SCCP message) {
			ServerClientRequestTypes type = message.getRequestType();
			SCCP response = new SCCP();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			Object[] productsToUpdate;

			/// Start input validation

			// verify type
			if (!(type.equals(ServerClientRequestTypes.UPDATE_PRODUCTS_IN_MACHINE))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}
			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// objects to update
				if (!(formattedMessage[0] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for machinesToUpdate (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				productsToUpdate = (Object[]) formattedMessage[0];

			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.UPDATE_PRODUCTS_IN_MACHINE,
					new Object[] { productsToUpdate });
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(productsToUpdate); // send the array of objects we sent to add to the db, to
															// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: updaing DB failed"); // TODO: add some valuable information.
			}
			return response;
		}
	}

	/**
	 * HandleMessageRemove is a private static final class that implements the
	 * IServerSideFunction interface. It handles the removal of a specific row from
	 * the database based on the information passed in a SCCP message. It takes a
	 * SCCP message as input and returns a SCCP message as output. The SCCP message
	 * passed as input must contain an Object array containing the following
	 * information in order: String - table name String - where clause The returned
	 * SCCP message will contain the request type as ServerClientRequestTypes.ACK if
	 * the removal was successful and the message sent will be a string "Success!".
	 * If the removal was not successful, the request type will be
	 * ServerClientRequestTypes.ERROR_MESSAGE and the message sent will be a string
	 * "Row found in the database!".
	 * 
	 * @author Rotem
	 *
	 */
	private static final class HandleMessageRemove implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP message) {
			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.DELETE,
					(Object[]) message.getMessageSent());
			if (res)
				return new SCCP(ServerClientRequestTypes.ACK, "Success!");
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "Row found in the database!");
		}

	}

	/**
	 * HandleMessageMap is a HashMap that contains the mapping of different
	 * ServerClientRequestTypes to their corresponding IServerSideFunction
	 * implementations. The class is responsible for handling the different types of
	 * messages received by the server, by invoking the corresponding function based
	 * on the request type. It contains the following implementations:
	 * HandleMessageAddToTable - for adding an entry to a table.
	 * HandleMessageSelectFromTable - for selecting an entry from a table.
	 * HandleMessageUpdateInTable - for updating an entry in a table.
	 * HandleMessageLogin - for logging in a user. HandleMessageLoginEK - for
	 * logging in an EK user. HandleMessageLogout - for logging out a user.
	 * HandleMessageGetMachineNames - for fetching all machine names.
	 * HandleMessageFetchMachines - for fetching machines by location.
	 * HandleMessageFetchProductsInMachine - for fetching products in a machine.
	 * HandleMessageUpdateProductsInMachine - for updating products in a machine.
	 * HandleMessageFetchProducts - for fetching products by category.
	 * HandleMessageFetchOrders - for fetching orders.
	 * HandleMessageDisplayPromotions - for displaying promotions.
	 * HandleMessageDisplaySelectedPromotions - for displaying selected promotions.
	 * HandleMessageUpdateOnlineOrders - for updating online orders.
	 * HandleMessageAddPromotion - for adding a promotion. HandleMessageRemove - for
	 * removing an entry from a table. HandleMessageUpdateStatus - for updating the
	 * status of a promotion. HandleMessageDisplayPromotionsToActive - for
	 * displaying promotions to active managers.
	 */
	private static HashMap<ServerClientRequestTypes, IServerSideFunction> map = new HashMap<ServerClientRequestTypes, IServerSideFunction>() {

		private static final long serialVersionUID = 1L;

		{
			/*
			 * A dedicated function for a message to add something to some table
			 * (server-side)
			 * 
			 */
			this.put(ServerClientRequestTypes.ADD, new HandleMessageAddToTable());
			this.put(ServerClientRequestTypes.SELECT, new HandleMessageSelectFromTable());
			this.put(ServerClientRequestTypes.UPDATE, new HandleMessageUpdateInTable());
			this.put(ServerClientRequestTypes.LOGIN, new HandleMessageLogin());
			
			// Rotem removed this class as the functionality is identical to regular login
			//this.put(ServerClientRequestTypes.EK_LOGIN, new HandleMessageLogin());
			
			this.put(ServerClientRequestTypes.LOGOUT, new HandleMessageLogout());
			this.put(ServerClientRequestTypes.REQUEST_ALL_MACHINES, new HandleMessageGetMachineNames());
			this.put(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION, new HandleMessageFetchMachines());
			this.put(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE, new HandleMessageFetchProductsInMachine());
			this.put(ServerClientRequestTypes.UPDATE_PRODUCTS_IN_MACHINE, new HandleMessageUpdateProductsInMachine());
			this.put(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY, new HandleMessageFetchProducts());
			this.put(ServerClientRequestTypes.FETCH_ORDERS, new HandleMessageFetchOrders());
			this.put(ServerClientRequestTypes.DISPLAY_PROMOTIONS, new HandleMessageDisplayPromotions());
			this.put(ServerClientRequestTypes.DISPLAY_SELECTED_PROMOTIONS,
					new HandleMessageDisplaySelectedPromotions());
			this.put(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS, new HandleMessageUpdateOnlineOrders());
			this.put(ServerClientRequestTypes.ADD_PROMOTION, new HandleMessageAddPromotion());
			this.put(ServerClientRequestTypes.REMOVE, new HandleMessageRemove());
			this.put(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS, new HandleMessageUpdateStatus());
			this.put(ServerClientRequestTypes.DISPLAY_PROMOTIONS_TO_ACTIVE,
					new HandleMessageDisplayPromotionsToActive());

			this.put(ServerClientRequestTypes.GET_PROMOTIONS_BY_LOCATION, new HandleMessageGetPromotionsByLocation());
			this.put(ServerClientRequestTypes.GET_ORDERS_FOR_CANCELLATION, new HandleMessageGetOrdersForCancellation());

			
		}
	};

	/**
	 * This method returns the map of server-side functions that are associated with
	 * specific {@link ServerClientRequestTypes}. The map is used to determine which
	 * function should be executed for a specific type of request.
	 * 
	 * @return a HashMap containing the association of request types to their
	 *         corresponding server-side functions.
	 */
	public static HashMap<ServerClientRequestTypes, IServerSideFunction> getMap() {
		return map;
	}

}
