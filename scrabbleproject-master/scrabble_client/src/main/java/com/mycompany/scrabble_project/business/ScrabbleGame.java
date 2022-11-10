package com.mycompany.scrabble_project.business;

import com.mycompany.scrabble_project.controllers.BoardGUIController;
import com.mycompany.scrabble_project.data.Ai;
import com.mycompany.scrabble_project.data.Board;
import com.mycompany.scrabble_project.data.Human;
import com.mycompany.scrabble_project.data.Letter;
import com.mycompany.scrabble_project.data.Player;
import com.mycompany.scrabble_project.data.Tile;
import com.mycompany.scrabble_project.utility.MyUtility;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import me.shib.java.lib.diction.DictionWord;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * This class is what will be used to run the Scrabble game on the back-end.
 *
 * @author Alin Caia & Asli Zeybek & Daniel Chin
 */
public class ScrabbleGame {

    //the socket variables passed from the board controller which allows us to send messages to the server.
    private Socket socket;
    private DataInputStream in;
    private OutputStream out;

    private BoardGUIController boardController;
    private GridPane gridPane;
    private TextArea wordsPlayedDisplay;
    private Text computerLastScore;
    private Text computerTotalScore;
    private Text tilesLeft;

    private MyUtility myUtility;
    private Board board;
    private List<Letter> letterBag;
    private Ai computer;
    private Human humanPlayer;
    private int numTilesLeft;
    private List<DictionWord> wordsPlayed;

    /**
     * Constructor that retrieves all the GUI components of the board that we
     * will need. Initializes a board, player, AI and the letter bag
     *
     * @param boardController Controller of the board
     * @param socket
     * @param in
     * @param out
     */
    public ScrabbleGame(BoardGUIController boardController, Socket socket, DataInputStream in, OutputStream out) {
        this.boardController = boardController;
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.gridPane = boardController.getBoardGridPane();
        this.wordsPlayedDisplay = boardController.getTextArea();
        this.computerLastScore = boardController.getComputerLastScore();
        this.computerTotalScore = boardController.getComputerTotalScore();
        this.tilesLeft = boardController.getTilesLeft();
        this.myUtility = new MyUtility();
        this.board = new Board();
        this.letterBag = new ArrayList<>(98);
        this.computer = new Ai();
        this.humanPlayer = new Human();
        this.numTilesLeft = 84;
        this.wordsPlayed = new ArrayList<>();
        setUp();
    }

    /**
     * Getter for the board object
     *
     * @return the board of the game
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Setter for the board object
     *
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Getter for the letter bag list uses deep-copy
     *
     * @return the list of letters kept in the game bag
     */
    public List<Letter> getLetterBag() {
        List<Letter> copyLetterBag = new ArrayList<>(this.letterBag.size());
        this.letterBag.forEach(letter -> copyLetterBag.add(letter));
        return copyLetterBag;
    }

    /**
     * Setter for the letter bag list
     *
     * @param letterBag
     */
    public void setLetterBag(List<Letter> letterBag) {
        this.letterBag = letterBag;
    }

    /**
     * Getter for the Ai object
     *
     * @return the ai in the game
     */
    public Ai getComputer() {
        return this.computer;
    }

    /**
     * Setter for the Ai object
     *
     * @param computer
     */
    public void setComputer(Ai computer) {
        this.computer = computer;
    }

    /**
     * Getter for the Human object
     *
     * @return the normal player in the game
     */
    public Human getHumanPlayer() {
        return this.humanPlayer;
    }

    /**
     * Setter for the Human object
     *
     * @param humanPlayer
     */
    public void setHumanPlayer(Human humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    /**
     * Getter for the int field numTilesLeft
     *
     * @return the number of tiles left
     */
    public int getNumTilesLeft() {
        return this.numTilesLeft;
    }

    /**
     * Setter for the int field numTilesLeft
     *
     * @param tilesLeft
     */
    public void setNumTilesLeft(int tilesLeft) {
        this.numTilesLeft = tilesLeft;
    }

    /**
     * Getter for the list of words that have been played uses deep-copy
     *
     * @return the list of words that have been played
     */
    public List<DictionWord> getWordsPlayed() {
        List<DictionWord> copyWordsPlayed = new ArrayList<>(this.wordsPlayed.size());
        this.wordsPlayed.forEach(word -> copyWordsPlayed.add(word));
        return copyWordsPlayed;
    }

    /**
     * Setter for the list of words that have been played
     *
     * @param wordsPlayed
     */
    public void setWordsPlayed(List<DictionWord> wordsPlayed) {
        this.wordsPlayed = wordsPlayed;
    }

    /**
     * This method is used to set up the game
     */
    private void setUp() {
        fillLetterBag();
        giveHumanLetters(7);
        giveAiLetters(7);
    }

    /**
     * This method is used to fill the letter bag with Letter objects
     */
    private void fillLetterBag() {
        //list of how many of each letter should be in the bag
        int[] nbEachLetter = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int nbEachLetterIndex = 0;
        //iterate through the ascii character values between A and Z
        for (int i = 65; i < 91; i++) {
            for (int j = 0; j < nbEachLetter[nbEachLetterIndex]; j++) {
                addLetterToBag(new Letter((char) i));
            }
            nbEachLetterIndex++;
        }
    }

    /**
     * This method is used to add a Letter object in the letter bag
     *
     * @param letter
     */
    private void addLetterToBag(Letter letter) {
        this.letterBag.add(letter);
    }

    /**
     * This method is used to add letters in bulk in the letter bag Repeatedly
     * calls addLetterToBag
     *
     * @param letters list of letters to add
     */
    private void addLettersToBag(List<Letter> letters) {
        letters.forEach(this::addLetterToBag);
    }

    /**
     * This method removes a Letter object from letter bag
     *
     * @return the Letter removed from the letter bag
     */
    private Letter removeLetterFromBag() {
        return letterBag.remove(getRandomLetterIndex());
    }

    /**
     * This method is used to remove letters in bulk from the letter bag
     * Repeatedly calls removeLetterFromBag
     *
     * @param nbLetters number of letters to remove
     * @return the letters removed from the letter bag
     */
    private List<Letter> removeLettersFromBag(int nbLetters) {
        List<Letter> letters = new ArrayList<>(nbLetters);
        for (int i = 0; i < nbLetters; i++) {
            letters.add(removeLetterFromBag());
        }
        return letters;
    }

    /**
     * This method is used to give Letter objects to give to a Human player's
     * rack
     *
     * @param nbLetters number of letters to give
     */
    private void giveHumanLetters(int nbLetters) {
        this.humanPlayer.addLettersToRack(removeLettersFromBag(nbLetters));
    }

    /**
     * This method is used to give Letter objects to give to an Ai player's rack
     *
     * @param nbLetters number of letters to give
     */
    private void giveAiLetters(int nbLetters) {
        this.computer.addLettersToRack(removeLettersFromBag(nbLetters));
    }

    /**
     * This method is used to swap a Human player's letters with other ones from
     * the letter bag. You can't swap letters if the letter bag doesn't contain
     * enough letters
     *
     * @param rackPositions indices on the rack containing a letter to swap
     */
    public void swapHumanLetters(int[] rackPositions) {
        //Swap the human's letters with the letter bag if the letter bag contains enough letters
        if (rackPositions.length < this.letterBag.size()) {
            List<Letter> playerLetters = this.humanPlayer.removeLettersFromRack(rackPositions);
            giveHumanLetters(rackPositions.length);
            addLettersToBag(playerLetters);
        } //otherwise, the number of tiles left will be set to 0 to end the game
        else {
            this.setNumTilesLeft(0);
        }
    }

    /**
     * This method is used to swap an Ai player's letters with other ones from
     * the letter bag. You can't swap letters if the letter bag doesn't contain
     * enough letters
     *
     * @param rackPositions indices on the rack containing a letter to swap
     */
    private void swapAiLetters(int[] rackPositions) {
        //Swap the ai's letters with the letter bag if the letter bag contains enough letters
        if (rackPositions.length < this.letterBag.size()) {
            List<Letter> playerLetters = this.computer.removeLettersFromRack(rackPositions);
            giveAiLetters(rackPositions.length);
            addLettersToBag(playerLetters);
        } //otherwise, the number of tiles left will be set to 0 to end the game
        else {
            this.setNumTilesLeft(0);
        }
    }

    /**
     * This method is used to completely fill a Human player's rack with Letter
     * objects depending on how many null values the rack list contains. You
     * can't refill a rack if the letter bag doesn't contain enough letters
     */
    public void refillHumanRack() {
        //Give the human letters if the letter bag contains enough of them
        if (this.humanPlayer.countNullsInRack() < this.letterBag.size()) {
            giveHumanLetters(this.humanPlayer.countNullsInRack());
        } //otherwise, the number of tiles left will be set to 0 to end the game
        else {
            this.setNumTilesLeft(0);
        }
    }

    /**
     * This method is used to completely fill an Ai player's rack with Letter
     * objects depending on how many null values the rack list contains. You
     * can't refill a rack if the letter bag doesn't contain enough letters
     */
    public void refillAiRack() {
        //Give the ai letters if the letter bag contains enough of them
        if (this.computer.countNullsInRack() < this.letterBag.size()) {
            giveAiLetters(this.computer.countNullsInRack());
        } //otherwise, the number of tiles left will be set to 0 to end the game
        else {
            this.setNumTilesLeft(0);
        }
    }

    /**
     * This method is used whenever the program needs to remove a letter from
     * the letter bag
     *
     * @return the index in the letter bag list where a letter will be removed
     */
    private int getRandomLetterIndex() {
        Random rand = new Random();
        return rand.nextInt(letterBag.size());
    }

    /**
     * This method is used once the game ends, to display the final result in a
     * dialog/alert box.
     *
     */
    public void endGame() {
        try // Close the socket , in , and out since the server is done servicing this client.
        {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Exception = " + e.getMessage());
        }

        //Total score of each player once points of unplaced tiles are deducted
        int humanScore = humanPlayer.getTotalScore() - countPointsInRack(this.humanPlayer);
        int computerScore = computer.getTotalScore() - countPointsInRack(this.computer);

        Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FINAL GAME SCORE");

        String message = "After deducting points of unplaced tiles, the score is: \n"
                + "You : " + humanScore + " Computer : " + computerScore;

        if (humanScore > computerScore) {
            alert.setContentText(message + "\nYou won!");
        } else if (humanScore < computerScore) {
            alert.setContentText(message + "\nYou lost!");
        } else {
            alert.setContentText(message + "\nYou tied!");
        }
        this.boardController.disableGame();
        alert.show();
    }

    /**
     * This method is used to get the sum of the values of the unplaced tiles in
     * a player's rack.
     *
     * @param player
     * @return int - number of points
     */
    private int countPointsInRack(Player player) {
        int points = 0;
        List<Letter> playerRack = player.getRack();
        for (Letter letter : playerRack) {
            points += letter.getValue();
        }
        return points;
    }

    //THE METHODS BELOW ARE MEANT TO BE USED FOR THE AI
    //All the permutations of the ai's rack
    private List<String> allPossibleWords = new ArrayList<String>();
    //The letters placed on the board by ai
    private List<Letter> aiPlacedLetters = new ArrayList<Letter>();
    //the new words formed by the ai during a play
    private List<String> newWords = new ArrayList<>();
    private int pointsMadeInServer = 0;
    //the ai will have 15 seconds to make a move. Or else it will swap its rack
    private long end;

    /**
     * The ai will make the first move if the user selected the checkbox in the
     * gameMenu
     */
    public void aiFirstMove() {
        //The AI has 15 seconds to make the first move
        long time = System.currentTimeMillis();
        end = time + 15000;

        //the first word must always start on the start tile
        Point2D startPosition = new Point2D(7, 7);
        //AI's rack has 7 letters, so the longest word possibly made is 7 letters long
        int maxWordLength = 7;

        //AI's first word must be between 2 and 7 letters.
        for (int i = 2; i <= maxWordLength; i++) {
            //all the i letter words will be created using the ai's letter rack
            combineRackLettersIntoString(i);
            if (allPossibleWords.isEmpty()) {
                continue;
            }

            //These are all the positions on the board where the ai will be placing its letters
            List<Point2D> coordinates = new ArrayList<>();
            coordinates.add(startPosition);
            int columnIndex = 7;
            while (coordinates.size() < i) {
                columnIndex++;
                coordinates.add(new Point2D(7, columnIndex));
            }
            //for every letter combination made from the AI rack, send it to the server to validate
            for (String word : allPossibleWords) {
                if (System.currentTimeMillis() >= end) {
                    invalidPlayReset(coordinates);
                    resetPlayVariables();
                    //we will swap the ai's entire rack since no moves could be played in 15 seconds
                    swapAiLetters(new int[]{0, 1, 2, 3, 4, 5, 6});

                    Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Computer cannot make any play");
                    alert.setHeaderText("The computer could not make a play in 15 seconds");
                    alert.setContentText("The computer has swapped its rack");
                    alert.showAndWait();
                    return;
                }
                //start adding letters from the rack onto the board
                int currentBoardSpot = 0;
                char[] wordLetters = word.toCharArray();
                for (char letter : wordLetters) {
                    boolean rackHasLetter = isLetterInComputerRack(letter, coordinates.get(currentBoardSpot));
                    if (!rackHasLetter) {
                        invalidPlayReset(coordinates);
                        //reset the letters played by ai
                        aiPlacedLetters = new ArrayList<>();
                        //move onto the next word
                        break;
                    }
                    //move onto the next position on the board
                    currentBoardSpot++;
                }

                //if the ai has placed letters on the board, we will validate it by sending it to server
                if (!aiPlacedLetters.isEmpty()) {
                    if (sendMessageToServer(coordinates)) {
                        allPossibleWords = new ArrayList<>();
                        //give the ai's rack new tiles
                        refillAiRack();
                        //re-display the board with the new letters on it.
                        for (Point2D point : coordinates) {
                            // The image which we will change since the play was valid
                            ImageView imageView = myUtility.getNodeFromGridPane(gridPane, (int) point.getX(), (int) point.getY());
                            char letter = board.getTiles()[(int) point.getX()][(int) point.getY()].getLetter().getLetter();
                            myUtility.setTheImage(imageView, letter);
                        }
                        List<String> verticalWords = myUtility.getAllVerticalWords(board.getTiles(), aiPlacedLetters);
                        for (String newWord : verticalWords) {
                            newWords.add(newWord);
                        }
                        List<String> horizontalWords = myUtility.getAllHorizontalWords(board.getTiles(), aiPlacedLetters);
                        for (String newWord : horizontalWords) {
                            newWords.add(newWord);
                        }
                        //update the words played, scoreboard
                        updateScoreBoardInterface();

                        //the tiles on the board that were placed will now have their isPlacedTile field set to true
                        myUtility.setWordTilesToPlayed(coordinates, board.getTiles());

                        resetPlayVariables();

                        //The AI's turn is over, enable the buttons for the player
                        boardController.enableGameButtons();
                        return;
                    } else {
                        invalidPlayReset(coordinates);
                        //reset the letters played by ai
                        aiPlacedLetters = new ArrayList<>();
                    }
                }
            }
            allPossibleWords = new ArrayList<>();
        }
    }

    /**
     * If the board already has words on it, the ai will create new ones by
     * appending to the current letter on the tile
     */
    public void makeSubsequentMove() {
        //first find all tiles on the board that have a letter already
        List<Point2D> occupiedSpots = findAllOccupiedTiles();

        //for every spot that has a letter, check how many tiles are available horizontally and vertically
        long time = System.currentTimeMillis();
        end = time + 15000;

        //for every occupied spot on the board, try out the possible plays
        for (Point2D point : occupiedSpots) {
            int horizontalOpenSpaces = getHorizontalEmptySpaces(point);
            int verticalOpenSpaces = getVerticalEmptySpaces(point);

            if (System.currentTimeMillis() >= end) {
                break;
            }

            //have the ai generate the horizontal plays to be sent to the server
            if (createPossibleComputerPLay(point, horizontalOpenSpaces, true)) {
                return;
            }

            if (System.currentTimeMillis() >= end) {
                break;
            }

            //have the ai generate all the vertical plays to be sent to the server
            if (createPossibleComputerPLay(point, verticalOpenSpaces, false)) {
                return;
            }

            if (System.currentTimeMillis() >= end) {
                break;
            }
        }

        //we will swap the ai's entire rack since no moves could be played in 15 seconds
        swapAiLetters(new int[]{0, 1, 2, 3, 4, 5, 6});

        Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Computer cannot make any play");
        alert.setHeaderText("The computer could not make a play in 15 seconds");
        alert.setContentText("The computer has swapped its rack");
        alert.showAndWait();

        //The AI's turn is over, allow the user to interact with the controls
        boardController.enableGameButtons();
    }

    /**
     * For every spot on the board that has a letter already, we either keeping
     * adding letters horizontally or vertically until a play is validated in
     * the server. Generates all possible word combinations using the AI's rack.
     *
     * @param startPosition : The first letter from which keep adding letters
     * until a word is found.
     * @param openSpaces : the number of open tiles to the right or under a
     * given position
     * @param isHorizontal : if true, then we only add letters to the right of
     * the startLetter, false : add letters to bottom of startLetter
     * @return true if the word is valid, false otherwise
     */
    private boolean createPossibleComputerPLay(Point2D startPosition, int openSpaces, boolean isHorizontal) {
        if (openSpaces == 0) {
            return false;
        }
        //creating 2,3,4 ... letter words depending on the number of open spaces available
        for (int i = 1; i <= openSpaces; i++) {
            //all the words of length 'i' will be created using the ai's letter rack
            combineRackLettersIntoString(i);
            if (allPossibleWords.isEmpty()) {
                continue;
            }

            //These are all the positions on the board where the ai will be placing its letters
            List<Point2D> coordinates = new ArrayList<>();

            //if the play is horizontal, we will only add letters to the right of the base position
            if (isHorizontal) {
                int columnIndex = (int) startPosition.getY();
                while (coordinates.size() < i) {
                    columnIndex++;
                    coordinates.add(new Point2D(startPosition.getX(), columnIndex));
                }
                //if the play is vertical, we will only add letters to the buttom of the base position
            } else {
                int rowIndex = (int) startPosition.getX();
                while (coordinates.size() < i) {
                    rowIndex++;
                    coordinates.add(new Point2D(rowIndex, startPosition.getY()));
                }
            }

            //For every letter combination made from the rack, send it to the server to validate.
            for (String word : allPossibleWords) {
                if (System.currentTimeMillis() >= end) {
                    invalidPlayReset(coordinates);
                    resetPlayVariables();
                    return false;
                }
                //start adding the letters of the rack onto the board.
                int currentBoardSpot = 0;
                char[] wordLetters = word.toCharArray();
                for (char letter : wordLetters) {
                    boolean rackHasLetter = isLetterInComputerRack(letter, coordinates.get(currentBoardSpot));
                    //if the rack does not have this letter
                    if (!rackHasLetter) {
                        invalidPlayReset(coordinates);
                        //reset the letters played by ai
                        aiPlacedLetters = new ArrayList<>();
                        //move onto the next word
                        break;
                    }
                    //move onto the next spot on the board on which we will add a letter
                    currentBoardSpot++;
                }

                //if the letters for that specific word were placed on the board, then send it to server.
                if (!aiPlacedLetters.isEmpty()) {
                    if (sendMessageToServer(coordinates)) {
                        allPossibleWords = new ArrayList<>();
                        //give the ai's rack new tiles
                        refillAiRack();
                        //re-display the board with the new letters on it.
                        for (Point2D point : coordinates) {
                            // The image which we will change since the play was valid
                            ImageView imageView = myUtility.getNodeFromGridPane(gridPane, (int) point.getX(), (int) point.getY());
                            char letter = board.getTiles()[(int) point.getX()][(int) point.getY()].getLetter().getLetter();
                            myUtility.setTheImage(imageView, letter);
                        }
                        List<String> verticalWords = myUtility.getAllVerticalWords(board.getTiles(), aiPlacedLetters);
                        for (String newWord : verticalWords) {
                            newWords.add(newWord);
                        }
                        List<String> horizontalWords = myUtility.getAllHorizontalWords(board.getTiles(), aiPlacedLetters);
                        for (String newWord : horizontalWords) {
                            newWords.add(newWord);
                        }

                        updateScoreBoardInterface();

                        //the tiles on the board that were placed will now have their isPlacedTile field set to true
                        myUtility.setWordTilesToPlayed(coordinates, board.getTiles());

                        resetPlayVariables();
                        Dialog<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Computer made a play");
                        alert.setHeaderText("The AI successfully placed a word");
                        alert.setContentText("The points made by AI : " + pointsMadeInServer);
                        alert.showAndWait();

                        //the AI's turn is over, so enable the interface for the human player.
                        boardController.enableGameButtons();
                        return true;
                    } else {
                        invalidPlayReset(coordinates);
                        //reset the letters played by ai
                        aiPlacedLetters = new ArrayList<>();
                    }
                }
            }
            allPossibleWords = new ArrayList<>();
        }
        return false;
    }

    /**
     * Finds all the tiles on the board which already has a letter placed on it.
     *
     * @return the coordinates of the tiles with letters on them
     */
    private List<Point2D> findAllOccupiedTiles() {
        List<Point2D> occupiedSpots = new ArrayList<>();
        Tile[][] tiles = board.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isTilePlayed()) {
                    occupiedSpots.add(new Point2D(i, j));
                }
            }
        }
        return occupiedSpots;
    }

    /**
     * Finds the amount of horizontal open tiles relative to a given point
     *
     * @param startOfWord
     * @return int representing the number of open tiles
     */
    private int getHorizontalEmptySpaces(Point2D startOfWord) {
        Tile[][] tiles = board.getTiles();
        boolean hasMoreSpots = true;

        int horizontalOpenSpots = 0;

        int neighborIndex = 1;
        while (hasMoreSpots) {
            if ((int) startOfWord.getY() + neighborIndex == 15) {
                return horizontalOpenSpots;
            }
            //if the tile on the right is has not been played yet.
            if (!tiles[(int) startOfWord.getX()][(int) startOfWord.getY() + neighborIndex].isTilePlayed()) {
                horizontalOpenSpots++;
                neighborIndex++;
            } else {
                hasMoreSpots = false;
            }
        }
        return horizontalOpenSpots;
    }

    /**
     * Finds the amount of vertical spaces below a given tile
     *
     * @param startOfWord
     * @return int representing the number of open tiles
     */
    private int getVerticalEmptySpaces(Point2D startOfWord) {
        Tile[][] tiles = board.getTiles();
        boolean hasMoreSpots = true;

        int verticalOpenSpots = 0;

        int neighborIndex = 1;
        while (hasMoreSpots) {
            if ((int) startOfWord.getX() + neighborIndex == 15) {
                return verticalOpenSpots;
            }
            //if the tile below is has not been played yet.
            if (!tiles[(int) startOfWord.getX() + neighborIndex][(int) startOfWord.getY()].isTilePlayed()) {
                verticalOpenSpots++;
                neighborIndex++;
            } else {
                hasMoreSpots = false;
            }
        }
        return verticalOpenSpots;
    }

    /**
     * Perform necessary updates to the words played and score board in the user
     * interface.
     */
    private void updateScoreBoardInterface() {
        //display the words that were accepted by server
        myUtility.updateWordsPlayedDisplay(wordsPlayedDisplay, newWords);

        //update the computer last score and total score
        computer.setLastScore(pointsMadeInServer);
        computer.sumTotalScore(pointsMadeInServer);
        computerLastScore.setText(String.valueOf(computer.getLastScore()));
        computerTotalScore.setText(String.valueOf(computer.getTotalScore()));
        //update the tiles left
        setNumTilesLeft(numTilesLeft - aiPlacedLetters.size());
        tilesLeft.setText(String.valueOf(numTilesLeft));
    }

    /**
     * Resets the needed global variables for future plays by the ai.
     */
    private void resetPlayVariables() {
        newWords = new ArrayList<>();
        allPossibleWords = new ArrayList<>();
        aiPlacedLetters = new ArrayList<>();
    }

    /**
     * Sending the byte[] message to server for a play and receiving the
     * response.
     */
    private boolean sendMessageToServer(List<Point2D> playPositions) {
        //create a 23 byte long message to be sent to the server
        byte[] message = myUtility.constructPlayMessage(playPositions, aiPlacedLetters);

        try {
            //send this message to the server
            out.write(message);
            //read the bytes returned from the server. The int represents how many bytes were returned (23). The actual response is stored in the message variable.
            byte[] allBytes = in.readNBytes(23);

            //if the first returned byte is 0, the word was invalid so we reset the rack and the letters placed on the board.
            if (allBytes[0] == 0) {
                invalidPlayReset(playPositions);
                return false;

            } //if the server responds saying that the first word must be on the start tile
            if (allBytes[0] == -1) {
                invalidPlayReset(playPositions);
                return false;
            }//the server returned the points made by AI, return true so we can update the user interface
            if (allBytes[0] != 0 && allBytes[0] != -1) {
                pointsMadeInServer = allBytes[0];
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
    private void invalidPlayReset(List<Point2D> playPositions) {
        //the tiles that have been given a letter will now be reset back to a null letter, since the play was not valid.
        for (Point2D point : playPositions) {
            Letter removedLetter = board.getTiles()[(int) point.getX()][(int) point.getY()].removeLetter();
        }

        int countNulls = 0;
        for (Letter letter : computer.getRack()) {
            if (letter == null) {
                countNulls++;
            }
        }
        if (countNulls == aiPlacedLetters.size()) {
            //put the players rack to the way it was before
            computer.addLettersToRack(aiPlacedLetters);
        }
    }

    /**
     * Checks if the letter to be placed is part of the rack
     *
     * @param letter
     * @param spot
     * @return true if it is part of the rack, false otherwise.
     */
    private boolean isLetterInComputerRack(char letter, Point2D spot) {
        Tile[][] tiles = board.getTiles();
        List<Letter> computerRack = computer.getRack();
        for (int i = 0; i < computerRack.size(); i++) {
            if (computerRack.get(i) != null) {
                if (computerRack.get(i).getLetter() == letter) {
                    tiles[(int) spot.getX()][(int) spot.getY()].addLetter(computerRack.get(i));
                    int[] rackPositionsToRemove = new int[1];
                    rackPositionsToRemove[0] = i;
                    //remove the letter from tha ai's rack
                    List<Letter> removedLetterFromRack = computer.removeLettersFromRack(rackPositionsToRemove);

                    //add this letter to the list of placedLetters
                    aiPlacedLetters.add(removedLetterFromRack.get(0));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Call the recursive method to find all the possible words made from the
     * rack
     *
     * @param wordLength
     */
    private void combineRackLettersIntoString(int wordLength) {
        String allLetters = "";
        for (Letter letter : computer.getRack()) {
            allLetters += letter.getLetter();
        }
        iterate(allLetters.toCharArray(), wordLength, new char[wordLength], 0);
    }

    /**
     * Recursively calls itself and comes up with all possible words from the
     * rack.
     *
     * @param chars
     * @param len
     * @param build
     * @param pos
     */
    private void iterate(char[] chars, int len, char[] build, int pos) {
        if (pos == len) {
            String word = new String(build);
            allPossibleWords.add(word);
            return;
        }

        for (int i = 0; i < chars.length; i++) {
            //need to check that the time hasn't exceeded 15 seconds since longer letter combinations have many permutations
            if (System.currentTimeMillis() >= end) {
                //end the process of recursion
                return;
            }
            build[pos] = chars[i];
            iterate(chars, len, build, pos + 1);
        }
    }
}
