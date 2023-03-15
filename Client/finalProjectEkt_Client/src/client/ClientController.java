package client;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import common.SCCP;
import common.ServerClientRequestTypes;
import entityControllers.OrderController;
import logic.Role;
import logic.SystemUser;

/**
 * 
 * ClientController class is used to handle the client-side operations and
 * communication with the server. It contains fields that store the current
 * user's information, configuration, and other data related to the client's
 * current state.
 * 
 * @author danielvardimon
 */
public class ClientController {
	public static int DEFAULT_PORT;

	// Controller specific fields - TODO: move these to dedicated controllers, and
	// use setters to these controllers here (I will show example )
	public static SCCP responseFromServer = new SCCP();
	private static Configuration launchConfig = null;
	private static SystemUser connectedSystemUser = null; // ClientController.getconnectedSystemUser().getID()
	public EKTClient client;
	private static Role currentUserRole = null;

	private static Boolean customerIsSubsriber = false;

	// dohot related fields

	private static ArrayList<String> machineID_TypeOfReport_Dates = new ArrayList<>();
	private static ArrayList<LocalDate> requestedOrderDates = new ArrayList<>();

	private static String currentUserRegion;

	// machine related fields

	private static String EKCurrentMachineName;
	private static int EKCurrentMachineID;
	private static String OLCurrentMachineName;
	private static int OLCurrentMachineID;

	// one buggish lookin chick
	private static boolean fastRecognitionToggle = false;
	private static String fastRecognitionUserName = null;
	private static String fastRecognitionPassword = null;

	/**
	 * The ClientController class constructor that takes in two parameters, host and
	 * port. It creates a new EKTClient object and connects to the specified host
	 * and port.
	 * 
	 * @param host The hostname or IP address of the server.
	 * @param port The port number to connect to the server.
	 * @throws IOException if the connection to the server cannot be established.
	 */
	public ClientController(String host, int port) throws IOException {

		client = new EKTClient(host, port);

	}

	/**
	 * The accept method takes in an SCCP object as a parameter and sends the SCCP
	 * object to the server via the EKTClient object's handleMessageFromClientUI
	 * method. It also prints its toString representation to the console.
	 * 
	 * @param msgToServer The SCCP object containing the message to be sent to the
	 *                    server.
	 */
	public void accept(SCCP msgToServer) {
		System.out.println(msgToServer.toString());
		client.handleMessageFromClientUI(msgToServer);
	}

	/**
	 * Getter method for the connectedSystemUser field.
	 * 
	 * @return the connectedSystemUser field
	 */
	public static SystemUser getCurrentSystemUser() {
		return connectedSystemUser;
	}

	/**
	 * Setter method for the connectedSystemUser field.
	 * 
	 * @param connectedSystemUser the connectedSystemUser field to set
	 */
	public static void setCurrentSystemUser(SystemUser currentSystemUser) {
		ClientController.connectedSystemUser = currentSystemUser;
	}

	/**
	 * Setter method for the currentUserRole field.
	 * 
	 * @param role the currentUserRole field to set
	 */
	public static void setCurrentUserRole(Role role) {
		currentUserRole = role;
	}

	/**
	 * Getter method for the currentUserRole field.
	 * 
	 * @return the currentUserRole field
	 */
	public static Role getCurrentUserRole() {
		return currentUserRole;
	}

	/**
	 * Getter method for the launchConfig field.
	 * 
	 * @return the launchConfig field
	 */
	public static Configuration getLaunchConfig() {
		return launchConfig;
	}

	/**
	 * Setter method for the launchConfig field.
	 * 
	 * @param launchConfig the launchConfig field to set
	 */
	public static void setLaunchConfig(Configuration launchConfig) {
		ClientController.launchConfig = launchConfig;
	}

	/**
	 * This method sends a logout request to the server and prints a message to the
	 * console.
	 */
	public static void sendLogoutRequest() {
		System.out.println("Logout operation started.");
		if (getCurrentSystemUser() != null) {
			System.out.println(
					"Processing a log-out request from client (user=" + getCurrentSystemUser().getUsername() + ").");
			ClientUI.getClientController()
					.accept(new SCCP(ServerClientRequestTypes.LOGOUT, getCurrentSystemUser().getUsername()));
		}
	}

	/**
	 * Getter method for the machineID_TypeOfReport_Dates field.
	 * 
	 * @return the machineID_TypeOfReport_Dates field
	 */
	public static ArrayList<String> getMachineID_TypeOfReport_Dates() {
		return machineID_TypeOfReport_Dates;
	}

	/**
	 * Setter method for the machineID_TypeOfReport_Dates field.
	 * 
	 * @param machineID_AndReportType the machineID_TypeOfReport_Dates field to set
	 */
	public static void setMachineID_TypeOfReport_Dates(ArrayList<String> machineID_AndReportType) {
		ClientController.machineID_TypeOfReport_Dates = machineID_AndReportType;
	}

	/**
	 * Getter method for the requestedOrderDates field.
	 * 
	 * @return the requestedOrderDates field
	 */
	public static ArrayList<LocalDate> getRequestedOrderDates() {
		return requestedOrderDates;
	}

	/**
	 * Setter method for the requestedOrderDates field.
	 * 
	 * @param requestedOrderDates the requestedOrderDates field to set
	 */
	public static void setRequestedOrderDates(ArrayList<LocalDate> requestedOrderDates) {
		ClientController.requestedOrderDates = requestedOrderDates;
	}

	/**
	 * Getter method for the currentUserRegion field.
	 * 
	 * @return the currentUserRegion field
	 */
	public static String getCurrentUserRegion() {
		return currentUserRegion;
	}

	/**
	 * Setter method for the currentUserRegion field.
	 * 
	 * @param currentUserRegion the currentUserRegion field to set
	 */
	public static void setCurrentUserRegion(String currentUserRegion) {
		ClientController.currentUserRegion = currentUserRegion;
	}

	/**
	 * Getter method for the customerIsSubsriber field.
	 * 
	 * @return the customerIsSubsriber field
	 */
	public static Boolean getCustomerIsSubsriber() {
		return customerIsSubsriber;
	}

	/**
	 * Setter method for the customerIsSubsriber field.
	 * 
	 * @param customerIsSubsriber the customerIsSubsriber field to set
	 */
	public static void setCustomerIsSubsriber(Boolean customerIsSubsriber) {
		ClientController.customerIsSubsriber = customerIsSubsriber;
	}

	/**
	 * This method is used by the login manager to reset user-specific variables
	 */
	public static void resetVars() {
		OrderController.resetVars();
		connectedSystemUser = null;

		currentUserRole = null;

		machineID_TypeOfReport_Dates = new ArrayList<>();
		requestedOrderDates = new ArrayList<>();

		setOLCurrentMachineName(null);
		setOLCurrentMachineID(0);

		customerIsSubsriber = null;

	}

	/**
	 * 
	 * Getter method for the fastRecognitionToggle field.
	 * 
	 * @return the fastRecognitionToggle field
	 */
	public static boolean isFastRecognitionToggle() {
		return fastRecognitionToggle;
	}

	/**
	 * 
	 * Setter method for the fastRecognitionToggle field.
	 * 
	 * @param fastRecognitionToggle the fastRecognitionToggle field to set
	 */
	public static void setFastRecognitionToggle(boolean fastRecognitionToggle) {
		ClientController.fastRecognitionToggle = fastRecognitionToggle;
	}

	/**
	 * 
	 * Getter method for the fastRecognitionUserName field.
	 * 
	 * @return the fastRecognitionUserName field
	 */
	public static String getFastRecognitionUserName() {
		return fastRecognitionUserName;
	}

	/**
	 * 
	 * Setter method for the fastRecognitionUserName field.
	 * 
	 * @param fastRecognitionUserName the fastRecognitionUserName field to set
	 */
	public static void setFastRecognitionUserName(String fastRecognitionUserName) {
		ClientController.fastRecognitionUserName = fastRecognitionUserName;
	}

	/**
	 * 
	 * Getter method for the fastRecognitionPassword field.
	 * 
	 * @return the fastRecognitionPassword field
	 */
	public static String getFastRecognitionPassword() {
		return fastRecognitionPassword;
	}

	/**
	 * 
	 * Setter method for the fastRecognitionPassword field.
	 * 
	 * @param fastRecognitionPassword the fastRecognitionPassword field to set
	 */
	public static void setFastRecognitionPassword(String fastRecognitionPassword) {
		ClientController.fastRecognitionPassword = fastRecognitionPassword;
	}

	/**
	 * 
	 * Getter method for the EKCurrentMachineName field.
	 * 
	 * @return the EKCurrentMachineName field
	 */
	public static String getEKCurrentMachineName() {
		return EKCurrentMachineName;
	}

	/**
	 * 
	 * Setter method for the EKCurrentMachineName field.
	 * 
	 * @param eKCurrentMachineName the EKCurrentMachineName field to set
	 */
	public static void setEKCurrentMachineName(String eKCurrentMachineName) {
		EKCurrentMachineName = eKCurrentMachineName;
	}

	/**
	 * 
	 * Getter method for the EKCurrentMachineID field.
	 * 
	 * @return the EKCurrentMachineID field
	 */
	public static int getEKCurrentMachineID() {
		return EKCurrentMachineID;
	}

	/**
	 * 
	 * Setter method for the EKCurrentMachineID field.
	 * 
	 * @param eKCurrentMachineID the EKCurrentMachineID field to set
	 */
	public static void setEKCurrentMachineID(int eKCurrentMachineID) {
		EKCurrentMachineID = eKCurrentMachineID;
	}

	/**
	 * 
	 * Getter method for the OLCurrentMachineName field.
	 * 
	 * @return the OLCurrentMachineName field
	 */
	public static String getOLCurrentMachineName() {
		return OLCurrentMachineName;
	}

	/**
	 * 
	 * Setter method for the OLCurrentMachineName field.
	 * 
	 * @param oLCurrentMachineName the OLCurrentMachineName field to set
	 */
	public static void setOLCurrentMachineName(String oLCurrentMachineName) {
		OLCurrentMachineName = oLCurrentMachineName;
	}

	/**
	 * 
	 * Getter method for the OLCurrentMachineID field.
	 * 
	 * @return the OLCurrentMachineID field
	 */
	public static int getOLCurrentMachineID() {
		return OLCurrentMachineID;
	}

	/**
	 * 
	 * Setter method for the OLCurrentMachineID field.
	 * 
	 * @param oLCurrentMachineID the OLCurrentMachineID field to set
	 */
	public static void setOLCurrentMachineID(int oLCurrentMachineID) {
		OLCurrentMachineID = oLCurrentMachineID;
	}
}
