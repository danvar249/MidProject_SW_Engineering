package controllers;

import java.io.Serializable;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represents a controller for the Regional Manager Set Threshold page of the system.
 * It allows the Regional Manager to set the threshold for a machine in his region.
 * The class contains methods to initialize the page, go back to the previous page,
 * select a machine from a list of machines in his region,
 * display the current threshold of the selected machine,
 * set a new threshold for the selected machine and update the threshold in the database.
 * @author Dima, Rotem, Maxim
 *
 */
public class EktRegionalManagerSetThresholdController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private Button btnGoBack;

    @FXML
    private ComboBox<String> cmbMachineName;

    @FXML
    private Text txtCurrentStock;

    @FXML
    private Text txtCurrentStockShow;

    @FXML
    private Text txtSetNewThreshold;

    @FXML
    private TextField txtFiledNewThreshold;

    @FXML
    private Button btnSet;
    
    @FXML
    private Label lblError;

    @FXML
    private Label lblUpdated;

    private String MachineName = null;
    private Integer MachineId = null;
    /**
     * This method initializes the Regional Manager Set Threshold page.
     * It sets the visibility of certain GUI components based on whether a machine has been selected,
     * retrieves a list of machines in the Regional Manager's region from the server,
     * populates the machine name combo box with the names of the machines,
     * sets an action listener for the combo box to display the current threshold of the selected machine,
     * and sets the machine id for the selected machine.
     */
    public void initialize() {
    	if(MachineName == null) {
    		txtCurrentStock.setVisible(false);
    		txtCurrentStockShow.setVisible(false);
    		txtSetNewThreshold.setVisible(false);
    		txtFiledNewThreshold.setVisible(false);
    		btnSet.setVisible(false);
    	}
    	
    	Integer managerId = ClientController.getCurrentSystemUser().getId();
    	
		SCCP getMachines = new SCCP();
		getMachines.setRequestType(ServerClientRequestTypes.SELECT);
		getMachines.setMessageSent(new Object[] {"machine INNER JOIN manager_location ON machine.locationId = manager_location.locationId",
				true, "machineName, threshold, machineId", true, "manager_location.idRegionalManager =" + managerId, false, null});
		ClientUI.getClientController().accept(getMachines);
		
		ArrayList<?> arrayOfMachines = new ArrayList<>();
		ArrayList<String> machinesNames = new ArrayList<String>();
		ArrayList<Integer> machinesthreshold = new ArrayList<Integer>();
		ArrayList<Integer> machinesId = new ArrayList<Integer>();

		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			arrayOfMachines = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			for(Object machine : arrayOfMachines) {
				machinesNames.add((String) ((ArrayList<?>)machine).get(0));
				machinesthreshold.add((Integer) ((ArrayList<?>)machine).get(1));
				machinesId.add((Integer) ((ArrayList<?>)machine).get(2));

			}
		}
		
		cmbMachineName.getItems().setAll(machinesNames);
		
		cmbMachineName.setOnAction(event ->{
			MachineName = cmbMachineName.getValue();
			int selectedIndex = cmbMachineName.getSelectionModel().getSelectedIndex();
			System.out.println("selectedIndex = " + selectedIndex);
			txtCurrentStockShow.setText(machinesthreshold.get(selectedIndex).toString());
			MachineId = machinesId.get(selectedIndex);
			txtCurrentStock.setVisible(true);
    		txtCurrentStockShow.setVisible(true);
    		txtSetNewThreshold.setVisible(true);
    		txtFiledNewThreshold.setVisible(true);
    		btnSet.setVisible(true);
		});

		
		if(MachineName != null) {
			
			SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, 
					new Object[]{"machine", true, "machineId", true,
							"machineName = '" + MachineName+ "'", false, null});
			ClientUI.getClientController().accept(msg);
			if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> tmp= (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
				System.out.println(tmp);
				ClientController.setOLCurrentMachineID((Integer.valueOf(tmp.get(0).get(0).toString())));
				System.out.println("Machine ID set to " + MachineId);
			}
		}
    }
    
    /**
     * Handles the action of the "Go Back" button on the Regional Manager Set Threshold page.
     * This method closes the current window and opens the Regional Manager Home page.
     * @param event The ActionEvent object that triggers this method.
     */
    @FXML
    void getBtnGoBack(ActionEvent event) {
    	Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page", false);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
    /**
     * 
     * Handles the action of the "Set" button on the Regional Manager Set Threshold page.
     * This method takes the value entered in the new threshold text field, checks if it's a valid number,
     * sends a request to the server to update the threshold for the selected machine,
     * and displays a message to the user indicating whether the update was successful or not.
     * @param event The ActionEvent object that triggers this method.
     */
    @FXML
    void getBtnSet(ActionEvent event) {
    	
    	
    	if(txtFiledNewThreshold.getText().matches("-?\\d+(\\.\\d+)?")) {
    		SCCP updateThreshold = new SCCP();
        	updateThreshold.setRequestType(ServerClientRequestTypes.UPDATE);
        	updateThreshold.setMessageSent(new Object[] {
        			"products_in_machine JOIN machine ON products_in_machine.machineID = machine.machineId"
        			, "threshold = " + txtFiledNewThreshold.getText() +", products_in_machine.min_stock = " + txtFiledNewThreshold.getText(),"products_in_machine.machineID = " + MachineId});
        	lblError.setVisible(false);
        	lblUpdated.setVisible(true);
   
        	ClientUI.getClientController().accept(updateThreshold);
    		System.out.println("Threshold was updated!");
    	}
    		
    	else {
    		System.out.println("Bad input!");
        	lblUpdated.setVisible(false);
    		lblError.setVisible(true);
    	}

    }

}
