package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import logic.Customer;
import logic.Role;
/*
 * Do not use this
 */
public class RegisterNewCustomerPageController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnRegisterCustomer;

    @FXML
    private CheckBox chkSubscriber;

    @FXML
    private TextField txtCVV;

    @FXML
    private TextField txtCreditCard;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtSurname;

    @FXML
    void getBtnBack(ActionEvent event) {
        // Code to handle going back to previous page
    }

    @FXML
    void getBtnRegisterCustomer(ActionEvent event) {
        // Create new Customer object with provided information
    	int id = 123;
        String name = txtName.getText();
        String surname = txtSurname.getText();
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String creditCard = txtCreditCard.getText();
        txtCVV.getText();
        Customer newCustomer = new Customer(name ,surname, id , phoneNumber, email, creditCard, name+"."+surname, Role.CUSTOMER.toString());


        // Send request to server to register new customer
        SCCP preparedMessage = new SCCP();
        preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
        preparedMessage.setMessageSent(newCustomer);
        ClientUI.getClientController().accept(preparedMessage);

        // Check for response from server
        if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ADD)) {
            // Registration successful
            System.out.println("Successfully registered new customer: " + newCustomer.getFirstName() + " " + newCustomer.getLastName());
        } else {
            // Registration failed
            System.out.println("Error registering new customer");
        }
    }

}
