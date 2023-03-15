package Server;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Server.ServerMessageHandler.HandleMessageLogin;
import Server.ServerMessageHandler.HandleMessageLogout;
import common.SCCP;
import common.ServerClientRequestTypes;
import database.DatabaseController;

class ServerMessageHandlerTest {

	private static Class<?> loginClassPointer;
	private static HandleMessageLogin loginClassInstance;
	private static Method methodHandleMessageLogin;
	private static Class<?> logOutClassPointer;
	private static HandleMessageLogout logOutClassInstance;
	private static Method methodHandleMessageLogout;

	@AfterAll
	public static void tearDown() {
		// close the SQL connection
		DatabaseController.closeDBController();
	}
	
	@BeforeAll
	static void setUp() throws NoSuchMethodException, SecurityException, 
	IllegalAccessException, IllegalArgumentException, 
	InvocationTargetException, InstantiationException, ClassNotFoundException{
		DatabaseController.setDatabaseUserName("root");
		// set password
		DatabaseController.setDatabasePassword("Aa123456");
		
		DatabaseController.getConnection();
		
		// get the classes we need
		loginClassPointer = Class.forName("Server.ServerMessageHandler$HandleMessageLogin");
		logOutClassPointer = Class.forName("Server.ServerMessageHandler$HandleMessageLogout");
		
		// create instances
		loginClassInstance = (HandleMessageLogin) 
				loginClassPointer.getConstructor().newInstance();
		logOutClassInstance = (HandleMessageLogout)
				logOutClassPointer.getConstructor().newInstance();
		
		// get the private handleMessage methods implemented in the classes
		// logout is only needed to perform cleanup after each test
	    methodHandleMessageLogin = loginClassPointer.
	    		getDeclaredMethod("handleMessage", SCCP.class);
	    methodHandleMessageLogout = logOutClassPointer.
	    		getDeclaredMethod("handleMessage", SCCP.class);
	    System.out.println("Finished prep");
	    
	}

	//test 1
	//check functionality: Testing the login functionality for an existing customer
	//input data: Object SCCP message with username and password of existing customer c1, 123
	//expected result: server acknowledged the message, user has loggedin
	@Test
	void login_existingCustomer() 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException 
	{
	    System.out.println("began test");

		// call the login method (server side) using an existing customer (systemuser)
		Object loginResult = methodHandleMessageLogin.
				invoke(loginClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGIN, new Object[] {
				"c1",
				"123"
		}));
		
		// assert the method returned an SCCP instance (as it should)
		assertFalse(loginResult == null);
		assertTrue(loginResult instanceof SCCP);
		
		// grab the SCCP result object
		SCCP resAsSCCP = (SCCP)loginResult;
		
		// assert login was successful (database added user to logged users)
		assertEquals(resAsSCCP.getRequestType(), ServerClientRequestTypes.ACK);

		// cleanup - remove the connected user from database
		loginResult = methodHandleMessageLogout.
				invoke(logOutClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGOUT, "c1"));
	}
	//test 2
	//check functionality: Testing the login functionality for an non-existing customer
	//input data: Object SCCP message with username and password of non-existing customer 0000, 123
	//expected result: server request type is LOGIN_FAILED_ILLEGAL_INPUT 
	@Test
	void login_notExistingCustomer() 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException 
	{
	    System.out.println("began test");

		// call the login method (server side) using an existing customer (systemuser)
		Object loginResult = methodHandleMessageLogin.
				invoke(loginClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGIN, new Object[] {
				"0000",
				"123"
		}));
		
		// assert the method returned an SCCP instance (as it should)
		assertFalse(loginResult == null);
		assertTrue(loginResult instanceof SCCP);
		SCCP resAsSCCP = (SCCP)loginResult;
		
		// assert login was successful (database added user to logged users)
		assertEquals(resAsSCCP.getRequestType(), 
				ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT);

	}
	//test 3
	//check functionality: Testing the login functionality for an null customer
	//input data: Object null SCCP  
	//expected result: server request type is ERROR_MESSAGE, and received message is "login message is null"
	@Test
	void login_nullCustomer() 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException 
	{
	    System.out.println("began test");

		// call the login method (server side) using an existing customer (systemuser)
		Object loginResult = methodHandleMessageLogin.
				invoke(loginClassInstance, 
						(SCCP)null);
		
		// assert the method returned an SCCP instance (as it should)
		assertFalse(loginResult == null);
		assertTrue(loginResult instanceof SCCP);
		SCCP resAsSCCP = (SCCP)loginResult;
		
		// assert login was successful (database added user to logged users)
		assertEquals(resAsSCCP.getRequestType(), 
				ServerClientRequestTypes.ERROR_MESSAGE);
		assertEquals(resAsSCCP.getMessageSent(),"login message is null");
		
	}
	//test 4
	//check functionality: Testing the login functionality for an logging in same user twice without logging out
	//input data: Object SCCP message with username and password of existing customer c1, 123  
	//expected result: server request type is LOGIN_FAILED_ALREADY_LOGGED_IN
	@Test
	void login_CustomerAlreadyLoggedIn() 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException 
	{
		
		// prepare
		// log user "c1" in
		
		// call the login method (server side) using an existing customer (systemuser)
		Object loginResult = methodHandleMessageLogin.
				invoke(loginClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGIN, new Object[] {
				"c1",
				"123"
		}));
				assertFalse(loginResult == null);
		assertTrue(loginResult instanceof SCCP);
		SCCP resAsSCCP = (SCCP)loginResult;
		assertEquals(resAsSCCP.getRequestType(), 
				ServerClientRequestTypes.ACK);

		// act
		// call the login method as user "c1" again
		Object loginResult2 = methodHandleMessageLogin.
				invoke(loginClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGIN, new Object[] {
				"c1",
				"123"
		}));
		
		// assert
		assertFalse(loginResult2 == null);
		assertTrue(loginResult2 instanceof SCCP);
		SCCP resAsSCCP2 = (SCCP)loginResult2;
		assertEquals(resAsSCCP2.getRequestType(), 
				ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN);

		// cleanup - remove the connected user from database
		loginResult = methodHandleMessageLogout.
				invoke(logOutClassInstance, 
						new SCCP(ServerClientRequestTypes.LOGOUT, "c1"));
		
	}
}
