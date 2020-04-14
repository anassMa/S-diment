package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;


public class Main extends Application {
	 @Override
	    public void start(Stage stage) throws Exception {
		 try{
	        Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));

	        Scene scene = new Scene(root);

	        stage.setScene(scene);

	        stage.getIcons().add(new Image("/application/images.jpg"));

	        stage.setTitle("Analyse granulométrique par tri hydraulique");

	        stage.show();

	 } catch (IOException e) {
         e.printStackTrace();
     }
	    }

	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String[] args) {
	        launch(args);
	    }

}
