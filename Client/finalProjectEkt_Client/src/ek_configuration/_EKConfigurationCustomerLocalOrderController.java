package ek_configuration;

import common.WindowStarter;
import entityControllers.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.Serializable;
import client.ClientController;

/**
 * 
 * _EKConfigurationCustomerLocalOrderController is a class that handles the
 * logic for the local order simulation page. It contains various FXML elements
 * and methods that handle the actions of the user when interacting with the
 * page. The class contains methods for loading different product categories and
 * a logout button. It also contains methods for handling the first order of a
 * subscriber.
 * 
 * @author Rotem
 */
public class _EKConfigurationCustomerLocalOrderController implements Serializable {

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
	private Text txtDiscountOnFirstOrder;

	String productFormFXMLLocation = "/gui/_EKConfigurationProductForm.fxml";

	/**
	 * This method is called automatically when the page is loaded. It sets the text
	 * of the welcome customer text field and checks if the customer is making their
	 * first order as a subscriber.
	 */

	@FXML
	public void initialize() {
		txtWelcomeCustomer
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back");
		txtWelcomeCustomer.setLayoutX(400 - (txtWelcomeCustomer.minWidth(0) / 2));
		if (_EKConfigurationLoginFrameController.firstOrderForSubscriber()) {
			txtDiscountOnFirstOrder.setVisible(true);
		}
	}

	/**
	 * This method handles the action of the user clicking on the first category
	 * button. It calls the loadCategoryPage method and passes the string "HEALTHY"
	 * as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog0_0(ActionEvent event) {
		loadCategoryPage(event, "HEALTHY");
	}

	/**
	 * This method handles the action of the user clicking on the second category
	 * button. It calls the loadCategoryPage method and passes the string "SOFT
	 * DRINKS" as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog0_1(ActionEvent event) {
		loadCategoryPage(event, "SOFT DRINKS");
	}

	/**
	 * This method handles the action of the user clicking on the third category
	 * button. It calls the loadCategoryPage method and passes the string "FRUITS"
	 * as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog0_2(ActionEvent event) {
		loadCategoryPage(event, "FRUITS");
	}

	/**
	 * This method handles the action of the user clicking on the fourth category
	 * button. It calls the loadCategoryPage method and passes the string
	 * "VEGETABLES" as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog0_3(ActionEvent event) {
		loadCategoryPage(event, "VEGETABLES");
	}

	/**
	 * This method handles the action of the user clicking on the fifth category
	 * button. It calls the loadCategoryPage method and passes the string "SNACKS"
	 * as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog1_0(ActionEvent event) {
		loadCategoryPage(event, "SNACKS");
	}

	/**
	 * This method handles the action of the user clicking on the sixth category
	 * button. It calls the loadCategoryPage method and passes the string
	 * "SANDWICHES" as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog1_1(ActionEvent event) {
		loadCategoryPage(event, "SANDWICHES");
	}

	/**
	 * This method handles the action of the user clicking on the seventh category
	 * button. It calls the loadCategoryPage method and passes the string "CHEWING
	 * GUM" as the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog1_2(ActionEvent event) {
		loadCategoryPage(event, "CHEWING GUM");
	}

	/**
	 * This method handles the action of the user clicking on the eighth category
	 * button. It calls the loadCategoryPage method and passes the string "DAIRY" as
	 * the category parameter.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	private void getBtnCatalog1_3(ActionEvent event) {
		loadCategoryPage(event, "DAIRY");
	}

	/**
	 * This method takes an ActionEvent and a string representing a category and
	 * loads the products page of the corresponding category.
	 * 
	 * @param event    - the event that triggered the method call.
	 * @param category - the category of products to be displayed.
	 */
	private void loadCategoryPage(ActionEvent event, String category) {
		Stage primaryStage = new Stage();
		OrderController.getCurrentProductCategory().add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method handles the action of the user clicking on the logout button. It
	 * hides the current window and opens the login window.
	 * 
	 * @param event - the event that triggered the method call.
	 */
	@FXML
	void getBtnLogout(ActionEvent event) {
		// actually log the user out
		ClientController.sendLogoutRequest();

		// move to new window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method is the event handler for the button that displays all the items
	 * in the catalog. It hides the primary window, creates a new stage, adds the
	 * current category to the order controller, and loads the product form view.
	 * 
	 * @param event the ActionEvent object that triggers the method
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
	 * 
	 * This method disables all the catalog buttons.
	 * 
	 * @param value boolean value to set the disable state of the buttons
	 */
	public void setDisableCatalog(boolean value) {
		btnCatalog1.setDisable(value);
		btnCatalog2.setDisable(value);
		btnCatalog3.setDisable(value);
		btnCatalog4.setDisable(value);
		btnCatalog5.setDisable(value);
		btnCatalog6.setDisable(value);
		btnCatalog7.setDisable(value);
		btnCatalog8.setDisable(value);
	}

}
