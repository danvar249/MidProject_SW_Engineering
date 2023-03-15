package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class contains methods for executing SQL statements to interact with the
 * database. The class uses the DatabaseController class to establish a
 * connection to the database. executeQuery() : it takes a SQL statement and an
 * array of objects as parameters and executes the statement. It returns a
 * boolean indicating whether the execution was successful or not.
 * executeQueryWithResults(): it takes a SQL statement and an array of objects
 * as parameters and executes the statement and returns the result set.
 * executeQuery(): it takes only a SQL statement and executes the statement. It
 * returns a boolean indicating whether the execution was successful or not. If
 * an exception occurs, the methods print the exception message and return
 * false.
 * 
 * @author Rotem
 *
 */
public class DatabaseSimpleOperation {
	/**
	 * 
	 * Executes a SQL statement with the given parameters and returns a boolean
	 * indicating if the execution was successful or not.
	 * 
	 * @param sqlStatement The SQL statement to be executed
	 * @param params       an array of objects that will be set as the parameters
	 *                     for the prepared statement
	 * @return true if the statement was executed successfully, false otherwise
	 */
	public static boolean executeQuery(String sqlStatement, Object[] params) {
		PreparedStatement ps;
		try {
			ps = DatabaseController.getConnection().prepareStatement(sqlStatement);
			if(params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			// System.out.println("prepared statement : " + ps.toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return false;
		}
	}

	/**
	 * 
	 * Executes a given SQL statement and returns a boolean indicating if the
	 * execution was successful or not.
	 * 
	 * @param sqlStatement The SQL statement to be executed
	 * @return true if the statement was executed successfully, false otherwise
	 */
	public static boolean executeQuery(String sqlStatement) {
		// we don't need ps or conn here
		try {
			// System.out.println("prepared statement : " + ps.toString());
			return DatabaseController.getConnection().prepareStatement(sqlStatement).executeUpdate() > 0;
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return false;
		}
	}

	/**
	 * 
	 * Executes a given SQL statement with the given parameters and returns a
	 * ResultSet containing the results of the query.
	 * 
	 * @param sqlStatement The SQL statement to be executed
	 * @param params       an array of objects that will be set as the parameters
	 *                     for the prepared statement
	 * @return ResultSet containing the results of the query, or null if the
	 *         execution was not successful
	 */
	public static ResultSet executeQueryWithResults(String sqlStatement, Object[] params) {
		PreparedStatement ps;
		try {
			System.out.println(sqlStatement);
			ps = DatabaseController.getConnection().prepareStatement(sqlStatement);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			System.out.println("SQL to execute: " + sqlStatement);
			// System.out.println("prepared statement : " + ps.toString());

			return ps.executeQuery();
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return null;
		}
	}

}
