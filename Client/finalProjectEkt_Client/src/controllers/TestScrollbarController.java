package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TestScrollbarController {
	@FXML
	private Pane topPane;
	
	@FXML
	private Pane bottomPane;
	
	
	@FXML
	private BorderPane mainBorderPane;
	
	@FXML
	public void initialize() {
		VBox productsVbox = new VBox();
		ImageView image100 = new ImageView(new Image("controllers/Images/100.png"));
		ImageView image103 = new ImageView(new Image("controllers/Images/103.png"));
		ImageView image104 = new ImageView(new Image("controllers/Images/104.png"));
		image104.setFitWidth(300);
		ImageView image105 = new ImageView(new Image("controllers/Images/105.png"));
		ImageView image123 = new ImageView(new Image("controllers/Images/123.png"));
	
		
		productsVbox.getChildren().add(image100);
		productsVbox.getChildren().add(image103);
		productsVbox.getChildren().add(image104);
		productsVbox.getChildren().add(image105);
		productsVbox.getChildren().add(image123);
		
		
		
		
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);
		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(800);
		
		mainBorderPane.setCenter(centerScrollBar);
		
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		
	}
}
