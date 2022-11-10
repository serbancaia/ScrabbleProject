package com.mycompany.scrabble_project.scrabble_server;

import com.mycompany.scrabble_project.data.Board;
import com.mycompany.scrabble_project.data.Letter;
import com.mycompany.scrabble_project.data.Tile;
import com.mycompany.scrabble_project.utility.Utility;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * The core logic of the Server side, where a client's message is interpreted as
 * either a start game request or a play by client. Validates a received message
 * and either responds with a game start response, the points made from a play,
 * or a byte message representing if the play was invalid.
 *
 * @author Daniel
 */
public class ServerLogic {

    private Tile[][] tiles;

    private Board board;

    private Utility utility;

    private final int BUFSIZE = 23;   // Size of receive buffer

    //This will contain all new words played on the board. We will check if they are valid and then calculate the points
    private List<ArrayList<Tile>> newWords;

    public ServerLogic() {
        board = new Board();
        utility = new Utility();
        newWords = new ArrayList<>();
        tiles = board.getTiles();
    }

    public byte[] validateClientMessage(byte[] clientMessage) {
        byte[] response = new byte[BUFSIZE];
        //first check if it is a start message. Since the client will never send a message which has the first two bytes 0, then it must be a gameStart message.
        if (clientMessage[0] == 0 && clientMessage[1] == 0) {
            byte[] message = "ready to play".getBytes();
            for (int i = 0; i < message.length; i++) {
                response[i] = message[i];
            }

            return response;
        }

        //if it isn't a start game request, then it must be a play message
        response = checkIfPlayMessage(clientMessage);

        return response;
    }

    /**
     * This method will check if it is a play Message. If the play is accepted,
     * then we return a byte message with the number of points made.
     *
     * @param in
     * @param out
     * @param receiveBuf
     */
    private byte[] checkIfPlayMessage(byte[] allBytes) {
        byte[] response = new byte[BUFSIZE];

        //Set the board with the new letters from the client. This will be reverted if the word(s) is invalid
        List<Point2D> coordinateList = setBoardLetters(allBytes); //the coordinate list is the points on the board that had a letter added for a play.

        getAllVerticalWords();
        getAllHorizontalWords();

        boolean isConnected = false;

        //check if there are any playedTiles on the board yet.
        boolean boardHasWords = utility.boardHasPlayedTile(tiles);
        if (!boardHasWords) {
            //if there are no words yet, check that the middle tile has a letter.
            if (tiles[7][7].getLetter() == null) {
                //return a byte array with the first byte -1 since the first word played must be on the start tile
                response[0] = (byte) -1;

                //the letters that were placed on the board are now going to be reset to how they were. (no letter).
                removeTileLetters(coordinateList);
                //reset the newWords for future plays
                newWords = new ArrayList<>();

            } //if one of the placed letters is on the start, then check that it is the only word on the board.
            else {
                //check that the play is legal. Also check that the first word does not consist of 1 letter.
                if (utility.isPlayOneSingleWord(coordinateList, tiles) && coordinateList.size() > 1) {
                    //perform the check for validity of words
                    if (utility.checkNewWordsValidity(newWords)) {
                        response = createValidWordMessage();
                    } else {
                        response = createInvalidWordMessage(coordinateList);
                    }
                } else {
                    response = createInvalidWordMessage(coordinateList);
                }
            }
        } //if the board does have words, we need to check that the new letters placed are connected to at least 1 played tile.
        else {
            for (Point2D point : coordinateList) {
                if (utility.checkTileHasNeighbor(tiles, point)) {
                    //at least one of the new letters placed has a neighbor which isn't a letter of the current play.
                    isConnected = true;
                }
            }
            //check if all words are valid, and that the newly placed letters are connected to a playedTile.
            if (utility.checkNewWordsValidity(newWords) && isConnected && utility.isPlayOneSingleWord(coordinateList, tiles) && coordinateList.size() >= 1) {
                response = createValidWordMessage();
            } else {
                response = createInvalidWordMessage(coordinateList);
            }
        }

        //reset the newWords for future plays
        newWords = new ArrayList<>();

        return response;
    }

    /**
     * Write the response message to the OutputStream and sets all the tiles'
     * isPlayed field to true. The message consists of how many points were made
     * in the play.
     *
     * @param out
     */
    private byte[] createValidWordMessage() {
        int totalPoints = utility.calculatePlayPoints(newWords);

        //set the tile's isTilePlayed field to true, since the tile has now been officially played.
        setWordTilesToPlayed();

        //respond to the client with the amount of points made.
        byte[] playResponse = new byte[BUFSIZE];
        playResponse[0] = (byte) totalPoints;

        return playResponse;

    }

    /**
     * The message consists of all 0 bytes and the helper method called will
     * remove all the unplayed tiles from the board.
     *
     * @param out
     * @param coordinateList
     */
    private byte[] createInvalidWordMessage(List<Point2D> coordinateList) {
        //returning a byte array with all 0 since a word has not been formed.
        byte[] playResponse = new byte[BUFSIZE];

        //the letters that were placed on the board are now going to be reset to how they were. (no letter).
        removeTileLetters(coordinateList);

        return playResponse;
    }

    /**
     * Takes the 23 byte message and translates it into the board containing the
     * proper letters at the proper positions.
     *
     * @param allBytes
     */
    private List<Point2D> setBoardLetters(byte[] allBytes) {
        //BASIC FORMAT OF A 23 BYTE MESSAGE.
        // PLAY || LETTERSPLAYED || XPOSITION || Y POSITION  || LETTER  || XPOSITION || Y POSITION || LETTER ...

        int lettersPlayed = allBytes[1];

        //The X and Y positions of the letters played
        List<Integer> coordinateList = new ArrayList<>();
        //The numeric representations of the letters played
        List<Integer> letterList = new ArrayList<>();

        //start at 2 , since we do not need the first two bytes indicating the a play message and the number of letters played
        int currentPosition = 2;

        //the end of the message will be 2 + (3*LettersPlayed) - 1
        //we stop reading at this position (inclusive).
        int endOfMessage = 2 + (3 * lettersPlayed) - 1;

        while (currentPosition <= endOfMessage) {
            //Adding this X coordinate to the coordinate list
            coordinateList.add((int) allBytes[currentPosition]);
            //Adding this Y coordinate to the coordinate list
            coordinateList.add((int) allBytes[currentPosition + 1]);
            //Adding this letter into the letter list
            letterList.add((int) allBytes[currentPosition + 2]);
            //Moving onto the next set of info for a single letter's position and value
            currentPosition += 3;
        }

        int coordinateListPosition = 0;
        int letterListPosition = 0;

        while (coordinateListPosition < coordinateList.size()) {
            //The tile's x coordinate
            int xPos = coordinateList.get(coordinateListPosition);
            //The tile's y coordinate
            int yPos = coordinateList.get(coordinateListPosition + 1);
            //The tile at that specific position
            Tile tile = tiles[xPos][yPos];

            //Adding that letter to the tile
            tile.addLetter(new Letter(utility.getLetterRepresentation(letterList.get(letterListPosition))));

            //We increment two since we move onto the next set of coordinates (X,Y)
            coordinateListPosition += 2;
            //Increment 1 since we want the next letter
            letterListPosition++;
        }
        return utility.convertToPoint2DList(coordinateList);
    }

    /**
     * All the changes made to the board's tile will be undone since the message
     * was invalid. The letters added to the board in the setBoardLetters are
     * removed.
     *
     * @param coordinates
     */
    private void removeTileLetters(List<Point2D> coordinates) {
        for (Point2D point : coordinates) {
            Letter removedLetter = tiles[(int) point.getX()][(int) point.getY()].removeLetter();
        }
    }

    /**
     * The play is valid, we set isPlayedTile field to true.
     */
    private void setWordTilesToPlayed() {
        for (int i = 0; i < newWords.size(); i++) {
            for (int j = 0; j < newWords.get(i).size(); j++) {
                newWords.get(i).get(j).setPlayedTile();
            }
        }
    }

    /**
     * Get and store all the vertical words found on the board.
     */
    private void getAllVerticalWords() {
        ArrayList<Tile> word = new ArrayList<>();
        boolean isNewWord = false;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[j][i].getLetter() != null) {
                    //add this tile to the word
                    word.add(tiles[j][i]);
                    //if this position has a letter that was played by client.
                    if (!tiles[j][i].isTilePlayed()) {
                        isNewWord = true;
                    }
                } else if (isNewWord && word.size() > 1) {
                    newWords.add(word);
                    isNewWord = false;
                    word = new ArrayList<>();
                } else {
                    word = new ArrayList<>();
                }
            }
        }
    }

    /**
     * Get and store all the horizontal words found on the board.
     */
    private void getAllHorizontalWords() {
        ArrayList<Tile> word = new ArrayList<>();
        boolean isNewWord = false;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].getLetter() != null) {
                    //add this tile to the word
                    word.add(tiles[i][j]);
                    //if this position has a letter that was played by client.
                    if (!tiles[i][j].isTilePlayed()) {
                        isNewWord = true;
                    }
                } else if (isNewWord && word.size() > 1) {
                    newWords.add(word);
                    isNewWord = false;
                    word = new ArrayList<>();
                } else {
                    word = new ArrayList<>();
                }
            }
        }
    }
}
