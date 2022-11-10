package com.mycompany.scrabble_project.controllers;

import com.mycompany.scrabble_project.data.Letter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * The pop up window for the player to swap letters
 *
 * @author Daniel
 */
public class SwapWindowController {

    private BoardGUIController boardController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox rackPosition0;

    @FXML
    private CheckBox rackPosition1;

    @FXML
    private CheckBox rackPosition2;

    @FXML
    private CheckBox rackPosition3;

    @FXML
    private CheckBox rackPosition4;

    @FXML
    private CheckBox rackPosition5;

    @FXML
    private CheckBox rackPosition6;

    @FXML
    private Button swapButton;

    /**
     * Pass the rack positions to be swapped back to the board controller. Close
     * the overlapping stage.
     *
     * @param event
     */
    @FXML
    void performSwap(ActionEvent event) {
        List<Integer> rackPositionsToSwap = new ArrayList<>();

        if (rackPosition0.isSelected()) {
            rackPositionsToSwap.add(0);
        }
        if (rackPosition1.isSelected()) {
            rackPositionsToSwap.add(1);
        }
        if (rackPosition2.isSelected()) {
            rackPositionsToSwap.add(2);
        }
        if (rackPosition3.isSelected()) {
            rackPositionsToSwap.add(3);
        }
        if (rackPosition4.isSelected()) {
            rackPositionsToSwap.add(4);
        }
        if (rackPosition5.isSelected()) {
            rackPositionsToSwap.add(5);
        }
        if (rackPosition6.isSelected()) {
            rackPositionsToSwap.add(6);
        }
        //Pass the selected rack letters to be swapped 
        boardController.setSwapPositions(rackPositionsToSwap);

        //close the pop up window, return to the game
        Stage stage = (Stage) swapButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {

        assert rackPosition0 != null : "fx:id=\"rackPosition0\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition1 != null : "fx:id=\"rackPosition1\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition2 != null : "fx:id=\"rackPosition2\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition3 != null : "fx:id=\"rackPosition3\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition4 != null : "fx:id=\"rackPosition4\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition5 != null : "fx:id=\"rackPosition5\" was not injected: check your FXML file 'SwapWindow.fxml'.";
        assert rackPosition6 != null : "fx:id=\"rackPosition6\" was not injected: check your FXML file 'SwapWindow.fxml'.";

    }

    /**
     * The player's current rack should be displayed in this window
     *
     * @param letters
     */
    public void setRackOptions(List<Letter> letters) {
        rackPosition0.setText(String.valueOf(letters.get(0).getLetter()));
        rackPosition1.setText(String.valueOf(letters.get(1).getLetter()));
        rackPosition2.setText(String.valueOf(letters.get(2).getLetter()));
        rackPosition3.setText(String.valueOf(letters.get(3).getLetter()));
        rackPosition4.setText(String.valueOf(letters.get(4).getLetter()));
        rackPosition5.setText(String.valueOf(letters.get(5).getLetter()));
        rackPosition6.setText(String.valueOf(letters.get(6).getLetter()));
    }

    /**
     * This controller needs a reference to the board controller to pass the
     * selected rack letters.
     *
     * @param boardController
     */
    public void setBoardController(BoardGUIController boardController) {
        this.boardController = boardController;
    }
}
