package controllers;

import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;

import entityControllers.OrderController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
/**

The EktCatalogFormController class is responsible for handling the events and actions for the EktCatalogForm.fxml file.
This class is responsible for displaying the different catalogs available to the user,
 allowing them to navigate to the EktProductForm,
logging out, viewing their orders, and selecting a machine from the ComboBox.
@author Dima,Maxsim
*/
public class EktCatalogFormController implements Serializable {
	
	/**
	 * Advanced TODO after we're done: add support for variable categories.
	 * Implementation idea: Remove the buttons from fxml, leave just a pane with an
	 * HBox or something, and in initialize() do: query all categories from a
	 * dedicated table, run in a loop: i = 1 for category in categories:
	 * insertToHBoxInPane(category) if (i++) % 5 == 0 createNewHBox()
	 * moveToNextRow()
	 * 
	 * Something like that.
	 */
	private static final long serialVersionUID = 1L;

	@FXML
	private Button btnCatalog1;

	@FXML
	private Button btnCatalog2;

	@FXML
	private Button btnCatalog3;

	@FXML
	private Button btnCatalog4;

	@FXML
	private Button btnCatalog5;

	@FXML
	private Button btnCatalog6;

	@FXML
	private Button btnCatalog7;

	@FXML
	private Button btnCatalog8;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnMyOrders;

	@FXML
	private ImageView imgClatalog0_0;

	@FXML
	private ImageView imgClatalog0_1;

	@FXML
	private ImageView imgClatalog0_2;

	@FXML
	private ImageView imgClatalog0_3;

	@FXML
	private ImageView imgClatalog1_0;

	@FXML
	private ImageView imgClatalog1_1;

	@FXML
	private ImageView imgClatalog1_2;

	@FXML
	private ImageView imgClatalog1_3;

	@FXML
	private Text txtWelcomeCustomer;
	
	@FXML
	private ComboBox<String> cmbMachineName;
	
	@FXML
	private Text txtDiscountFirstOrder;

	
	String productFormFXMLLocation = "/gui/EktProductForm.fxml";

	/**
	 * A method to initialize the EktCatalogFormController class. It sets the welcome message for the user, the images for each catalog,
	 *  and the machine selection combo box. It also checks if the user's first order and displays a message if it is.
*/
	
	@FXML
	public void initialize() {
		if(EktSystemUserLoginController.firstOrderForSubscriber()) {
			txtDiscountFirstOrder.setVisible(true);
		}
		if(ClientController.getOLCurrentMachineName() == null)
			setDisableCatalog(true);
		if(ClientController.getOLCurrentMachineName() != null)
			cmbMachineName.setValue(ClientController.getOLCurrentMachineName());
		
		txtWelcomeCustomer
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
		txtWelcomeCustomer.setLayoutX(400 - (txtWelcomeCustomer.minWidth(0)) / 2);
		
		SCCP getMachines = new SCCP();
		getMachines.setRequestType(ServerClientRequestTypes.SELECT);
		getMachines.setMessageSent(new Object[] {"machine", true, "machineName, machineId", false, null, false, null});
		ClientUI.getClientController().accept(getMachines);
		
		ArrayList<?> arrayOfMachines = new ArrayList<>();
		ArrayList<String> machinesNames = new ArrayList<String>();
		ArrayList<Integer> machinesIds = new ArrayList<Integer>();
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			arrayOfMachines = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			for(Object machine : arrayOfMachines) {
				machinesNames.add((String) ((ArrayList<?>)machine).get(0));
				machinesIds.add((Integer) ((ArrayList<?>)machine).get(1));
			}
		}

		System.out.println("arrayOfMachines = " + arrayOfMachines);
		cmbMachineName.getItems().setAll(machinesNames);
		
		cmbMachineName.setOnAction(event ->{
			if(!cmbMachineName.getValue().equals(ClientController.getOLCurrentMachineName())) {
				if(ClientController.getOLCurrentMachineName() != null) {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("WARNING");
					alert.setContentText("Changing a machine will clear your cart! Are you sure?");

					alert.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.OK) {
                        	EktProductFormController.setMachineSwitchedFlag(true);
							ClientController.setOLCurrentMachineName(cmbMachineName.getValue());
							System.out.println("SET MACHINE TO " + ClientController.getOLCurrentMachineName());
                        } else if (type == ButtonType.NO) {
                        } else {
                        }
                });
					
				}
				else {
					// don't inform user first time
					EktProductFormController.setMachineSwitchedFlag(true);
					ClientController.setOLCurrentMachineName(cmbMachineName.getValue());
				}

			}
			
			if(ClientController.getOLCurrentMachineName() != null) {
				// first, drop the flag!
				
				SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, 
						new Object[]{"machine", true, "machineId", true,
								"machineName = '" +ClientController.getOLCurrentMachineName()+ "'", false, null});
				ClientUI.getClientController().accept(msg);
				if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<Object>> tmp= (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
					System.out.println(tmp);
					ClientController.setOLCurrentMachineID((Integer.valueOf(tmp.get(0).get(0).toString())));
					System.out.println("Machine ID set to " + ClientController.getOLCurrentMachineID());
				}
			}
			
			setDisableCatalog(false);
		;});
		
		
		
			
		
	}

	/**
	 * A method that is called when the user clicks on the button representing catalog 1.
	 *  It opens the EktProductForm window and displays the products of the selected catalog.
	 * @param event The event that triggered this method.
	 */
	// Category 1
	@FXML
	private void getBtnCatalog0_0(ActionEvent event) {
		loadCategoryPage(event,"HEALTHY");
	}
	/**
	 * This method is called when the "SOFT DRINKS" button is clicked.
	 * It creates a new window with the products in the "SOFT DRINKS" category.
	 * It also sets the value of the CurrentProductCategory variable in the ClientController class to "SOFT DRINKS"
	 * 
	 * @param event the event that triggers the method. It is usually a button click.
	 */
	@FXML
	private void getBtnCatalog0_1(ActionEvent event) {
		loadCategoryPage(event, "SOFT DRINKS");
	}
	/**
	 * This method is called when the "FRUITS" button is clicked.
	 * It creates a new window with the products in the "FRUITS" category.
	 * It also sets the value of the CurrentProductCategory variable in the ClientController class to "FRUITS"
	 * 
	 * @param event the event that triggers the method. It is usually a button click.
	 */
	@FXML
	private void getBtnCatalog0_2(ActionEvent event) {
		loadCategoryPage(event, "FRUITS");
	}
	/**
	 * This method is called when the "VEGETABLES" button is clicked.
	 * It creates a new window with the products in the "VEGETABLES" category.
	 * It also sets the value of the CurrentProductCategory variable in the ClientController class to "VEGETABLES"
	 * 
	 * @param event the event that triggers the method. It is usually a button click.
	 */
	@FXML
	private void getBtnCatalog0_3(ActionEvent event) {
		loadCategoryPage(event, "VEGETABLES");
	}

	/**
	 * This method is called when the "SNACKS" button is clicked.
	 * It creates a new window with the products in the "SNACKS" category.
	 * It also sets the value of the CurrentProductCategory variable in the ClientController class to "SNACKS"
	 * 
	 * @param event the event that triggers the method. It is usually a button click.
	 */
	@FXML
	private void getBtnCatalog1_0(ActionEvent event) {
		loadCategoryPage(event, "SNACKS");
	}
	/**
	 * This method is called when the "SANDWICHES" button is clicked.
	 * It creates a new window with the products in the "SANDWICHES" category.
	 * It also sets the value of the CurrentProductCategory variable in the ClientController class to "SANDWICHES"
	 * 
	 * @param event the event that triggers the method. It is usually a button click.
	 */
	@FXML
	private void getBtnCatalog1_1(ActionEvent event) {
		loadCategoryPage(event, "SANDWICHES");
	}
	/**
	* Handles the event when the "CHEWING GUM" category button is clicked.
	* It sets the current product category to "CHEWING GUM" and opens a new window for the product form.
	* The primary window is closed after the new window is opened.
	*
	* @param event the action event that triggered the method call
	*/
	@FXML
	private void getBtnCatalog1_2(ActionEvent event) {
		loadCategoryPage(event, "CHEWING GUM");
	}
	/**
	 * Handles the event when the "DAIRY" category button is clicked.
	 * It sets the current product category to "DAIRY" and opens a new window for the product form.
	 * The primary window is closed after the new window is opened.
	 * 
	 * @param event the action event that triggered the method call
	 */
	@FXML
	private void getBtnCatalog1_3(ActionEvent event) {
		loadCategoryPage(event, "DAIRY");
	}

	private void loadCategoryPage(ActionEvent event, String category) {
		Stage primaryStage = new Stage();
		OrderController.getCurrentProductCategory().add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

/**
 * Handles the event when the "Logout" button is clicked.
 * It sends a logout request to the server and opens a new window for the login form.
 * The primary window is closed after the new window is opened.
 * 
 * @param event the action event that triggered the method call
 */
	@FXML
	void getBtnLogout(ActionEvent event) {
		// actually log the user out
		ClientController.sendLogoutRequest();

		// move to new window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	/**
	 * Handles the event when the "My Orders" button is clicked.
	 * It opens a new window for the "Ekt My Orders" form.
	 * The primary window is closed after the new window is opened.
	 * 
	 * @param event the action event that triggered the method call
*/
	@FXML
	void getBtnMyOrders(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null, "Ekt My Orders", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	/**
	 * Handles the event when the "All Items" category button is clicked.
	 * It sets the current product category to "ALL ITEMS" and opens a new window for the product form.
	 * The primary window is closed after the new window is opened.
	 * 
	 * @param event the action event that triggered the method call
	 */
	@FXML
	void getBtnCatalogAllItems(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		String category = "ALL ITEMS";
		OrderController.getCurrentProductCategory().add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	/**
	 * Method to disable all category buttons
	 * 
	 * @param value the boolean value to set the buttons to
	 */
	public void setDisableCatalog(boolean value) {
		btnCatalog1.setDisable(value);btnCatalog2.setDisable(value);
		btnCatalog3.setDisable(value);btnCatalog4.setDisable(value);
		btnCatalog5.setDisable(value);btnCatalog6.setDisable(value);
		btnCatalog7.setDisable(value);btnCatalog8.setDisable(value);
	}
	
}
