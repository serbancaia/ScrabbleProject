package com.mycompany.scrabble_project.scrabble_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX ScrabbleClient opens up the SetupMenu asking for port and server
 * inputs
 */
public class ScrabbleClient extends Application {

    public static Stage theStage; //Allows the stage be easily accessible 

    @Override
    public void start(Stage theStage) throws IOException {
        this.theStage = theStage;
        //Creates the GUI for the setup page
        Scene scene = new Scene(loadFXML("/fxml/SetupSession"), 600, 200);
        //Add the scene to the stage
        theStage.setScene(scene);
        theStage.setTitle("Setup");   
        theStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ScrabbleClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
