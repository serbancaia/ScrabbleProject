package com.mycompany.scrabble_project.utility;

import com.mycompany.scrabble_project.data.Letter;
import com.mycompany.scrabble_project.data.Tile;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.IPAddress;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import me.shib.java.lib.diction.DictionService;
import me.shib.java.lib.diction.DictionWord;

/**
 * Helper methods that will be used in the rest of the program. Relates to
 * checking an IPAddress, setting an image, getting a Letter's numeric value and
 * updating the words played.
 *
 * @author Daniel Chin & Alin Caia
 */
public class MyUtility {

    public MyUtility() {
    }

    /**
     * Checks the validity of an ip address
     *
     * @param hostName
     * @return
     */
    public int IP_checker(String hostName) {
        int return_value = 0;
        if (hostName.equals("localhost")) {
            return_value = 1;
        } else {
            IPAddressString str = new IPAddressString(hostName);
            IPAddress addr = str.getAddress();
            if (addr != null) {
                return_value = 1;
            }
        }
        return return_value;
    }

    /**
     * Get the number representing the letter. Since we are passing it to the
     * server.
     *
     * @param letter
     * @return int representing a char
     */
    public int getLetterNumericRepresentaion(char letter) {
        int resultInt = 0;
        switch (letter) {
            case 'A':
                resultInt = 1;
                break;
            case 'B':
                resultInt = 2;
                break;
            case 'C':
                resultInt = 3;
                break;
            case 'D':
                resultInt = 4;
                break;
            case 'E':
                resultInt = 5;
                break;
            case 'F':
                resultInt = 6;
                break;
            case 'G':
                resultInt = 7;
                break;
            case 'H':
                resultInt = 8;
                break;
            case 'I':
                resultInt = 9;
                break;
            case 'J':
                resultInt = 10;
                break;
            case 'K':
                resultInt = 11;
                break;
            case 'L':
                resultInt = 12;
                break;
            case 'M':
                resultInt = 13;
                break;
            case 'N':
                resultInt = 14;
                break;
            case 'O':
                resultInt = 15;
                break;
            case 'P':
                resultInt = 16;
                break;
            case 'Q':
                resultInt = 17;
                break;
            case 'R':
                resultInt = 18;
                break;
            case 'S':
                resultInt = 19;
                break;
            case 'T':
                resultInt = 20;
                break;
            case 'U':
                resultInt = 21;
                break;
            case 'V':
                resultInt = 22;
                break;
            case 'W':
                resultInt = 23;
                break;
            case 'X':
                resultInt = 24;
                break;
            case 'Y':
                resultInt = 25;
                break;
            case 'Z':
                resultInt = 26;
                break;
        }
        return resultInt;
    }

    /**
     * Sets the imageView to its appropriate image depending on the letter
     *
     * @param imageView
     * @param letter
     */
    public void setTheImage(ImageView imageView, char letter) {
        switch (letter) {
            case 'A':
                imageView.setImage(new Image("/images/A.png"));
                break;
            case 'B':
                imageView.setImage(new Image("/images/B.png"));
                break;
            case 'C':
                imageView.setImage(new Image("/images/C.png"));
                break;
            case 'D':
                imageView.setImage(new Image("/images/D.png"));
                break;
            case 'E':
                imageView.setImage(new Image("/images/E.png"));
                break;
            case 'F':
                imageView.setImage(new Image("/images/F.png"));
                break;
            case 'G':
                imageView.setImage(new Image("/images/G.png"));
                break;
            case 'H':
                imageView.setImage(new Image("/images/H.png"));
                break;
            case 'I':
                imageView.setImage(new Image("/images/I.png"));
                break;
            case 'J':
                imageView.setImage(new Image("/images/J.png"));
                break;
            case 'K':
                imageView.setImage(new Image("/images/K.png"));
                break;
            case 'L':
                imageView.setImage(new Image("/images/L.png"));
                break;
            case 'M':
                imageView.setImage(new Image("/images/M.png"));
                break;
            case 'N':
                imageView.setImage(new Image("/images/N.png"));
                break;
            case 'O':
                imageView.setImage(new Image("/images/O.png"));
                break;
            case 'P':
                imageView.setImage(new Image("/images/P.png"));
                break;
            case 'Q':
                imageView.setImage(new Image("/images/Q.png"));
                break;
            case 'R':
                imageView.setImage(new Image("/images/R.png"));
                break;
            case 'S':
                imageView.setImage(new Image("/images/S.png"));
                break;
            case 'T':
                imageView.setImage(new Image("/images/T.png"));
                break;
            case 'U':
                imageView.setImage(new Image("/images/U.png"));
                break;
            case 'V':
                imageView.setImage(new Image("/images/V.png"));
                break;
            case 'W':
                imageView.setImage(new Image("/images/W.png"));
                break;
            case 'X':
                imageView.setImage(new Image("/images/X.png"));
                break;
            case 'Y':
                imageView.setImage(new Image("/images/Y.png"));
                break;
            case 'Z':
                imageView.setImage(new Image("/images/Z.png"));
                break;
        }
    }

    /**
     * When a play is valid, add the played words to the display of played words
     * along with the description and hyponym
     *
     * @param textArea
     * @param words
     */
    public void updateWordsPlayedDisplay(TextArea textArea, List<String> words) {
        DictionService dictionService = new DictionService();
        for (String word : words) {
            DictionWord newWord = dictionService.getDictionWord(word);
            if (newWord != null) {
                String description = newWord.getDescriptions().get(0).getDescription();
                List<String> hyponyms = newWord.getHyponyms();
                String newHyponym = "";

                for (String hyponum : hyponyms) {
                    newHyponym += " " + hyponum;
                }
                textArea.appendText(word + "  :  " + description + "; " + newHyponym);

                //new line
                textArea.appendText("\n");
            }

        }
    }

    /**
     * Helper method to construct a 23 byte message of the play
     *
     * Format: [info] [letters placed] [x position] [y position] [letter] ...
     *
     * @param playPositions
     * @param lettersPlaced
     * @return A 23 byte message for a play by player.
     */
    public byte[] constructPlayMessage(List<Point2D> playPositions, List<Letter> lettersPlaced) {
        //the 1st bit of the first byte is 1 since we want to play.
        byte[] message = new byte[23];

        //first byte indicates that it is a play action
        byte playMessage = (byte) 1;

        //indicates how many letters are in the play.
        byte letterCountByte = (byte) lettersPlaced.size();

        ArrayList<Byte> allBytes = new ArrayList<>();
        allBytes.add(playMessage);
        allBytes.add(letterCountByte);
        for (int i = 0; i < playPositions.size(); i++) {
            byte xPosition = (byte) playPositions.get(i).getX();
            byte yPosition = (byte) playPositions.get(i).getY();
            byte letterByte = (byte) getLetterNumericRepresentaion(lettersPlaced.get(i).getLetter());

            allBytes.add(xPosition);
            allBytes.add(yPosition);
            allBytes.add(letterByte);
        }

        int allBytesSize = allBytes.size();
        for (int i = 0; i < allBytesSize; i++) {
            message[i] = allBytes.get(i);
        }
        return message;
    }

    /**
     * Returns the ImageView from the board that we wish to reset.
     *
     * @param boardGridPane
     * @param row
     * @param col
     * @return the ImageView from the board that we wish to reset.
     */
    public ImageView getNodeFromGridPane(GridPane boardGridPane, int row, int col) {
        List<Node> allGridChildren = boardGridPane.getChildren();
        List<ImageView> boardImageViews = new ArrayList<>();
        for (Node child : allGridChildren) {
            if (child instanceof ImageView) {
                boardImageViews.add((ImageView) child);
            }
        }
        for (ImageView imageView : boardImageViews) {
            if (boardGridPane.getColumnIndex(imageView) == col && boardGridPane.getRowIndex(imageView) == row) {
                return imageView;
            }
        }
        return null;
    }

    /**
     * The play is valid, we set isPlayedTile field to true.
     *
     * @param playPositions
     * @param tiles
     */
    public void setWordTilesToPlayed(List<Point2D> playPositions, Tile[][] tiles) {
        for (Point2D point : playPositions) {
            tiles[(int) point.getX()][(int) point.getY()].setPlayedTile();
        }
    }

    /**
     * Gets all the vertical words in the board.
     *
     * @param tiles
     * @param lettersPlaced
     * @return the list of found words
     */
    public List<String> getAllVerticalWords(Tile[][] tiles, List<Letter> lettersPlaced) {
        //The list of total new words founds
        List<String> foundWords = new ArrayList<>();

        //A base to be appended to when creating a word
        StringBuilder wordString = new StringBuilder();
        boolean isNewWord = false;
        //starting position for an individual string
        int stringStart = 0;
        //end position for an individual string
        int stringEnd;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j <= tiles[i].length; j++) {
                //only start creating a word if the letter on this tile is not null (also check to see that you are not missing the last index of the column)
                if (j < tiles[i].length && tiles[j][i].getLetter() != null) {
                    if (wordString.length() == 0) {
                        stringStart = j;
                    }
                    //add this tile to the word
                    wordString.append(tiles[j][i].getLetter().getLetter());
                    //if this position has a letter who's isTilePlayed == false, then it is a new word
                    if (!tiles[j][i].isTilePlayed()) {
                        isNewWord = true;
                    }
                } else if (isNewWord && wordString.length() > 1) {
                    stringEnd = j - 1;
                    Point2D firstCoordinate = new Point2D(stringStart, i);
                    Point2D lastCoordinate = new Point2D(stringEnd, i);
                    Pair<Point2D, Point2D> positions = new Pair<>(firstCoordinate, lastCoordinate);
                    Pair<Pair<Point2D, Point2D>, String> wordAndPositions = new Pair<>(positions, wordString.toString());
                    if (isWordPartOfCurrentPlay(wordAndPositions, lettersPlaced, tiles)) {
                        foundWords.add(wordString.toString());
                    }
                    isNewWord = false;
                    wordString = new StringBuilder();
                } else {
                    wordString = new StringBuilder();
                }
            }
        }
        return foundWords;
    }

    /**
     * Gets all the horizontal words in the board.
     *
     * @param tiles
     * @param lettersPlaced
     * @return the list of found words
     */
    public List<String> getAllHorizontalWords(Tile[][] tiles, List<Letter> lettersPlaced) {
        List<String> foundWords = new ArrayList<>();

        StringBuilder wordString = new StringBuilder();
        boolean isNewWord = false;
        //starting position for an individual string
        int stringStart = 0;
        //end position for an individual string
        int stringEnd;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j <= tiles[i].length; j++) {
                //only start creating a word if the letter on this tile is not null (also check to see that you are not missing the last index of the row)
                if (j < tiles[i].length && tiles[i][j].getLetter() != null) {
                    if (wordString.length() == 0) {
                        stringStart = j;
                    }
                    //add this tile to the word
                    wordString.append(tiles[i][j].getLetter().getLetter());
                    //if this position has a letter that was played by client.
                    if (!tiles[i][j].isTilePlayed()) {
                        isNewWord = true;
                    }
                } else if (isNewWord && wordString.length() > 1) {
                    stringEnd = j - 1;
                    Point2D firstCoordinate = new Point2D(i, stringStart);
                    Point2D lastCoordinate = new Point2D(i, stringEnd);
                    Pair<Point2D, Point2D> positions = new Pair<>(firstCoordinate, lastCoordinate);
                    Pair<Pair<Point2D, Point2D>, String> wordAndPositions = new Pair<>(positions, wordString.toString());
                    if (isWordPartOfCurrentPlay(wordAndPositions, lettersPlaced, tiles)) {
                        foundWords.add(wordString.toString());
                    }
                    isNewWord = false;
                    wordString = new StringBuilder();
                } else {
                    wordString = new StringBuilder();
                }
            }
        }
        return foundWords;
    }

    /**
     * Checks if the word found is part of the current play.
     *
     * @param wordAndPositions
     * @param lettersPlaced
     * @param tiles
     * @return true if the word is part of the play, false otherwise.
     */
    private boolean isWordPartOfCurrentPlay(Pair<Pair<Point2D, Point2D>, String> wordAndPositions, List<Letter> lettersPlaced, Tile[][] tiles) {
        for (int i = 0; i < wordAndPositions.getValue().length(); i++) {
            boolean isMatch = false;
            for (Letter letter : lettersPlaced) {
                if (letter.getLetter() == wordAndPositions.getValue().charAt(i)) {
                    if (wordAndPositions.getKey().getKey().getX() == wordAndPositions.getKey().getValue().getX()) {
                        for (int j = (int) wordAndPositions.getKey().getKey().getY(); j <= (int) wordAndPositions.getKey().getValue().getY(); j++) {
                            if (tiles[(int) wordAndPositions.getKey().getKey().getX()][j].getLetter() == letter) {
                                if (!tiles[(int) wordAndPositions.getKey().getKey().getX()][j].isTilePlayed()) {
                                    isMatch = true;
                                }
                            }
                        }
                    } else if (wordAndPositions.getKey().getKey().getY() == wordAndPositions.getKey().getValue().getY()) {
                        for (int j = (int) wordAndPositions.getKey().getKey().getX(); j <= (int) wordAndPositions.getKey().getValue().getX(); j++) {
                            if (tiles[j][(int) wordAndPositions.getKey().getKey().getY()].getLetter() == letter) {
                                if (!tiles[j][(int) wordAndPositions.getKey().getKey().getY()].isTilePlayed()) {
                                    isMatch = true;
                                }
                            }
                        }
                    }
                }
            }
            if (isMatch) {
                return true;
            }
        }
        return false;
    }
}
