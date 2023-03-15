package controllers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import common.SCCP;
import common.ServerClientRequestTypes;
import controllers.EktSystemUserLoginController.LoginHelper;

import logic.Role;
import logic.SystemUser;

public class EktSystemUserLoginControllerTests {

	private EktSystemUserLoginController loginController;
	private LoginHelper lh;
//	private SystemUser systemUser;
//	private Method getBtnLoginMethod;
	private String userC1;
	private String passC1;
	private SCCP stub1;
	private String path1;
	
	@Before
    public void setUp() throws Exception {
		loginController = new EktSystemUserLoginController(); 
		lh = loginController.new LoginHelper(false);
    }
	
	// test 1
	//check functionality: Testing the login functionality for an existing customer
	//input data: String username, String password
	//expected result: The role is Customer and the next page is "catalog"
	@Test
	public void login_customerExists()  {

		// input - user name and password
		userC1 = "c1";
		passC1 = "123";
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				new SystemUser(0, null, null, null, null, null, "c1", "123", Role.CUSTOMER));
		path1 = "/gui/EktCatalogForm.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(userC1, passC1);

		// verify role
		assertEquals(Role.CUSTOMER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 2
	//check functionality: Testing the login functionality for an non existing customer
	//input data: String username, Stirng password
	//expected result: null page, label will be equal "Invalid input for login"
	@Test
	public void login_customerDoesNotExist()  {
		// input - user name and password
		userC1 = "c0";
		passC1 = "123";
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT, 
				"Invalid input for login");
		String lbl1 = "Invalid input for login";
		//lh = new EktSystemUserLoginController().new LoginHelper(false);
		lh.setResponseFromServer(stub1);

		lh.login(userC1, passC1);


		// verify next page (in case of failed login, we stay in place)
		assertEquals(null, lh.nextPage);
		// verify label
		assertEquals(lbl1, lh.statusLabelString);

	}
	  
	
	// test 3
	//check functionality: Testing the login functionality for an already loggedin customer
	//input data: String username, Stirng password
	//expected result: null page, label will be equal "Sorry, user is already logged in"
	@Test
	public void login_customerAlreadyLoggedIn()  {
		// input - user name and password
		userC1 = "c1";
		passC1 = "123";
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN, 
				"Sorry, user is already logged in");
		String lbl1 = "Sorry, user is already logged in";
		//lh = new EktSystemUserLoginController().new LoginHelper(false);
		lh.setResponseFromServer(stub1);
		lh.login(userC1, passC1);

		// verify next page (in case of failed login, we stay in place)
		assertEquals(null, lh.nextPage);
		// verify label
		assertEquals(lbl1, lh.statusLabelString);
	}
	
	// test 4
	//check functionality: Testing the login functionality for an existing subscriber
	//input data: String username, String password
	//expected result: The role is SUBSCRIBER and the next page is "catalog"
	@Test
	public void login_subscriber()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"shir", "123", Role.SUBSCRIBER);
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/EktCatalogForm.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.SUBSCRIBER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 5
	//check functionality: Testing the login functionality for an existing regional manager
	//input data: String username, String password
	//expected result: The role is REGIONAL_MANAGER and the next page is "Regional Manager Home Page"
	@Test
	public void login_regionalManager()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"kobi", "123", Role.REGIONAL_MANAGER);
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/EktRegionalManagerHomePage.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.REGIONAL_MANAGER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 6
	//check functionality: Testing the login functionality for an existing sales manager
	//input data: String username, String password
	//expected result: The role is SALES_MANAGER and the next page is "Sales Manager"
	@Test
	public void login_salesManager()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"anna", "123", Role.SALES_MANAGER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/SalesManager.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.SALES_MANAGER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 7
	//check functionality: Testing the login functionality for an existing service representative
	//input data: String username, String password
	//expected result: The role is SERVICE_REPRESENTATIVE and the next page is "Service Representative Home Page"
	@Test
	public void login_serviceRepresentative()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"anna", "123", Role.SERVICE_REPRESENTATIVE);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/EktServiceRepresentativeHomePage.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.SERVICE_REPRESENTATIVE, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 8
	//check functionality: Testing the login functionality for an existing inventory worker
	//input data: String username, String password
	//expected result: The role is INVENTORY_WORKER and the next page is "Inventory Restock Worker Page"
	@Test
	public void login_inventoryWorker()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"shimi", "123", Role.INVENTORY_WORKER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/InventoryRestockWorkerPage.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.INVENTORY_WORKER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 9
	//check functionality: Testing the login functionality for an existing delivery worker
	//input data: String username, String password
	//expected result: The role is DELIVERY_WORKER and the next page is "Delivery Manager Page"
	@Test
	public void login_deliveryWorker()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"aziz", "123", Role.DELIVERY_WORKER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/DeliveryManagerPage.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.DELIVERY_WORKER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 10
	//check functionality: Testing the login functionality for an existing division manager
	//input data: String username, String password
	//expected result: The role is DIVISION_MANAGER and the next page is "Division Manager Home Page"
	@Test
	public void login_divisionManager()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"ella", "123", Role.DIVISION_MANAGER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/EktDivisionManagerHomePage.fxml";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.DIVISION_MANAGER, lh.role);
		// verify next page
		assertEquals(path1, lh.nextPage);
	}
	
	// test 11
	//check functionality: Testing the login functionality for an existing unapproved customer
	//input data: String username, String password
	//expected result: The role is UNAPPROVED_CUSTOMER, label will be equal "Customer not yet approved!"
	@Test
	public void login_unapprovedCustomer()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"karenj", "123", Role.UNAPPROVED_CUSTOMER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		String lbl = "Customer not yet approved!";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.UNAPPROVED_CUSTOMER, lh.role);
		// verify label
		assertEquals(lbl, lh.statusLabelString);
	}
	
	// test 12
	//check functionality: Testing the login functionality for an existing unapproved subscriber
	//input data: String username, String password
	//expected result: The role is UNAPPROVED_SUBSCRIBER, label will be equal "Subscriber not yet approved!"
	@Test
	public void login_unapprovedSubscriber()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"jamesw", "123", Role.UNAPPROVED_SUBSCRIBER);

		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		String lbl = "Subscriber not yet approved!";
		lh.setResponseFromServer(stub1);
		
		lh.login(toLogIn.getUsername(), toLogIn.getPassword());

		// verify role
		assertEquals(Role.UNAPPROVED_SUBSCRIBER, lh.role);
		// verify label
		assertEquals(lbl, lh.statusLabelString);
	}
	
	// test 13
	//check functionality: Testing the login functionality for an null username
	//input data: null username, String password
	//expected result: catch null pointer exception
	@Test
	public void login_nullUsername()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"c1", "123", Role.CUSTOMER);
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.ACK, 
				toLogIn);
		path1 = "/gui/EktRegionalManagerHomePage.fxml";
		lh.setResponseFromServer(stub1);
		try {
			lh.login(null, toLogIn.getPassword());
			fail();
		}	
		catch(NullPointerException ex) {

		}

	}
	
	// test 14
	//check functionality: Testing the login functionality for an null password
	//input data: String username, null password
	//expected result: catch null pointer exception
	@Test
	public void login_nullPassword()  {

		// input - user name and password
		SystemUser toLogIn = new SystemUser(0, null, null, null, null, null, 
				"c1", "123", Role.CUSTOMER);
		// stub response from server
		stub1 = new SCCP(ServerClientRequestTypes.LOGIN, 
				toLogIn);
		path1 = "/gui/EktRegionalManagerHomePage.fxml";
		lh.setResponseFromServer(stub1);
		try {
			lh.login(toLogIn.getUsername(), null);
			fail();
		}
		catch(NullPointerException ex) {
		}
		
	}


}
