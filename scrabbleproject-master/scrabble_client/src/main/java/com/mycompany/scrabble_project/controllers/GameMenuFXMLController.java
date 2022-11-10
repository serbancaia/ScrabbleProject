package com.mycompany.scrabble_project.controllers;

import static com.mycompany.scrabble_project.scrabble_client.ScrabbleClient.theStage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * This menu allows the user to select if the AI starts, go back to the setup,
 * play the game or exit
 *
 * @author Daniel
 */
public class GameMenuFXMLController {

    private String server;
    private int serverPort;
    @FXML
    private Button playButton;

    @FXML
    private CheckBox cpuStartsCheckbox;

    @FXML
    private Button exitButton;

    /**
     * Exit the program when this button is clicked
     *
     * @param event
     */
    @FXML
    void onExitHandle(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Open up the main UI for the board
     *
     * @param event
     */
    @FXML
    void onPlayGameHandle(ActionEvent event) {
        String serverResponse = "";
        Scene scene = null;

        Socket socket = null;
        DataInputStream in = null;
        OutputStream out = null;

        //A try with resources will automatically close all the connections to the server even when failing.
        try {
            socket = new Socket(server, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = socket.getOutputStream();

            //create a byte array with all 0
            byte[] byteBuffer = new byte[23];

            //send this message to the server
            out.write(byteBuffer);
            //read the number of bytes in the recievedMessage which was written by the server.
            //int resultBytes = in.read(byteBuffer);
            byte[] resultBytes = in.readNBytes(23);

            //we only want the first 13 bytes since the server is expected to answer with "ready to play"
            byte[] theMessage = new byte[13];
            for (int i = 0; i < theMessage.length; i++) {
                theMessage[i] = resultBytes[i];
            }
            serverResponse = new String(theMessage, StandardCharsets.UTF_8);

            //close the connections
        } catch (SocketException e) {
            System.out.println("caught socket exception");

            Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Socket connection alert");
            alert.setContentText("The connection to the socket cannot be made, please enter valid server and port. Please re-run the server scrabble_server beforehand.");
            alert.showAndWait();

        } catch (IOException ex) {
            System.out.println("caught io exception");
        }
        //only open the board gui if the server responsed correctly
        if (serverResponse.equals("ready to play")) {
            try {
                //Instantiate a FXMLLoader object
                FXMLLoader loader = new FXMLLoader();

                //Connect the FXMLLoader to the fxml file that is stored in the jar
                loader.setLocation(GameMenuFXMLController.class.getResource("/fxml/BoardGUI.fxml"));

                //The load command returns a reference to the root pane of the fxml file
                AnchorPane rootPane = (AnchorPane) loader.load();

                BoardGUIController boardController = loader.getController();

                //pass the socket, in, and out to the boardController
                boardController.setSocketVariables(socket, in, out);
                //create the ScrabbleGame object
                boardController.initializeScrabbleGame();

                if (cpuStartsCheckbox.isSelected()) {
                    boardController.setPlayOrder(false);
                } else {
                    boardController.setPlayOrder(true);
                }
                scene = new Scene(rootPane);
            } catch (IOException ex) {
            }

            theStage.setScene(scene);
            theStage.setTitle("Scrabble Game");
            theStage.show();

        }
    }

    /**
     * Allows the user to return to the setup Interface if they need to make
     * changes to the port and server
     *
     * @param event
     */
    @FXML
    void returnToConfigHandle(ActionEvent event) {
        Scene scene = null;
        try {
            //Instantiate a FXMLLoader object
            FXMLLoader loader = new FXMLLoader();

            //Connect the FXMLLoader to the fxml file that is stored in the jar
            loader.setLocation(GameMenuFXMLController.class.getResource("/fxml/SetupSession.fxml"));

            //The load command returns a reference to the root pane of the fxml file
            GridPane rootPane = (GridPane) loader.load();

            scene = new Scene(rootPane);
        } catch (IOException ex) {
        }

        theStage.setScene(scene);
        theStage.setTitle("Setup");
        theStage.show();
    }

    /**
     * The port and server is passed here from the SetupFXML stage
     *
     * @param server
     * @param serverPort
     */
    public void setServerParameters(String server, int serverPort) {
        this.serverPort = serverPort;
        this.server = server;
    }

}
