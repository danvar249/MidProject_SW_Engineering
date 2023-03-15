package common;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

	/**
	 * The createWindow method is used to create new windows in a JavaFX
	 * application. It takes in the following parameters:
	 * 
	 * @param primaryStage the primary stage of the JavaFX application
	 * @param classObject  the class object of the class from which the method is
	 *                     called
	 * @param fxmlAddress  the address of the fxml file to be loaded
	 * @param cssAddress   the address of the css file to be applied to the scene
	 * @param windowTitle  the title of the new window This method loads the fxml
	 *                     file from the given fxmlAddress using the FXMLLoader
	 *                     class, creates a new scene using the loaded fxml, applies
	 *                     the css stylesheet from cssAddress (if provided), sets
	 *                     the scene's title to windowTitle, set the primary stage's
	 *                     icon, and sets the primary stage's scene to the newly
	 *                     created scene.
	 * @author Rotem
	 */
	public static void createWindow(Stage primaryStage, Object classObject, String fxmlAddress, String cssAddress,
			String windowTitle) {

		Parent root;
		try {
			root = FXMLLoader.load(classObject.getClass().getResource(fxmlAddress));
			Scene scene = new Scene(root);
			if (cssAddress != null)
				scene.getStylesheets().add(classObject.getClass().getResource(cssAddress).toExternalForm());
			Image image = new Image("/gui/ekrutServerManager.png");
			primaryStage.getIcons().add(image);
			primaryStage.setTitle(windowTitle);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
