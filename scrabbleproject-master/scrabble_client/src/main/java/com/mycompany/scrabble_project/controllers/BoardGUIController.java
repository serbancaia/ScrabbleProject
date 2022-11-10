package com.mycompany.scrabble_project.controllers;

import com.mycompany.scrabble_project.business.ScrabbleGame;
import com.mycompany.scrabble_project.data.DoubleLetterTile;
import com.mycompany.scrabble_project.data.DoubleWordTile;
import com.mycompany.scrabble_project.data.Letter;
import com.mycompany.scrabble_project.data.StartTile;
import com.mycompany.scrabble_project.data.Tile;
import com.mycompany.scrabble_project.data.TripleLetterTile;
import com.mycompany.scrabble_project.data.TripleWordTile;
import com.mycompany.scrabble_project.utility.MyUtility;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * All actions such as drag and dropping from rack to tile, play, swap, pass and
 * clear are handled in this class.
 *
 * @author Daniel Chin & Alin Caia
 */
public class BoardGUIController implements Initializable {

    private MyUtility myUtility;

    private List<String> newWords;

    //Whenever a player drags their tile onto the board. We add those potential coordinates to the list. The image of those positions on the grid will get changed when user clicks on play. 
    private List<Point2D> playPositions = new ArrayList<>();

    //When a letter is placed on the board for a play, we add it to this list
    private List<Letter> lettersPlaced = new ArrayList<>();

    //The selected rack positions for a swap are put into this list.
    private List<Integer> rackPositionsToSwap = new ArrayList<>();

    //if true, the player starts, else the cpu starts
    public boolean playerStarts;

    private ImageView temporaryImageView;//The image view to be set to null when the letter from rack is placed on the board.
    private Letter tempLetter;//The letter to be added to the lettersPlaced List once dropped on the board.
    private int rackPositionToRemove;//The rack position to be removed once the letter is dropped on the board.

    @FXML
    private ImageView rackPosition0;

    @FXML
    private ImageView rackPosition1;

    @FXML
    private ImageView rackPosition2;

    @FXML
    private ImageView rackPosition3;

    @FXML
    private ImageView rackPosition4;

    @FXML
    private ImageView rackPosition5;

    @FXML
    private ImageView rackPosition6;

    @FXML
    private Button playButton;

    @FXML
    private Button passButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button swapButton;

    @FXML
    private Text computerLastScore;

    @FXML
    private Text computerTotalScore;

    @FXML
    private Text yourLastScore;

    @FXML
    private Text yourTotalScore;

    @FXML
    private Text tilesLeft;

    @FXML
    private TextArea wordsPlayedDisplay;

    @FXML
    private GridPane boardGridPane;

    ScrabbleGame game;

    private Tile[][] tiles;

    //The variables needed for the human player to send play messages to the server
    private Socket socket;
    private DataInputStream in;
    private OutputStream out;

    /**
     * Initializes the controller class and sets up the event listeners for the
     * rack ImageView and board ImageView.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myUtility = new MyUtility();

        newWords = new ArrayList<>();

        List<Node> allGridChildren = boardGridPane.getChildren();

        List<ImageView> boardImageViews = new ArrayList<>();
        for (Node child : allGridChildren) {
            if (child instanceof ImageView) {
                boardImageViews.add((ImageView) child);
            }
        }

        for (ImageView imageView : boardImageViews) {
            //all imageViews on the board can be dragged over
            setOnDragOver(imageView);
            //all imageViews on the board can have a letter dropped on it given that it is not taken.
            setOnDragDropped(imageView);
        }
        setOnDragDetected(rackPosition0);
        setOnDragDetected(rackPosition1);
        setOnDragDetected(rackPosition2);
        setOnDragDetected(rackPosition3);
        setOnDragDetected(rackPosition4);
        setOnDragDetected(rackPosition5);
        setOnDragDetected(rackPosition6);

    }

    /**
     * The ImageView objects on the board can accept a Drag and Drop from the
     * tile rack.
     *
     * @param imageView
     */
    private void setOnDragOver(ImageView imageView) {
        imageView.setOnDragOver((DragEvent event) -> {
            ImageView source = (ImageView) event.getSource();
            int xPosition = boardGridPane.getRowIndex(source);
            int yPosition = boardGridPane.getColumnIndex(source);

            //if that tile on the board has no letter in place, we can drag and drop on it.
            if (tiles[xPosition][yPosition].getLetter() == null && event.getGestureSource() != null) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }

    /**
     * Once the image is dropped on the board, add those coordinates as tiles
     * that will potentially be played and the letter to the letters being
     * played.
     *
     * @param imageView
     */
    private void setOnDragDropped(ImageView imageView) {
        imageView.setOnDragDropped((DragEvent event) -> {
            //The letter that was taken from the rack is placed into the list of letters to be played.
            lettersPlaced.add(tempLetter);

            //The letter taken from the rack is now removed from the rack
            int[] rackPositionsToRemove = new int[1];
            rackPositionsToRemove[0] = rackPositionToRemove;
            List<Letter> removedLetters = game.getHumanPlayer().removeLettersFromRack(rackPositionsToRemove);

            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                imageView.setImage(db.getImage());
                success = true;

                ImageView source = (ImageView) event.getSource();
                int xPosition = boardGridPane.getRowIndex(source);
                int yPosition = boardGridPane.getColumnIndex(source);

                playPositions.add(new Point2D(xPosition, yPosition));

                //the most recently placed letter is placed on the tiles[][]
                tiles[xPosition][yPosition].addLetter(lettersPlaced.get(lettersPlaced.size() - 1));

                //the most recent imageView from the rack is to have its image removed.
                temporaryImageView.setImage(null);
            }
            //let the source know whether the string was successfully transferred
            // and used
            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * When clicked, the player's rack is to be reset and all letters on the
     * board that wasn't played is removed.
     *
     * @param event
     */
    @FXML
    void clearButtonHandle(ActionEvent event) {
        //all the tiles that were set with a letter are now cancelled so we remove its letter in the tiles[][]
        for (Point2D spot : playPositions) {
            Letter removedLetter = tiles[(int) spot.getX()][(int) spot.getY()].removeLetter();
            resetImage(tiles[(int) spot.getX()][(int) spot.getY()], (int) spot.getX(), (int) spot.getY());
        }
        //The letters that were removed from the rack are put back.
        game.getHumanPlayer().addLettersToRack(lettersPlaced);

        //Display the rack images to the original rack before letters were placed.
        setupRackImages();

        //the potential play is cancelled, reset the array
        playPositions = new ArrayList<>();

        lettersPlaced = new ArrayList<>();

        newWords = new ArrayList<>();
    }

    /**
     * Helper method that sets the image on the board back to its original image
     * when a play is invalid or the clear button is clicked.
     *
     * @param tile
     * @param xPosition
     * @param yPosition
     */
    private void resetImage(Tile tile, int xPosition, int yPosition) {
        ImageView imageView = myUtility.getNodeFromGridPane(boardGridPane, xPosition, yPosition);
        if (tile instanceof DoubleLetterTile) {
            imageView.setImage(new Image("/images/DL.png"));
        } else if (tile instanceof TripleLetterTile) {
            imageView.setImage(new Image("/images/TL.png"));
        } else if (tile instanceof DoubleWordTile) {
            imageView.setImage(new Image("/images/DW.png"));
        } else if (tile instanceof TripleWordTile) {
            imageView.setImage(new Image("/images/TW.png"));
        } else if (tile instanceof StartTile) {
            imageView.setImage(new Image("/images/ST.png"));
        } else {
            imageView.setImage(null);
        }
    }

    /**
     * When the pass button is clicked, we allow the ai to make its move.
     *
     * @param event
     */
    @FXML
    void passButtonHandle(ActionEvent event) {
        //The player passed the turn to the AI so the GUI is disabled temporarily
        disableGame();
        clearButtonHandle(event);

        //call this method so the ai can play its turn
        boardHasPlacedLetter();
    }

    /**
     * If the board has no words on it, then we make the ai make the first move.
     * Otherwise, the ai makes a subsequent move.
     */
    private void boardHasPlacedLetter() {
        boolean hasLetter = false;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isTilePlayed()) {
                    hasLetter = true;
                }
            }
        }
        if (hasLetter) {
            //check if game is over after player's turn is over
            if (isGameOver()) {
                //end the game if so
                this.game.endGame();
                return;
            }
            game.makeSubsequentMove();
            //check if game is over after ai's turn is over
            if (isGameOver()) {
                //end the game if so
                this.game.endGame();
                return;
            }
        } else {
            //check if game is over after player's turn is over
            if (isGameOver()) {
                //end the game if so
                this.game.endGame();
                return;
            }
            game.aiFirstMove();
            //check if game is over after ai's turn is over
            if (isGameOver()) {
                //end the game if so
                this.game.endGame();
                return;
            }
        }
    }

    /**
     * Constructs the byte message from the placed letters and sends it to the
     * server.
     *
     * @param event
     */
    @FXML
    void playButtonHandle(ActionEvent event) {
        //If no letters were placed on the board, show a alert message
        if (playPositions.isEmpty()) {
            Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PLAY ALERT");
            alert.setContentText("A letter must have been placed on the board to make a play!");
            alert.showAndWait();

            return;
        }

        if (sendMessageToServer()) {
            //check if game is over after player's turn is over
            if (isGameOver()) {
                //end the game if over, call the endGame method to display the results
                this.game.endGame();
            }
            //the ai makes a move once the play is validated
            game.makeSubsequentMove();
            //check if game is over after ai's turn is over
            if (isGameOver()) {
                //end the game if over, call the endGame method to display the results
                this.game.endGame();
            }
        }
        //reset all the arraylists relating to a specific play.
        playPositions = new ArrayList<>();
        lettersPlaced = new ArrayList<>();
        newWords = new ArrayList<>();
    }

    /**
     * Sending the byte[] message to server for a play and receiving the
     * response.
     */
    private boolean sendMessageToServer() {
        byte[] message = myUtility.constructPlayMessage(playPositions, lettersPlaced);

        try {
            //send this message to the server
            out.write(message);
            //read the bytes returned from the server. The int represents how many bytes were returned (23). The actual response is stored in the message variable.
            int resultBytes = in.read(message); //store the reponse in the message variable.

            //if the first returned byte is 0, the word was invalid so we reset the rack and the letters placed on the board.
            if (message[0] == 0) {
                invalidPlayReset();

                //display a dialog telling the user that the played move was invalid.
                Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Play is invalid");
                alert.setContentText("The word is invalid!");
                alert.showAndWait();

            } //if the server responds saying that the first word must be on the start tile
            if (message[0] == -1) {
                invalidPlayReset();

                //display a dialog telling the user that the played move was invalid.
                Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("First Word Alert");
                alert.setContentText("The first word must be on the Star tile!");
                alert.showAndWait();

            }
            if (message[0] != 0 && message[0] != -1) {
                //The player can no longer interact with the buttons or the rack until it is his/her turn

                disableGame();
                //the play was accepted
                int points = message[0];

                List<String> verticalWords = myUtility.getAllVerticalWords(tiles, lettersPlaced);
                for (String word : verticalWords) {
                    newWords.add(word);
                }
                List<String> horizontalWords = myUtility.getAllHorizontalWords(tiles, lettersPlaced);
                for (String word : horizontalWords) {
                    newWords.add(word);
                }

                //the tiles on the board that were placed will now have their isPlacedTile field set to true
                myUtility.setWordTilesToPlayed(playPositions, tiles);
                //display the words that were accepted by server
                myUtility.updateWordsPlayedDisplay(wordsPlayedDisplay, newWords);
                //refill the player's rack so they have 7 tiles for the next turn.
                game.refillHumanRack();

                setupRackImages();

                //update the points for the human player
                game.getHumanPlayer().setLastScore(points);
                game.getHumanPlayer().sumTotalScore(points);
                yourLastScore.setText(String.valueOf(game.getHumanPlayer().getLastScore()));
                yourTotalScore.setText(String.valueOf(game.getHumanPlayer().getTotalScore()));

                //update the amount of tiles left on board to be placed.
                //go through this if/else condition to make sure numTilesLeft is never below 0
                if (game.getNumTilesLeft() - lettersPlaced.size() >= 0) {
                    game.setNumTilesLeft(game.getNumTilesLeft() - lettersPlaced.size());
                } else {
                    game.setNumTilesLeft(0);
                }
                tilesLeft.setText(String.valueOf(game.getNumTilesLeft()));
                return true;
            }
        } catch (SocketException ex) {

        } catch (IOException ex) {
        }
        return false;
    }

    /**
     * Whenever the user makes an invalid play, we remove the letters placed on
     * the board and reset the images.The player rack is also reset.
     */
    private void invalidPlayReset() {
        //the tiles that have been given a letter will now be reset back to a null letter, since the play was not valid.
        for (Point2D point : playPositions) {
            Letter removedLetter = tiles[(int) point.getX()][(int) point.getY()].removeLetter();
            //reset that tile's image based on the tile type.
            resetImage(tiles[(int) point.getX()][(int) point.getY()], (int) point.getX(), (int) point.getY());
        }

        //put the players rack to the way it was before
        game.getHumanPlayer().addLettersToRack(lettersPlaced);
        setupRackImages();
    }

    /**
     * A user can swap as many tiles as they want, once per turn. Opens a new
     * stage allowing to selected letters to swap.
     *
     * @param event
     */
    @FXML
    void swapButtonHandle(ActionEvent event) {

        clearButtonHandle(event);

        try {
            Stage swapStage = new Stage();
            //Instantiate a FXMLLoader object
            FXMLLoader loader = new FXMLLoader();

            //Connect the FXMLLoader to the fxml file that is stored in the jar
            loader.setLocation(this.getClass().getResource("/fxml/SwapWindow.fxml"));

            //The load command returns a reference to the root pane of the fxml file
            Parent rootPane = (AnchorPane) loader.load();

            SwapWindowController controller = loader.getController();
            controller.setRackOptions(game.getHumanPlayer().getRack());
            controller.setBoardController(this);

            swapStage.setScene(new Scene(rootPane));
            swapStage.setTitle("Swap");
            swapStage.initModality(Modality.APPLICATION_MODAL);
            swapStage.showAndWait();

            /**
             * If the number of selected letters is 0, we show an alert
             */
            if (rackPositionsToSwap.isEmpty()) {
                Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Swap Alert");
                alert.setContentText("No rack letters were selected to swap!");
                alert.showAndWait();
            } else {
                int[] rackPositions = new int[rackPositionsToSwap.size()];
                for (int i = 0; i < rackPositions.length; i++) {
                    rackPositions[i] = rackPositionsToSwap.get(i);
                }
                game.swapHumanLetters(rackPositions);
                setupRackImages();
                rackPositionsToSwap = new ArrayList<>();

                //the interface is disabled until the AI completes his turn
                disableGame();

                //the ai will makes its turn
                boardHasPlacedLetter();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * The Swap Window stage will pass the selected rack positions to be
     * swapped.
     *
     * @param swapRackPostiions
     */
    public void setSwapPositions(List<Integer> swapRackPostiions) {
        this.rackPositionsToSwap = new ArrayList<>(swapRackPostiions);
    }

    /**
     * If the user decides to have the CPU start, it will be passed to the
     * playerStarts field.
     *
     * @param playerStarts
     */
    public void setPlayOrder(boolean playerStarts) {
        this.playerStarts = playerStarts;
        isAIStarting();
    }

    /**
     * Makes the ai make a move if user choose to have computer start
     */
    private void isAIStarting() {
        if (!playerStarts) {
            game.aiFirstMove();
        }
    }

    /**
     * Whenever a letter is dragged from rack to tile, we store the information
     * related to that letter and set it once it is dropped.
     *
     * @param imageView
     */
    private void setOnDragDetected(ImageView imageView) {
        imageView.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());

            String idname = imageView.getId();

            //int rackPosition = idname.charAt(idname.length() - 1);
            int rackPosition = Integer.parseInt(idname.substring(idname.length() - 1));

            //Once the the letter is dropped on the board, then we will add it to the lettersPlaced arrayList.
            this.tempLetter = game.getHumanPlayer().getRack().get(rackPosition);

            //Once the imageView is dragged and dropped onto the board, we remove the letter from the player's rack.
            this.rackPositionToRemove = rackPosition;

            //The image that was dragged and dropped onto the board will no longer appear in the rack.
            temporaryImageView = imageView;

            db.setContent(content);
            event.consume();
        });

    }

    /**
     * Pass the Socket, DataInputStream and OutputStream so this controller and
     * the AI can send messages to the server.
     *
     * @param socket
     * @param in
     * @param out
     */
    public void setSocketVariables(Socket socket, DataInputStream in, OutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Initialize the ScrabbleGame object and pass it the socket, in and out so
     * the AI can send messages to the server.
     */
    public void initializeScrabbleGame() {
        //give a reference to this controller so the ScrabbleGame can access the private fields here. Also pass the socket, in and out so the AI can send messages to the server.
        game = new ScrabbleGame(this, socket, in, out);
        //use the getters to get the board tiles
        tiles = game.getBoard().getTiles();
        //showing the approritate images depending on the rack's contents
        setupRackImages();
    }

    /**
     * Shows the images on the rack depending on the player's rack.
     */
    private void setupRackImages() {
        List<Letter> humanRack = game.getHumanPlayer().getRack();

        for (int i = 0; i < humanRack.size(); i++) {
            char theLetter = humanRack.get(i).getLetter();
            switch (i) {
                case 0:
                    myUtility.setTheImage(rackPosition0, theLetter);
                    break;
                case 1:
                    myUtility.setTheImage(rackPosition1, theLetter);
                    break;
                case 2:
                    myUtility.setTheImage(rackPosition2, theLetter);
                    break;
                case 3:
                    myUtility.setTheImage(rackPosition3, theLetter);
                    break;
                case 4:
                    myUtility.setTheImage(rackPosition4, theLetter);
                    break;
                case 5:
                    myUtility.setTheImage(rackPosition5, theLetter);
                    break;
                case 6:
                    myUtility.setTheImage(rackPosition6, theLetter);
                    break;
            }
        }
    }

    /**
     * Getter which is called from ScrabbleGame
     *
     * @return the gridPane containing the imageViews
     */
    public GridPane getBoardGridPane() {
        return this.boardGridPane;
    }

    /**
     * Getter which is called from ScrabbleGame
     *
     * @return the textArea for played words
     */
    public TextArea getTextArea() {
        return this.wordsPlayedDisplay;
    }

    /**
     * Getter which is called from ScrabbleGame
     *
     * @return the computer's last score text UI
     */
    public Text getComputerLastScore() {
        return this.computerLastScore;
    }

    /**
     * Getter which is called from ScrabbleGame
     *
     * @return the computer's total score text UI
     */
    public Text getComputerTotalScore() {
        return this.computerTotalScore;
    }

    /**
     * Getter which is called from ScrabbleGame
     *
     * @return the tiles left text UI
     */
    public Text getTilesLeft() {
        return this.tilesLeft;
    }

    /**
     * This method checks whether there are no more tiles left and ends the game
     * if that's the case
     *
     * @return true or false
     */
    public boolean isGameOver() {
        if (this.game.getNumTilesLeft() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to disable the game's gui elements once the game ends
     * or when the AI is searching for a valid word
     */
    public void disableGame() {
        //Disables all the fxml objects that affect the state of the board and racks
        this.rackPosition0.setDisable(true);
        this.rackPosition1.setDisable(true);
        this.rackPosition2.setDisable(true);
        this.rackPosition3.setDisable(true);
        this.rackPosition4.setDisable(true);
        this.rackPosition5.setDisable(true);
        this.rackPosition6.setDisable(true);
        this.boardGridPane.setDisable(true);
        this.playButton.setDisable(true);
        this.passButton.setDisable(true);
        this.clearButton.setDisable(true);
        this.swapButton.setDisable(true);
    }

    /**
     * When it is the player's turn we enable the buttons and imageViews.
     */
    public void enableGameButtons() {
        this.rackPosition0.setDisable(false);
        this.rackPosition1.setDisable(false);
        this.rackPosition2.setDisable(false);
        this.rackPosition3.setDisable(false);
        this.rackPosition4.setDisable(false);
        this.rackPosition5.setDisable(false);
        this.rackPosition6.setDisable(false);
        this.boardGridPane.setDisable(false);
        this.playButton.setDisable(false);
        this.passButton.setDisable(false);
        this.clearButton.setDisable(false);
        this.swapButton.setDisable(false);
    }
}
