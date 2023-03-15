package controllers;

import java.util.ArrayList;
import java.util.Arrays;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import logic.Product;

public class TestSelectFromDBProductController {
	
    @FXML
    private Text lblStatus;

    @FXML
    private TableView<Product> tblProducts;
	@FXML TableColumn<Product, String> pid;
	@FXML TableColumn<Product, String> pname;
	@FXML TableColumn<Product, String> pprice;
	@FXML TableColumn<Product, String> pcat;
	@FXML TableColumn<Product, String> psubcat;
    
    @FXML
    private void initialize() {
    	// prep

    	pid.setCellValueFactory(new PropertyValueFactory<Product, String>("productID"));
    	pname.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    	pprice.setCellValueFactory(new PropertyValueFactory<Product, String>("costPerUnit"));
    	pcat.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	psubcat.setCellValueFactory(new PropertyValueFactory<Product, String>("subCategory"));

        
    	SCCP output = new SCCP();
    	output.setRequestType(ServerClientRequestTypes.SELECT);
    	// Arguments passed: {tableName, filterColumns, what, filterRows, where, useSpecial, special}
    	output.setMessageSent(new Object[] {"product", false, null, false, "category = \"Healthy\"", false, null});
    	ClientUI.getClientController().accept(output);
    	if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    		SCCP answer = ClientController.responseFromServer;
    		ArrayList<Product> productArrayList = new ArrayList<>();
    		if(answer.getMessageSent() instanceof ArrayList) {
    			@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>)answer.getMessageSent();
    			for(ArrayList<Object> lst : preProcessedOutput) {
    				// we expect product to have 5 columns, and act accordion-ly
    				Object[] arr = lst.toArray();
    				System.out.println(Arrays.toString(arr));
    				// well...
    				if(arr[4] == null) {
    					arr[4] = "This is what happens when your ";
    					// added an Update test here:
    					System.out.println("Calling a second queer");
    					ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.UPDATE, new Object[] {"product", "subCategory = \""+arr[4]+"\"","productID = \"" + arr[0]+"\""}));
    					System.out.println("worked though dinit?");
    				}
    				// now look here,  ni - it's important - if you ain't (only) using strings, remove the toString calls here!!!!!!
    				productArrayList.add(new Product(arr[0].toString(), arr[1].toString(), arr[2].toString(), arr[3].toString(), arr[4].toString()));
    			}
				tblProducts.getItems().setAll(productArrayList);
    		}
    		else {
    			System.out.println("0 -> 1 implies 1 -> 0");
    		}
    		
    	}
    	
    }
    
    
}
