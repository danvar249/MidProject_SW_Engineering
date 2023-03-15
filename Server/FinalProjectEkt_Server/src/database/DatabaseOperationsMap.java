package database;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import logic.*;
import logic.Order.*;

public class DatabaseOperationsMap {
	private static String SCHEMA_EKRUT = "ektdb";

	/*
	 * 1.15... IMPORT SIM.
	 */
	/**
	 * protected static final class DatabaseActionImportSimulation implements
	 * IDatabaseAction This class is responsible for importing simulation data into
	 * the systemuser and manager_location tables. The class retrieves all rows from
	 * the ektdb.external_users table, formats the data, and inserts it into the
	 * appropriate tables. The class also checks for null values, invalid data
	 * formats, and enforces constraints on the data.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionImportSimulation implements IDatabaseAction {
		@Override
		public Object getDatabaseAction(Object[] params) {
			// select * from ektdb.external_users;
			Object res = DatabaseController.handleQuery(DatabaseOperation.GENERIC_SELECT,
					new Object[] { "SELECT * FROM ektdb.external_users" });
			if (res == null || !(res instanceof ArrayList)) {
				return false;
			}
			ArrayList<?> rows = (ArrayList<?>) res;
			// format of row in rows: [id, firstName, lastName, phoneNumber, emailAddress,
			// creditCard, username, password, typeOfUser, locationID]
			// types : [int, string, string, string, string, string, string, string, string,
			// int ]

			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> rowsAsArrayListOfArrayListsOfObjects = (ArrayList<ArrayList<Object>>) rows;
			for (ArrayList<Object> row : rowsAsArrayListOfArrayListsOfObjects) {
				String userID;
				String fName, lName, phone, email, cCard, uName, password, userTypeRole;
				Integer locationId;
				if (row.size() != 10) {
					return false;
				}
				for (Object o : row) {
					// we only want to allow locationId to be null (since a non-regional-manager
					// user has no inherent location)
					if (o == null && o != row.get(9)) {
						return false;
					}
				}
				userID = row.get(0).toString();
				fName = row.get(1).toString();
				lName = row.get(2).toString();
				phone = row.get(3).toString();
				email = row.get(4).toString();
				cCard = row.get(5).toString();
				uName = row.get(6).toString();
				password = row.get(7).toString();
				userTypeRole = row.get(8).toString();
				locationId = (Integer) row.get(9);

				if (!userID.matches("^[0-9]+$")) {
					return false;
				}
				Integer uIdAsInt = Integer.valueOf(userID);
				SystemUser userToAdd = new SystemUser(uIdAsInt, fName, lName, phone, email, cCard, uName, password,
						userTypeRole);
				System.out.println(
						"Import simulation is now inserting user=" + userToAdd + ". Inserting into systemuser");
				// insert into our two relevant user-management related tables:
				// insert this object into systemuser
				// INSERT INTO systemuser VALUES (userID, fName, lName, phone, email, cCard,
				// uName, password, userTypeRole);
				DatabaseController.handleQuery(DatabaseOperation.INSERT,
						new Object[] { "systemuser", false, new Object[] { userToAdd } });

				if (userToAdd.getRole().equals(Role.REGIONAL_MANAGER)) {
					System.out.println("User " + userToAdd + " is a regional manager. added user with location "
							+ locationId + " into table manager_location");

					// INSERT INTO manager_location VALUES (userID, locationId);
					DatabaseController.handleQuery(DatabaseOperation.INSERT, new Object[] { "manager_location", false,
							new Object[] { "(" + userID + ", " + locationId + ")" } });
				}
			}

			return true;
		}
	}

	// I recommend not using this one - it returns ResultSet to server so you have
	// to parse it
	// use GenericSelect instead (call the map with GENERIC_SELECT), and you will
	// get
	// an array list of rows from the DB.
	/**
	 * protected static final class DatabaseActionSelect implements IDatabaseAction
	 * This class is responsible for performing a SELECT operation on the database.
	 * The class only accepts a single argument, which should be a string containing
	 * the SELECT query to be executed. The class checks the number and type of
	 * arguments, and throws an IllegalArgumentException if they are not valid. The
	 * class then executes the query and returns the ResultSet containing the query
	 * results.
	 * 
	 * @author Rotem
	 */
	protected static final class DatabaseActionSelect implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			ResultSet rs = null;
			if (params.length != 1)
				throw new IllegalArgumentException("Invalid argument cound for database SELECT (pass only 1)");
			if (!(params[0] instanceof String))
				throw new IllegalArgumentException(
						"Can only accept a string to database SELECT (passed " + params.getClass().getName() + ")");

			// do it
			String query = (String) params[0];
			rs = DatabaseSimpleOperation.executeQueryWithResults(query, null);

			// return result set
			return rs;
		}

	}

	/*
	 * This one returns TO THE SERVER the following object:
	 * ArrayList<ArrayList<Object>> Where the outer ArrayList contains each row
	 * received from the DB, and the inner contains the chosen columns for each row
	 * TESTED! (works)
	 */
	protected static final class DatabaseActionGenericSelect implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			ResultSet rs = null;
			if (params.length != 1)
				throw new IllegalArgumentException("Invalid argument cound for database GENERIC SELECT (pass only 1)");
			if (!(params[0] instanceof String))
				throw new IllegalArgumentException("Can only accept a string to database GENERIC SELECT (passed "
						+ params.getClass().getName() + ")");

			// do it
			String query = (String) params[0];
			rs = DatabaseSimpleOperation.executeQueryWithResults(query, null);
			if(rs == null)
				return rs;
			ResultSetMetaData rsmd;
			try {
				rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				ArrayList<ArrayList<Object>> result = new ArrayList<>();
				while (rs.next()) {
					ArrayList<Object> row = new ArrayList<>();
					for (int i = 0; i < columnsNumber; i++) {
						row.add(rs.getObject(i + 1));

					}
					result.add(row);
				}
				rs.close();
				// here, we return to the server
				return result;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// return result set
			return null;
		}

	}

	/*
	 * Generic update query: "update table tablename set setters where condition;"
	 */
	/**
	 * protected static final class DatabaseActionGenericSelect implements
	 * IDatabaseAction This class is responsible for performing a Generic SELECT
	 * operation on the database. The class only accepts a single argument, which
	 * should be a string containing the SELECT query to be executed. The class
	 * checks the number and type of arguments, and throws an
	 * IllegalArgumentException if they are not valid. The class then executes the
	 * query, retrieves the ResultSet containing the query results, and converts the
	 * result set to an ArrayList of ArrayList of Objects and returns it.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionGenericUpdate implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			// get the values:
			String tableName, setters, conditions;
			tableName = (String) params[0];
			setters = (String) params[1];
			conditions = (String) params[2];

			StringBuilder sqlQuery = new StringBuilder("UPDATE ");
			sqlQuery.append(DatabaseController.getSchemaName()).append(".").append(tableName).append(" SET ");
			sqlQuery.append(setters).append(" WHERE ");
			sqlQuery.append(conditions).append(";");
			System.out.println("Sending query to database=" + sqlQuery.toString());
			return DatabaseSimpleOperation.executeQuery(sqlQuery.toString());
		}

	}

	/*
	 * Generic delete query: "Delete from tablename where condition;"
	 */
	/**
	 * The class DatabaseActionDelete implements the IDatabaseAction interface and
	 * is used to delete data from a database table. It takes in three parameters: a
	 * table name, setters, and conditions. The table name and setters are used to
	 * construct the SQL DELETE query, and the conditions are used to specify which
	 * rows to delete. The query is then executed using the
	 * DatabaseSimpleOperation.executeQuery method. The method returns true if the
	 * deletion is successful.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionDelete implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			String tableName, setters, conditions;
			tableName = (String) params[0];
			setters = (String) params[1];
			conditions = (String) params[2];

			StringBuilder sqlQuery = new StringBuilder("DELETE FROM ");
			sqlQuery.append(DatabaseController.getSchemaName()).append(".").append(tableName);
			sqlQuery.append(setters).append(" WHERE ");
			sqlQuery.append(conditions).append(";");
			System.out.println("Sending query to database=" + sqlQuery.toString());
			DatabaseSimpleOperation.executeQuery(sqlQuery.toString());
			return true;
		}
	}

	// this has to be protected (not private) because we need it in
	// DatabaseController
	/**
	 * The class DatabaseActionInsert implements the IDatabaseAction interface and
	 * is used to insert data into a database table. It takes in three parameters:
	 * table name, a boolean flag indicating whether to add many objects at once,
	 * and an array of objects to add. The table name is used to construct the SQL
	 * INSERT query, and the objects are used to specify the data to be inserted.
	 * The query is then executed using the DatabaseSimpleOperation.executeQuery
	 * method. If the addMany flag is set to true, the method will insert all
	 * objects in the array, and will return false if any of them fail. If the
	 * addMany flag is set to false, the method will insert only the first object in
	 * the array and will return false if it fails. The method returns true if the
	 * insertion is successful.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionInsert implements IDatabaseAction {
		private String tableName;
		private Boolean addMany;
		private Object[] objectsToAdd;

		// this performs the action we had in DatabaseController for .INSERT
		// returns a boolean (Boolean) as an Object (because we implement an interface
		// we have to be general)
		@Override
		public Object getDatabaseAction(Object[] params) {
			// add input check - I can't be bothered to such an extent
			if (params.length != 3)
				throw new IllegalArgumentException("Amount of parameters is not 3 in getDatabaseAction (INSERT)");
			// set class fields:
			// *again - we might need to test input for validity, but it's too much for me
			// right now
			setTableName((String) params[0]);
			setAddMany((Boolean) params[1]);
			setObjectsToAdd((Object[]) params[2]);

			String addToTable = "INSERT INTO " + DatabaseOperationsMap.SCHEMA_EKRUT + "." + getTableName() + " VALUES ";
			// we need:
			// SQL format set in each logic class
			for (Object o : getObjectsToAdd()) {

				String currentAddToTable = (new StringBuilder(addToTable)).append(o.toString()).append(";").toString();
				System.out.println("Writing to SQL:");
				System.out.println(currentAddToTable);
				if (!DatabaseSimpleOperation.executeQuery(currentAddToTable)) {
					Boolean tmpAddMany = getAddMany();
					cleanUp(); // careful with this line
					if (tmpAddMany) {
						// TODO: add granularity (idea - return a list of objects NOT YET INSERTED)
						return false;
					}
					return false;
				}
			}
			cleanUp(); // careful with this line
			return true;
		}

		/**
		 * The cleanUp method is used to set all class fields (tableName, addMany, and
		 * objectsToAdd) to null.
		 */
		// after action - clean up static stuff
		public void cleanUp() {
			tableName = null;
			addMany = null;
			objectsToAdd = null;

		}

		/**
		 * @return the current table name field.
		 */
		public String getTableName() {
			return tableName;
		}

		/**
		 * The setTableName method sets the table name field to the given value.
		 * 
		 * @param tableName The new table name to be set.
		 */
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		/**
		 * 
		 * @return the current addMany field
		 */
		public Boolean getAddMany() {
			return addMany;
		}

		/**
		 * The setAddMany method sets the addMany field to the given value.
		 * 
		 * @param addMany The new value for addMany field.
		 */
		public void setAddMany(Boolean addMany) {
			this.addMany = addMany;
		}

		/**
		 * 
		 * @return the current objectsToAdd field.
		 */
		public Object[] getObjectsToAdd() {
			return objectsToAdd;
		}

		/**
		 * 
		 * The setObjectsToAdd method sets the objectsToAdd field to the given value.
		 * 
		 * @param objectsToAdd The new value for objectsToAdd field.
		 */
		public void setObjectsToAdd(Object[] objectsToAdd) {
			this.objectsToAdd = objectsToAdd;
		}

	}

	// Return currently logged in user back to client
	/**
	 * The class DatabaseActionSelectForLogin implements the IDatabaseAction
	 * interface and is used to select data from a database table for login
	 * purposes. It takes in two parameters: a table name, and a string array
	 * containing the user and password. The table name is used to construct the SQL
	 * SELECT query, and the user and password are used to specify which rows to
	 * select. The query is then executed using the
	 * DatabaseSimpleOperation.executeQueryWithResults method. The method then
	 * constructs a new SystemUser object from the selected data, if a match is
	 * found, and returns it. If no match is found, the method returns null.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionSelectForLogin implements IDatabaseAction {
		private String tableName;

		// this performs the action we had in DatabaseController for .INSERT
		// returns a boolean (Boolean) as an Object (because we implement an interface
		// we have to be general)
		@Override
		public Object getDatabaseAction(Object[] params) {
			// add input check - I can't be bothered to such an extent

			String user, pass;
			tableName = (String) params[0];
			String[] up = (String[]) params[1];
			user = up[0];
			pass = up[1];

			String sqlQuery = "SELECT * FROM " + DatabaseOperationsMap.SCHEMA_EKRUT + "." + tableName
					+ " WHERE username = \"" + user + "\" AND password = \"" + pass + "\";";
			ResultSet queryResult = DatabaseSimpleOperation.executeQueryWithResults(sqlQuery, null);
			SystemUser connectedUser = null;
			try {
				queryResult.next();
				if (queryResult.getRow() != 0) {
					// get fields from DB
					Integer idNew = queryResult.getInt(1);

					String fname = queryResult.getString(2);

					String lname = queryResult.getString(3);

					String email = queryResult.getString(4);

					String fone = queryResult.getString(5);

					String cc = queryResult.getString(6);

					String retUser = queryResult.getString(7);

					String retPass = queryResult.getString(8);

					String role = queryResult.getString(9);
					connectedUser = new SystemUser(idNew, fname, lname, fone, email, cc, retUser, retPass,
							role.toLowerCase());
					System.out.println(role);
				}
				queryResult.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connectedUser;

		}

	}

	// Class which is used to return a result set of all products with the category
	// name categoryName[0]
	/**
	 * The class DatabaseActionSelectForFetchProducts implements the IDatabaseAction
	 * interface and is used to select data from a product table. It takes in one
	 * parameter: a category name. The table name is hardcoded as "product". The
	 * category name is used to construct the SQL SELECT query. If category name is
	 * "ALL PRODUCTS" it will fetch all products, otherwise it will fetch all
	 * products of a specified category or subcategory. The query is then executed
	 * using the DatabaseSimpleOperation.executeQueryWithResults method. The method
	 * then constructs an ArrayList of Product objects from the selected data, and
	 * returns it. If no match is found, the method returns an empty ArrayList.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionSelectForFetchProducts implements IDatabaseAction {
		private String tableName;

		@Override
		public Object getDatabaseAction(Object[] categoryName) {
			tableName = "product";
			String sqlQuery;
			String productCategory = (String) categoryName[0];
			if (productCategory.equals("ALL PRODUCTS")) {
				sqlQuery = "SELECT * FROM " + DatabaseOperationsMap.SCHEMA_EKRUT + "." + tableName;
				System.out.println(sqlQuery);
			} else {
				sqlQuery = "Select * FROM " + DatabaseOperationsMap.SCHEMA_EKRUT + "." + tableName
						+ " Where category = \"" + productCategory + "\" OR subCategory =" + " \"" + productCategory
						+ "\";";
			}

			// Uses simpler version of execute query with one input string variable (the
			// requested sql query)
			ResultSet fetchProductsResultSet = DatabaseSimpleOperation.executeQueryWithResults(sqlQuery, null);

			ArrayList<Product> arrayOfProducts = new ArrayList<>();
			try {
				while (fetchProductsResultSet.next()) {
					String productID = fetchProductsResultSet.getString("productID");

					String productName = fetchProductsResultSet.getString("productName");

					String costPerUnit = fetchProductsResultSet.getString("costPerUnit");

					String category = fetchProductsResultSet.getString("category");

					String subCategory = fetchProductsResultSet.getString("subCategory");

					Product tempProduct = new Product(productID, productName, costPerUnit, category, subCategory);

					System.out.println(tempProduct.toString());

					arrayOfProducts.add(tempProduct);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return arrayOfProducts;
		}

	}

	protected static final class DatabaseActionUpdateForUpdateOrders implements IDatabaseAction {
		private String ORDERS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "orders";

		@Override
		public Object getDatabaseAction(Object[] params) {
			if (!(params[0] instanceof Object[]))
				throw new InvalidParameterException("Error: expected array of Objects.");
			Object[] orders = (Object[]) params[0];
			for (Object o : orders) {
				if (!(o instanceof Order))
					throw new InvalidParameterException("Error: expected array of Objects that includes Order.");
				Order order = (Order) o;

				StringBuilder sqlBuilder = new StringBuilder("UPDATE ");
				sqlBuilder.append(ORDERS_TABLE);
				sqlBuilder.append(" SET total_price = ");
				sqlBuilder.append(order.getTotalPrice());
				sqlBuilder.append(", total_quantity = ");
				sqlBuilder.append(order.getTotalAmount());
				sqlBuilder.append(", deliveryTime = '");
				sqlBuilder.append(Timestamp.valueOf(order.getDeliveryTime()));
				sqlBuilder.append("', typeId = ");
				sqlBuilder.append(order.getType().getTypeId());
				sqlBuilder.append(", statusId = ");
				sqlBuilder.append(order.getStatus().getStatusId());
				sqlBuilder.append(" WHERE orderID = ");
				sqlBuilder.append(order.getOrderID());
				sqlBuilder.append(";");
				/*
				 * String sqlQuery = "UPDATE " + ORDERS_TABLE + " SET " +
				 * "typeId = (select statusId FROM " + ORDER_TYPES_TABLE +
				 * "  where typeName = \"" + order.getType().name() + "\"), " +
				 * "statusId = (select statusId FROM " + ORDER_STATUSES_TABLE +
				 * "  where statusName = \"" + order.getStatus().name() + "\"), " +
				 * "deliveryTime = '" + Timestamp.valueOf(order.getDeliveryTime()).toString() +
				 * "' " + "WHERE orderId = \"" + order.getOrderID() + "\";";
				 */
				boolean success = DatabaseSimpleOperation.executeQuery(sqlBuilder.toString());
				if (!success)
					return success;
			}
			return true;
		}
	}

	// Class which is used to return a result set of all online orders with the
	// status name in orderFilters[0]
	/**
	 * The class DatabaseActionUpdateForUpdateOrders implements the IDatabaseAction
	 * interface and is used to update data from an orders table. It takes in one
	 * parameter: an array of Order objects. The table name is hardcoded as "orders"
	 * and is a part of a schema "schema_ekrut". The method updates total_price,
	 * total_quantity, deliveryTime, typeId, statusId fields of the orders table for
	 * all orders passed as the parameter. The query is then executed using the
	 * DatabaseSimpleOperation.executeQuery method. The method returns true if all
	 * the queries are executed successfully, otherwise it returns false.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionSelectForFetchOrders implements IDatabaseAction {
		private String ORDERS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "orders";
		private String MACHINES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "machine";

		@Override
		public Object getDatabaseAction(Object[] orderFilters) {
			StringBuilder sqlBuilder = new StringBuilder("select * from ");
			sqlBuilder.append(ORDERS_TABLE);
			sqlBuilder.append(" JOIN ");
			sqlBuilder.append(MACHINES_TABLE);
			sqlBuilder.append(" using(machineId)");
			if (orderFilters[0] instanceof Object[] && ((Object[]) orderFilters[0]).length > 0) {
				Status[] statuses = (Status[]) ((Object[]) orderFilters[0])[0];
				Type[] types = (Type[]) ((Object[]) orderFilters[0])[1];
				// append status filters
				sqlBuilder.append(" WHERE statusId in(");
				for (Status s : statuses) {
					sqlBuilder.append(s.getStatusId());
					sqlBuilder.append(",");
				}
				// delete last comma
				sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
				// append type filters
				sqlBuilder.append(") AND typeId in(");
				for (Type t : types) {
					sqlBuilder.append(t.getTypeId());
					sqlBuilder.append(",");
				}
				// delete last comma
				sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
				sqlBuilder.append(")");
			}
			sqlBuilder.append(";");

			// Uses simpler version of execute query with one input string variable (the
			// requested sql query)
			ResultSet fetchOrdersResultSet = DatabaseSimpleOperation.executeQueryWithResults(sqlBuilder.toString(),
					null);

			ArrayList<Order> orders = new ArrayList<>();
			try {
				while (fetchOrdersResultSet.next()) {
					Integer orderID = fetchOrdersResultSet.getInt("orderID");
					Integer totalPrice = fetchOrdersResultSet.getInt("total_price");
					Integer totalAmount = fetchOrdersResultSet.getInt("total_quantity");

					int machineId = fetchOrdersResultSet.getInt("machineID");
					String machineName = fetchOrdersResultSet.getString("machineName");
					Location location = Location.fromLocationId(fetchOrdersResultSet.getInt("locationId"));
					new Machine(machineId, machineName, location, 0);

					Type type = Type.fromTypeId(fetchOrdersResultSet.getInt("typeId"));
					Status status = Status.fromStatusId(fetchOrdersResultSet.getInt("statusId"));
					LocalDate dateReceived = fetchOrdersResultSet.getDate("date_received").toLocalDate();
					Timestamp deliveryTimeStamp = fetchOrdersResultSet.getTimestamp("deliveryTime");
					LocalDateTime deliveryTime = deliveryTimeStamp != null ? deliveryTimeStamp.toLocalDateTime() : null;

					orders.add(new Order(orderID, totalPrice, totalAmount, null, dateReceived, deliveryTime, status,
							type));
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return orders;
		}

	}

	/**
	 * The class DatabaseActionSelectPromotionNames implements the IDatabaseAction
	 * interface and is used to select promotion names from the database. It takes
	 * in one parameter: a string containing the SQL query to select promotion
	 * names. The method executes the query using the
	 * DatabaseSimpleOperation.executeQueryWithResults method. The method returns a
	 * ResultSet containing the result of the query.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionSelectPromotionNames implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			String promotionNam = (String) params[0];
			ResultSet fetchPromotionNames = DatabaseSimpleOperation.executeQueryWithResults(promotionNam, null);
			return fetchPromotionNames;
		}
	}

	// Object[] params contains just the sqlQuery at the [0] index
	/**
	 * This class implements the {@link IDatabaseAction} interface and is used to
	 * select a promotion from the database. The class takes in a single parameter
	 * of type {@link Object} array, where the first element is expected to be a
	 * {@link String} containing the promotion name. The method
	 * {@link #getDatabaseAction(Object[])} returns an {@link ArrayList} of
	 * {@link Promotions} objects containing all the information of the selected
	 * promotion.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionSelectPromotion implements IDatabaseAction {

		@Override
		public Object getDatabaseAction(Object[] params) {
			String promotionNam = (String) params[0];
			// String sqlQuery = "SELECT * FROM promotions WHERE promotionName = '\" +
			// promotionNam + \"';";
			ResultSet fetchPromotionNames = DatabaseSimpleOperation.executeQueryWithResults(promotionNam, null);
			ArrayList<Promotions> arrayOfPromotions = new ArrayList<>();
			try {
				while (fetchPromotionNames.next()) {
					String promotionID = fetchPromotionNames.getString("promotionId");
					String promotionName = fetchPromotionNames.getString("promotionName");
					String promotionDescription = fetchPromotionNames.getString("promotionDescription");
					int locationId = Integer.parseInt(fetchPromotionNames.getString("locationId"));
					String productID = fetchPromotionNames.getString("productID");
					String discountPercentage = fetchPromotionNames.getString("discountPercentage");
					Date startDate = fetchPromotionNames.getDate("startDate");
					Date endDate = fetchPromotionNames.getDate("endDate");
					boolean promotionStatus = fetchPromotionNames.getBoolean("promotionStatus");

					Promotions tempPromtions = new Promotions(promotionID, promotionName, promotionDescription,
							locationId, productID, discountPercentage, startDate, endDate, promotionStatus);

					System.out.println(tempPromtions.toString());
					arrayOfPromotions.add(tempPromtions);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return arrayOfPromotions;
		}
	}

	/**
	 * The {@code DatabaseActionUpdatePromotionStatus} class is a concrete
	 * implementation of the {@link IDatabaseAction} interface that updates the
	 * status of an existing promotion in the database. This class takes a single
	 * parameter, a {@code String} representing an SQL query that updates the status
	 * of a promotion, and returns a {@code boolean} indicating the success of the
	 * update operation.
	 * 
	 * @author Rotem
	 *
	 */
	protected static final class DatabaseActionUpdatePromotionStatus implements IDatabaseAction {
		@Override
		public Object getDatabaseAction(Object[] params) {
			String sqlQuery = (String) params[0];
			return DatabaseSimpleOperation.executeQuery(sqlQuery);
		}
	}

	/**
	 * This method takes an array of Location objects as a parameter and returns an
	 * ArrayList of Machine objects. The returned machines are filtered based on
	 * their locationId. If the location array passed is empty, all machines are
	 * returned.
	 * 
	 * @param machineLocations an array of Location objects
	 * @return ArrayList of Machine objects
	 * @author Daniel
	 *
	 */
	protected static final class DatabaseActionSelectForFetchMachines implements IDatabaseAction {
		private static final String MACHINES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + ".machine";

		@Override
		public Object getDatabaseAction(Object[] machineLocations) {
			ArrayList<Machine> machines = new ArrayList<>();

			// init locationsArr to empty array
			Location[] locationsArr = new Location[] {};
			// if location array passed, set it to locationsArr.
			if (machineLocations[0] instanceof Location[]) {
				locationsArr = (Location[]) machineLocations[0];
			}
			StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
			sqlBuilder.append(MACHINES_TABLE);

			if (locationsArr.length > 0) {
				sqlBuilder.append(" WHERE locationId in(");
				for (Location location : locationsArr) {
					if (location instanceof Location) {
						sqlBuilder.append(location.getLocationId());
						sqlBuilder.append(",");
					}
				}
				// delete last comma
				sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
				sqlBuilder.append(")");
			}
			sqlBuilder.append(";");

			ResultSet fetchMachinesResultSet = DatabaseSimpleOperation.executeQueryWithResults(sqlBuilder.toString(),
					null);
			try {
				while (fetchMachinesResultSet.next()) {
					int machineId = fetchMachinesResultSet.getInt("machineId");
					int locationId = fetchMachinesResultSet.getInt("locationId");
					String machineName = fetchMachinesResultSet.getString("machineName");
					machines.add(new Machine(machineId, machineName, Location.fromLocationId(locationId), 0));
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return machines;
		}

	}

	/**
	 * Database action class that handles the fetching of products in a machine.
	 * Implements the IDatabaseAction interface. The class defines the table and
	 * schema names for the products in machine table and product table. The
	 * getDatabaseAction method retrieves the products in machine by joining the two
	 * tables using the productID and filtering by machineID and restock_flag. It
	 * returns an ArrayList of ProductInMachine objects.
	 *
	 * @author Daniel
	 *
	 */
	protected static final class DatabaseActionSelectForFetchProductsInMachine implements IDatabaseAction {
		private static final String PRODUCTS_IN_MACHINE_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT
				+ ".products_in_machine";
		private static final String PRODUCTS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "product";

		@Override
		public Object getDatabaseAction(Object[] params) {
			ArrayList<ProductInMachine> products = new ArrayList<>();
			if (!(params[0] instanceof Machine)) {
				throw new InvalidParameterException("FetchProductsInMachine: Expected parameter of type 'Machine'.");
			}
			Machine machine = (Machine) params[0];
			StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
			sqlBuilder.append(PRODUCTS_IN_MACHINE_TABLE);
			sqlBuilder.append(" JOIN ");
			sqlBuilder.append(PRODUCTS_TABLE);
			sqlBuilder.append(" USING(productID) WHERE machineID = ");
			sqlBuilder.append(machine.getMachineId());
			sqlBuilder.append(" AND restock_flag = 1");
			sqlBuilder.append(";");

			ResultSet fetchProductsInMachineResultSet = DatabaseSimpleOperation
					.executeQueryWithResults(sqlBuilder.toString(), null);
			try {
				while (fetchProductsInMachineResultSet.next()) {
					String productID = fetchProductsInMachineResultSet.getString("productID");
					String productName = fetchProductsInMachineResultSet.getString("productName");
					String costPerUnit = fetchProductsInMachineResultSet.getString("costPerUnit");
					int stock = fetchProductsInMachineResultSet.getInt("stock");
					int minStock = fetchProductsInMachineResultSet.getInt("min_stock");
					int maxStock = fetchProductsInMachineResultSet.getInt("max_stock");
					products.add(new ProductInMachine(new Product(productID, productName, costPerUnit, "", ""), machine,
							stock, minStock, maxStock, false));
					// System.out.println("product in machine added:"+products.get(products.size() -
					// 1));
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return products;

		}

	}

	/**
	 * Database action class that handles the updating of products in a machine.
	 * Implements the IDatabaseAction interface. The class defines the table and
	 * schema names for the products in machine table. The getDatabaseAction method
	 * updates the products in machine by filtering machineID and productID and
	 * update the stock and restock_flag. It takes an array of ProductInMachine
	 * objects as a parameter, constructs and executes an update query for each
	 * object, and returns a boolean indicating whether the update was successful or
	 * not.
	 * 
	 * @author Daniel
	 *
	 */
	protected static final class DatabaseActionUpdateForUpdateProductsInMachine implements IDatabaseAction {
		private static final String PRODUCTS_IN_MACHINE_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT
				+ ".products_in_machine";

		@Override
		public Object getDatabaseAction(Object[] params) {
			Object[] products = (Object[]) params[0];
			StringBuilder sqlBuilder = new StringBuilder();
			for (Object o : products) {
				if (!(o instanceof ProductInMachine))
					throw new InvalidParameterException(
							"UpdateProductsInMachine: expected ProductInMachine objects in params[0]");
				ProductInMachine product = (ProductInMachine) o;
				// queries to update the products in machine
				sqlBuilder = new StringBuilder("UPDATE ");
				sqlBuilder.append(PRODUCTS_IN_MACHINE_TABLE);
				sqlBuilder.append(" SET stock = ");
				sqlBuilder.append(product.getStock());
				sqlBuilder.append(", restock_flag = ");
				sqlBuilder.append(product.isRestockFlag() ? 1 : 0);
				sqlBuilder.append(" WHERE machineID = ");
				sqlBuilder.append(product.getMachine().getMachineId());
				sqlBuilder.append(" and productID = \"");
				sqlBuilder.append(product.getProduct().getProductID());
				sqlBuilder.append("\";");
				boolean success = DatabaseSimpleOperation.executeQuery(sqlBuilder.toString());
				if (!success)
					return success;
			}
			return true;
		}

	}

	/**
	 * HashMap that maps a DatabaseOperation enum to an IDatabaseAction
	 * implementation. The map is initialized with key-value pairs that associate
	 * each DatabaseOperation with the corresponding IDatabaseAction implementation.
	 * This allows for easy lookup and execution of the appropriate action for a
	 * given operation. The class is defined as private and static so that it can
	 * only be accessed within the same class and only one instance of the map is
	 * created The serialVersionUID is added for serialization.
	 */
	private static HashMap<DatabaseOperation, IDatabaseAction> map = new HashMap<DatabaseOperation, IDatabaseAction>() {

		private static final long serialVersionUID = 1L;

		{
			this.put(DatabaseOperation.INSERT, new DatabaseActionInsert());
			this.put(DatabaseOperation.USER_LOGIN, new DatabaseActionSelectForLogin());
			this.put(DatabaseOperation.SELECT, new DatabaseActionSelect());
			this.put(DatabaseOperation.UPDATE, new DatabaseActionGenericUpdate());
			this.put(DatabaseOperation.GENERIC_SELECT, new DatabaseActionGenericSelect());
			this.put(DatabaseOperation.DELETE, new DatabaseActionDelete());

			this.put(DatabaseOperation.FETCH_PRODUCTS_BY_CATEGORY, new DatabaseActionSelectForFetchProducts());
			this.put(DatabaseOperation.FETCH_ORDERS, new DatabaseActionSelectForFetchOrders());
			this.put(DatabaseOperation.FETCH_MACHINES_BY_LOCATION, new DatabaseActionSelectForFetchMachines());
			this.put(DatabaseOperation.FETCH_PRODUCTS_IN_MACHINE, new DatabaseActionSelectForFetchProductsInMachine());

			this.put(DatabaseOperation.SELECT_PROMOTION, new DatabaseActionSelectPromotion());

			this.put(DatabaseOperation.UPDATE_ORDERS, new DatabaseActionUpdateForUpdateOrders());
			this.put(DatabaseOperation.UPDATE_PROMOTION_STATUS, new DatabaseActionUpdatePromotionStatus());
			this.put(DatabaseOperation.UPDATE_PRODUCTS_IN_MACHINE,
					new DatabaseActionUpdateForUpdateProductsInMachine());

			this.put(DatabaseOperation.INSERT_PROMOTION_NAMES, new DatabaseActionSelectPromotionNames());

			this.put(DatabaseOperation.IMPORT_SIMULATION, new DatabaseActionImportSimulation());

		}
	};

	/**
	 * Returns the HashMap instance that maps a DatabaseOperation enum to an
	 * IDatabaseAction implementation.
	 * 
	 * @return HashMap<DatabaseOperation, IDatabaseAction> the HashMap that contains
	 *         the mapping of operations to actions.
	 */
	public static HashMap<DatabaseOperation, IDatabaseAction> getMap() {
		return map;
	}

}
