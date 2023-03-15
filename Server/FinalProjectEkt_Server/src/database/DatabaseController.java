package database;

import java.sql.*;

//import com.mysql.cj.admin.ServerController;

/**
 * The DatabaseController class is used to handle all database operations in the application.
 * The class is implemented as a singleton pattern, and the {@link #getInstance()} method is used to retrieve the singleton instance of the class.
 * The class contains a private static final String URL, which holds the url of the database.
 * It also contains private static variables for the database user name and password, that are used to connect to the database.
 * The class contains a private static Connection variable, which holds the connection to the database, that is opened and closed as needed.
 * The class provides a public static method {@link #getConnection()} which returns the connection to the database.
 * It also provides a public static method {@link #checkLoginCredentials()} which attempts to connect to the database using the current user name and password, and returns true if the connection is successful, false otherwise.
 * @author Rotem
 *
 */
public class DatabaseController {
	// constants
	// URL is defined with ssl disabled because the java version
	private static final String URL = "jdbc:mysql://localhost/ektdb?serverTimezone=IST&sslMode=DISABLED&allowPublicKeyRetrieval=true";	// Rotem -> read line 28
	//private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private static String dbName = "root";
	private static String dbPassword;

	// Singleton instance of the class.
	private static DatabaseController instance;
	// TODO:
	// for test only
	private static Connection con;
	private static String schemaName="ektdb";
	/**
	 * Private constructor for the DatabaseController class.
	 * Initializes the driver and creates a connection to the database using the URL, user name and password.
	 * The driver is not initialized by the constructor, it is done by the method getConnection().
	 * If the connection is successful, it will print "SQL connection succeed" to the console, otherwise it will print the error message, SQLState and VendorError.
	 */
	private DatabaseController() {
	
		try {
			//Class.forName(DRIVER_NAME).getDeclaredConstructor().newInstance();
			
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		try {
			con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	/**
	 * Public method that returns an instance of the DatabaseController class.
	 * If an instance of the class does not exist, it creates one by calling the private constructor.
	 * @return The instance of the DatabaseController.
	 */
	public static DatabaseController getInstance() {
		if (instance == null)
			instance = new DatabaseController();
		return instance;
	}
	/**
	 * getConnection is a method that returns the connection to the database.
	 * It first checks if the connection is open, if it is it returns the connection,
	 * otherwise, it opens a new connection to the database using the URL, database username and password.
	 * It also prints out success or failure messages for the driver definition and connection.
	 * @return con - the connection to the database.
	 */
	public static Connection getConnection() {
		try {
			if(con != null && !con.isClosed())
				return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// If connection isnt open, open it
		try {
			//Class.forName(DRIVER_NAME).getDeclaredConstructor().newInstance();
			
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return con;
	}
	/**
	 * This method check the login credentials by trying to connect to the database with the given username and password.
	 * If the connection is successful, it closes the connection and returns true, otherwise it returns false.
	 * If there is any other SQLException, it throws the exception.
	 * @throws SQLException if any other SQLException occurs while trying to connect to the database.
	 * @return true if the login credentials are correct, false otherwise.
	 */
	public static boolean checkLoginCredentials() throws SQLException {
		try {
			Connection tmp_con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			if(tmp_con != null) {
				tmp_con.close();
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// password is incorrect
			if(e.getMessage().contains("Access denied for user "))
				return false;
			// error (TODO)
			throw e;
		}
	}
	/**
	 * This method closes the current connection to the database.
	 * It uses the {@link Connection#close()} method to close the connection and then it prints the status of the connection using {@link Connection#isClosed()} method.
	 * If an exception is caught, it will be printed using the {@link SQLException#printStackTrace()} method.
	 */
	public static void closeDBController() {
		try {
			getConnection().close();
			System.out.println("SQL controller is closed: " + con.isClosed());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return The current database username.
	 */
	public static String getDatabaseUserName() {
		return dbName;
	}
	/**
	 * Sets the database username.
	 * @param databaseUserName The new database username.
	 */
	public static void setDatabaseUserName(String databaseUserName) {
		dbName = databaseUserName;
	}
	/**
	 * 
	 * @return The current database password.
	 */
	public static String getDatabasePassword() {
		return dbPassword;
	}
	/**
	 * Sets the database password.
	 * @param databasePassword The new database password.
	 */
	public static void setDatabasePassword(String databasePassword) {
		dbPassword = databasePassword;
	}

	
	// please, just ignore this one, I left it only to keep track of how it was done before the map existed.
	/**
	 * This method handles different database operations such as INSERT.
	 * It uses the objectsToAdd array to add data to the table specified by tableName.
	 * It should be noted that this method is currently ignored and is only kept for tracking purposes.
	 * @param operation the type of database operation to be performed
	 * @param tableName the name of the table to be affected by the operation
	 * @param addMany a boolean value that indicates if multiple objects need to be added
	 * @param objectsToAdd an array of objects to be added to the table
	 * @return a boolean value indicating if the operation was successful
	 */
	public static boolean handleQuery(DatabaseOperation operation, String tableName, Boolean addMany,
			Object[] objectsToAdd) {
		
		if(operation.equals(DatabaseOperation.INSERT)) {
			// add
			//(?, ?, ?, ?, ?, ?, ?, ?)
			String addToTable =
					"INSERT INTO " +getSchemaName()+"."+tableName+ " VALUES ";
			// sql format set in each logic class
			for(Object o : objectsToAdd) {
				
				String currentAddToTable = (new StringBuilder(addToTable)).append(o.toString()).append(";").toString();
				System.out.println("Tring to sql:");
				System.out.println(currentAddToTable);
				if(!DatabaseSimpleOperation.executeQuery(currentAddToTable)) {
					return false; // TODO: add granularity
				}
			}
			return true;

		}
		return false;
	}
	/**
	 * 
	 * This method is used to handle a database operation. It takes in a {@link DatabaseOperation} enumeration and an array of objects as parameters.
	 * It checks if the {@link DatabaseOperationsMap} contains the specified operation, and if it does, it calls the appropriate action
	 * from the map with the provided parameters.
	 * @param operation The {@link DatabaseOperation} enumeration specifying the desired operation.
	 * @param params An array of objects containing the necessary parameters for the operation.
	 * @return An object returned by the specified operation, or an exception if the operation is not supported.
	 */
	public static Object handleQuery(DatabaseOperation operation, Object[] params) {
		// TODO: add error checking
		if(!DatabaseOperationsMap.getMap().containsKey(operation)) {
			throw new UnsupportedOperationException("SQL Controller does not support the operation " + operation + ". Contact your doctor about Levi-tra today!");
		}
		return DatabaseOperationsMap.getMap().get(operation).getDatabaseAction(params);
	}
	/**
	 * Retrieves the name of the schema used in the database.
	 * @return the name of the schema as a string.
	 */
	public static String getSchemaName() {
		return schemaName;
	}
	
	
}
