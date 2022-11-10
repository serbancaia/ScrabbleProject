package com.mycompany.scrabble_project.controllers;

import com.mycompany.scrabble_project.utility.MyUtility;
import static com.mycompany.scrabble_project.scrabble_client.ScrabbleClient.theStage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The initial window asking user for port and server.
 *
 * @author Daniel
 */
public class SetupFXMLController {

    @FXML
    private TextField ipAddressTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextArea ipAddressTextArea;

    @FXML
    private TextArea portTextArea;

    @FXML
    private Button continueButton;

    private String server;
    private int servPort;

    //Instantiate MyUtility class
    MyUtility myUtility = new MyUtility();

    /**
     * Setting the input ip address to the server variable
     *
     * @param event
     */
    @FXML
    private void getTextFieldIp(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            server = ipAddressTextField.getText();
            if (myUtility.IP_checker(server) != 1) {
                ipAddressTextArea.appendText("ERROR: " + server + " isn't valid IP address." + "\n");
            } else {
                ipAddressTextArea.appendText("Server name verified" + "\n");
            }
            ipAddressTextField.clear();
        }
    }

    /**
     * Setting the input port number to the serverPort variable
     *
     * @param event
     */
    @FXML
    private void getTextFieldPort(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                servPort = Integer.parseInt(portTextField.getText().trim());
                portTextArea.appendText("Your entered: " + servPort + "\n");
            } catch (NumberFormatException nfe) {
                portTextArea.appendText("ERROR: " + portTextField.getText() + " isn't valid port." + "\n");
            }
            ipAddressTextField.clear();
        }
    }

    /**
     * Opens up the game menu if the continue button is clicked, if both fields
     * are not set we display an alert message.
     *
     * @param event
     */
    @FXML
    private void continueButtonHandle(ActionEvent event) {
        //User must fill out the necessary textfields for server and port
        if (server != null && servPort != 0) {
            Scene scene = null;
            try {
                //Instantiate a FXMLLoader object
                FXMLLoader loader = new FXMLLoader();

                //Connect the FXMLLoader to the fxml file that is stored in the jar
                loader.setLocation(GameMenuFXMLController.class.getResource("/fxml/GameMenu.fxml"));

                //The load command returns a reference to the root pane of the fxml file
                TitledPane rootPane = (TitledPane) loader.load();

                GameMenuFXMLController gameMenuController = loader.getController();

                gameMenuController.setServerParameters(server, servPort);

                scene = new Scene(rootPane);
            } catch (IOException ex) {
            }

            theStage.setScene(scene);
            theStage.setTitle("GameMenu");
            theStage.show();
        } else {
            Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Config Alert");
            alert.setContentText("Please enter a valid server and port before continuing!");
            alert.showAndWait();
        }

    }
}
