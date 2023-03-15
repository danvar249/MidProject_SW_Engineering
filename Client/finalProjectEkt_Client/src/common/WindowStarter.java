package common;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import client.ClientController;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * The WindowStarter class is used to create new windows in a JavaFX
 * application. The {@link #createWindow(Stage, Object, String, String, String)}
 * method takes in a primary stage, classObject, fxmlAddress, cssAddress and
 * windowTitle as parameters. The method loads the fxml file from the given
 * fxmlAddress using the FXMLLoader class, creates a new scene using the loaded
 * fxml, applies the css stylesheet from cssAddress (if provided), sets the
 * scene's title to windowTitle, set the primary stage's icon, and sets the
 * primary stage's scene to the newly created scene.
 * 
 * @author Rotem
 *
 */
public class WindowStarter {
	private static Stage currentStage = null;
	/*
	 * This constant relates to taking care of shutting down EK client after 20
	 * minutes
	 */
	private static final long TIME_FOR_INACTIVITY_RESET = 20 * 60 * 1000;
	
	/**
	 * The createWindow method is used to create new windows in a JavaFX
	 * application. It takes in the following parameters:
	 * 
	 * @param primaryStage          the primary stage of the JavaFX application
	 * @param classObject           the class object of the class from which the
	 *                              method is called
	 * @param fxmlAddress           the address of the fxml file to be loaded
	 * @param cssAddress            the address of the css file to be applied to the
	 *                              scene
	 * @param windowTitle           the title of the new window This method loads
	 *                              the fxml file from the given fxmlAddress using
	 *                              the FXMLLoader class, creates a new scene using
	 *                              the loaded fxml, applies the css stylesheet from
	 *                              cssAddress (if provided), sets the scene's title
	 *                              to windowTitle, set the primary stage's icon,
	 *                              and sets the primary stage's scene to the newly
	 *                              created scene.
	 * @param activateActivityCheck a flag to indicate whether to activate an
	 *                              inactivity checker
	 * @author Rotem
	 */
	public static void createWindow(Stage primaryStage, Object classObject, String fxmlAddress, String cssAddress,
			String windowTitle, boolean activateActivityCheck) {
		// added debug print:
		System.out.println("Loading UI page=" + fxmlAddress + ", with css=" + cssAddress + " and title=" + windowTitle);
		Parent root;

		try {
			if (classObject == null) {
				// hope for the best with dees
				classObject = new Object();
			}
			root = FXMLLoader.load(classObject.getClass().getResource(fxmlAddress));
			Scene scene = new Scene(root);
			if (cssAddress != null)
				scene.getStylesheets().add(classObject.getClass().getResource(cssAddress).toExternalForm());
			Image image = new Image("controllers/Images/ekrut.png");
			primaryStage.getIcons().add(image);

			primaryStage.setTitle(windowTitle);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);

			primaryStage.setOnCloseRequest(we -> {
				System.out.println("Pressed the X button.");
				if (ClientController.getCurrentSystemUser() != null) {
					System.out.println("Logging off user " + ClientController.getCurrentSystemUser().getUsername());
					ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.LOGOUT,
							ClientController.getCurrentSystemUser().getUsername()));
				}
				System.out.println("Shutting down client.");
				System.exit(0);
			});
			currentStage = primaryStage;
			/*
			 * This takes care of shutting down EK client after 20 minutes!
			 */
			if (activateActivityCheck) {
				Timer timer = new Timer();

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// check if user logged in and if we are in EK configuration
						if (ClientController.getEKCurrentMachineID() != 0
								&& ClientController.getCurrentSystemUser() != null
								&& ClientController.getCurrentSystemUser().getUsername() != null
								// if we switched window - stop this operation!
								&& currentStage != null 
								&& currentStage.equals(primaryStage)) {
							// run this in 20 minutes:
							Platform.runLater(() -> update(primaryStage));
							timer.cancel();
							timer.purge();
						}
					}

					// log the user out
					private void update(Stage primaryStage) {
						System.out.println("User has been logged off due to inactivity!");
						// actually log the user out
						ClientController.sendLogoutRequest();
						
						// save our original stage
						Stage oldStage = primaryStage;
						// create new stage
						primaryStage = new Stage();
						// close old stage
						oldStage.close();
						// launch the login page
						WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null,
								"Login", false);

						primaryStage.show();
					}
				}, TIME_FOR_INACTIVITY_RESET);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Play a sound when mouse clicks a button (untested)
	 * @param b button to click
	 * @param soundPath sound to play
	 */
	public static void buttonSoundAdder(Button b, String soundPath) {
	    Media sound = new Media(new File(soundPath).toURI().toString());
	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	    mediaPlayer.setOnEndOfMedia(new Runnable() 
	    {
	        public void run() 
	        {
	            mediaPlayer.stop();
	        }
	    });
	    
	    b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
            	mediaPlayer.play();
            }
	    }); 
	}
	
	/**
	 * Play a default sound
	 */
	public static void mySoundPlayer() {
	    Media sound = new Media(new File("src/controllers/Images/ClickTestEKRUT.mp3").toURI().toString());
	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	    mediaPlayer.setOnEndOfMedia(new Runnable() 
	    {
	        public void run() 
	        {
	            mediaPlayer.stop();
	        }
	    });
    	mediaPlayer.play();
	}

}
