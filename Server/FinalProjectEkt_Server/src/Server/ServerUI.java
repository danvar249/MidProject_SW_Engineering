package Server;

import java.time.LocalDate;

import database.DatabaseSimpleOperation;
import gui.ServerPortController;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The ServerUI class is the main class of the EKT project's server-side application.
 * It launches the main GUI window of the server and also handles server's start and shutdown operations.
 * The class main() method is the entry point of the program, it calls the JavaFX's launch method which calls the start method of the class,
 * where the main GUI window is loaded.
 * The class also contains methods for starting the server and handling server shutdowns.
 * @author Rotem, Maxim
 *
 */
public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	private static EktServer serverObject;

	/**
	 * This is the main function for the server side of the EKT project.
	 * main calls the JavaFX function start, where the main server GUI window is loaded.
	 * @param args - these are the command-line arguments passed to the server application on start-up. (currently, 1/4/23 no arguments)
	 */
	public static void main(String args[])
	   {   
		try {
		 launch(args);
		}catch(Exception ex) {
			System.out.println("JavaFX Application ServerUI threw an exception and stopped running. Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	  } 
	
	/**
	 * start method is the main method for the ServerUI class. It creates a new instance of the ServerPortController class,
	 * sets the action for the close button of the primary stage, and calls the start method of the serverGuiWindow object.
	 * @param primaryStage - the main stage of the JavaFX application, where the main window is displayed.
	 * @throws Exception - if an error occurs while loading the GUI window.	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortController serverGuiWindow = new ServerPortController();
		// override the X button, (possible option: to disable it, like this:
		//
		// if we want to only allow the X button to stay:
		// 
		//)
//		primaryStage.initStyle(StageStyle.UNDECORATED);
//		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setOnCloseRequest(we -> {
	    	System.out.println("X button has been clicked!");

		    if(serverObject != null && serverObject.isListening()) {
		    		serverForcedShutdown();
		    }
		}); 
		serverGuiWindow.start(primaryStage);
		(new threadForReports()).start();
	}
	
	
	public class threadForReports extends Thread {

		public void run() {
			System.out.println("Thread for creating monthly report has started");
			while (true) {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (LocalDate.now().getDayOfMonth() == 1) {
					int currentMonth = LocalDate.now().getMonthValue();
					int currentYear = LocalDate.now().getYear();
					int monthBefore = currentMonth, yearBefore = currentYear;

					if (currentMonth == 1) {
						// Set the date for the reports of last month
						monthBefore = 12;
						yearBefore -= 1;
					} else {
						monthBefore -= 1;

					}
					// Create reports
					createInventoryReport(currentMonth, currentYear, monthBefore, yearBefore);
					try {
						// Sleep for a whole day
						Thread.sleep(86400000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public boolean createInventoryReport(int currentMonth, int currentYear, int monthBefore, int yearBefore) {

			String sqlToExecute = "INSERT INTO report ( month_year, machineID, restock_amount) "
					+ "SELECT month_year, machine_id, SUM(restock_amount) " + "FROM restock_in_machine "
					+ "WHERE date_of_restock >= \"" + yearBefore + "-" + monthBefore + "-1\" "
					+ "AND date_of_restock < \"" + currentYear + "-" + currentMonth + "-1\""
					+ " GROUP BY month_year, machine_id;";
			System.out.println("Our sql; " + sqlToExecute);
			return DatabaseSimpleOperation.executeQuery(sqlToExecute, null);
			}
	}

	
	/**
	 * The runServer method is used to start a new instance of the EktServer class, which is responsible for managing client connections and handling client requests.
	 * It takes a single argument, the port number on which the server should listen for connections.
	 * The method first attempts to parse the port number argument as an integer, and assigns it to the variable 'port'.
	 * If the parseInt method throws an exception, the method prints an error message to the console.
	 * Next, a new instance of EktServer is created, passing the 'port' variable as an argument.
	 * The method then calls the listen method on the server object, which begins listening for client connections on the specified port.
	 * If an exception is thrown while calling the listen method, the method prints an error message to the console.
	 * @param port number on which the server will listen
	 */
	public static void runServer(String p)
	{
		 int port = 0; //Port to listen on

	        try
	        {
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect! Message: " + t.getLocalizedMessage());
	        }
	    	
	        serverObject = new EktServer(port);
	        
	        try 
	        {
	          getEktServerObject().listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	        	System.out.println("Exception in calling .listen() on server object: "+ ex);
	        	System.out.println("ERROR - Could not listen for clients!");
	        }
	}
	/**
	 * This method returns the current instance of the EktServer object that is being used by the application.
	 * @return EktServer object that is currently in use by the application.
	 */
	public static EktServer getEktServerObject() {
		return serverObject;
	}
	/**
	 * This method is used to perform a forced shutdown of the server.
	 * It will close all active connections and stop the server from listening for new connections.
	 * This method is typically called when the user closes the server GUI window.
	 */
	public static void serverForcedShutdown() {
		System.out.println("Server starting shutdown process");
		if(getEktServerObject() != null)
			getEktServerObject().handleForcedShutdown();
	}
	

}
