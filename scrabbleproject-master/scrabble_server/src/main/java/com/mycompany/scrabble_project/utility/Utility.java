package com.mycompany.scrabble_project.utility;

import com.mycompany.scrabble_project.data.Board;
import com.mycompany.scrabble_project.data.DoubleWordTile;
import com.mycompany.scrabble_project.data.StartTile;
import com.mycompany.scrabble_project.data.Tile;
import com.mycompany.scrabble_project.data.TripleWordTile;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.util.Pair;
import me.shib.java.lib.diction.DictionService;

/**
 * Methods used in the server to get the letter representations of an int,
 * calculate points, check if a tile has neighbors
 *
 * @author Daniel Chin and Alin Caia
 */
public class Utility {

    private DictionService dictionService;

    /**
     * Instantiates a DictionService object which will continue to be used while
     * this thread is running
     */
    public Utility() {
        dictionService = new DictionService();
    }

    /**
     * Gets the letter char representation of a number.
     *
     * @param number
     * @return char representing the int number
     */
    public char getLetterRepresentation(int number) {
        char resultChar = ' ';
        switch (number) {
            case 1:
                resultChar = 'A';
                break;
            case 2:
                resultChar = 'B';
                break;
            case 3:
                resultChar = 'C';
                break;
            case 4:
                resultChar = 'D';
                break;
            case 5:
                resultChar = 'E';
                break;
            case 6:
                resultChar = 'F';
                break;
            case 7:
                resultChar = 'G';
                break;
            case 8:
                resultChar = 'H';
                break;
            case 9:
                resultChar = 'I';
                break;
            case 10:
                resultChar = 'J';
                break;
            case 11:
                resultChar = 'K';
                break;
            case 12:
                resultChar = 'L';
                break;
            case 13:
                resultChar = 'M';
                break;
            case 14:
                resultChar = 'N';
                break;
            case 15:
                resultChar = 'O';
                break;
            case 16:
                resultChar = 'P';
                break;
            case 17:
                resultChar = 'Q';
                break;
            case 18:
                resultChar = 'R';
                break;
            case 19:
                resultChar = 'S';
                break;
            case 20:
                resultChar = 'T';
                break;
            case 21:
                resultChar = 'U';
                break;
            case 22:
                resultChar = 'V';
                break;
            case 23:
                resultChar = 'W';
                break;
            case 24:
                resultChar = 'X';
                break;
            case 25:
                resultChar = 'Y';
                break;
            case 26:
                resultChar = 'Z';
                break;
        }
        return resultChar;
    }

    /**
     * Takes a list of int coordinates and converts it into simpler Point2D
     * Objects
     *
     * @param coordinates
     * @return list of Point2D objects representing the letter's position on the
     * board
     */
    public ArrayList<Point2D> convertToPoint2DList(List<Integer> coordinates) {
        ArrayList<Point2D> resultList = new ArrayList<>();
        int currentPosition = 0;

        while (currentPosition < coordinates.size()) {
            resultList.add(new Point2D(coordinates.get(currentPosition), coordinates.get(currentPosition + 1)));
            currentPosition += 2;
        }
        return resultList;
    }

    /**
     * Checks if a letter on the tile is connected to another letter which is
     * not a playedTile already
     *
     * @param theTiles
     * @param coordinates
     * @return true if it has a neighbor, false otherwise.
     */
    public boolean checkTileHasNeighbor(Tile[][] theTiles, Point2D coordinates) {
        int x = (int) coordinates.getX();
        int y = (int) coordinates.getY();
        Tile north = new Tile();
        Tile east = new Tile();
        Tile south = new Tile();
        Tile west = new Tile();

        if ((x - 1) >= 0) {
            if (theTiles[x - 1][y].isTilePlayed()) {
                north = theTiles[x - 1][y];
            }
        }

        if ((y + 1) <= 14) {
            if (theTiles[x][y + 1].isTilePlayed()) {
                east = theTiles[x][y + 1];
            }
        }

        if ((x + 1 <= 14)) {
            if (theTiles[x + 1][y].isTilePlayed()) {
                south = theTiles[x + 1][y];
            }
        }

        if ((y - 1) >= 0) {
            if (theTiles[x][y - 1].isTilePlayed()) {
                west = theTiles[x][y - 1];
            }
        }

        if (north.getLetter() == null && east.getLetter() == null && south.getLetter() == null && west.getLetter() == null) {
            //this tile has no neighbors
            return false;
        }
        return true;
    }

    /**
     * Checks that all the new words formed after the play are valid dictionary
     * words.
     *
     * @param newWords
     * @return true if they are all valid, false otherwise.
     */
    public boolean checkNewWordsValidity(List<ArrayList<Tile>> newWords) {
        if (newWords.isEmpty()) {
            return false;
        }

        for (int i = 0; i < newWords.size(); i++) {
            StringBuilder word = new StringBuilder();
            for (int j = 0; j < newWords.get(i).size(); j++) {
                word.append(newWords.get(i).get(j).getLetter().getLetter());
            }

            //if the formed word is not a valid dictionary word, return false
            if (dictionService.getDictionWord(word.toString()) == null) {
                return false;
            }
        }
        //all new words are in the dictionary, so we return true
        return true;
    }

    /**
     * Iterate through each word and calculate the points that the word
     * generates.
     *
     * @param newWords
     * @return the total points made from the play.
     */
    public int calculatePlayPoints(List<ArrayList<Tile>> newWords) {

        int totalPoints = 0;

        for (int i = 0; i < newWords.size(); i++) {
            int wordPoints = 0;
            int overallWordMultiplier = getOverallWordMultiplier(newWords.get(i));
            //we only want to count the points if the word contains an at least one unplayed tile. Previously found words will not be counted.
            if (doesWordContainUnplayedTile(newWords.get(i))) {
                for (int j = 0; j < newWords.get(i).size(); j++) {
                    Tile tile = newWords.get(i).get(j);

                    if (tile.isTilePlayed()) {
                        wordPoints += tile.getLetter().getValue();
                    } else {
                        wordPoints += (tile.getLetterMultip() * tile.getLetter().getValue());
                    }

                }
                totalPoints += (wordPoints * overallWordMultiplier);
            }
        }

        if (newWords.size() == 7) {
            totalPoints += 50;
        }
        return totalPoints;
    }

    /**
     * Checks if a word contains an unplayed tile, if so, then we will count the
     * word's points
     *
     * @param word
     * @return true if one of the letters is an unplayed tile
     */
    private boolean doesWordContainUnplayedTile(ArrayList<Tile> word) {
        for (Tile letter : word) {
            if (!letter.isTilePlayed()) {
                return true;
            }
        }
        return false;
    }

    /**
     * A word can span over 1 or more word multipliers. This method calculates
     * the overall multiplier.
     *
     * @param word
     * @return the overall multiplier.
     */
    private int getOverallWordMultiplier(ArrayList<Tile> word) {
        int multiplier = 1;

        int doubleWordMultipliers = 0;
        int tripleWordMultipliers = 0;

        for (Tile tile : word) {
            if (tile instanceof DoubleWordTile && !tile.isTilePlayed()) {
                doubleWordMultipliers++;
            } else if (tile instanceof StartTile && !tile.isTilePlayed()) {
                doubleWordMultipliers++;
            } else if (tile instanceof TripleWordTile && !tile.isTilePlayed()) {
                tripleWordMultipliers++;
            }
        }

        if (doubleWordMultipliers != 0) {
            multiplier *= (2 * doubleWordMultipliers);
        }

        if (tripleWordMultipliers != 0) {
            multiplier *= (3 * tripleWordMultipliers);
        }
        return multiplier;
    }

    /**
     * Checks if the board has a word placed in it yet.
     *
     * @param tiles
     * @return true if a tile isPlayed = true, false otherwise.
     */
    public boolean boardHasPlayedTile(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isTilePlayed()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks whether the human player who's performing the first
     * play on the board has placed only one word either on the row or column
     * where the start tile is. The star tile needs to hold a letter in order
     * for the play to be valid
     *
     * @param board
     * @return true or false
     */
    public boolean checkFirstPlayValid(Board board) {
        //get all the row and column indices that hold at least 1 letter
        ArrayList<Integer> usedRows = findUsedRows(board);
        ArrayList<Integer> usedColumns = findUsedColumns(board);
        //check if the player put a word horizontally only on the row with the start tile (and on the start tile)
        if (usedRows.size() == 1 && usedRows.get(0) == 7) {
            //check if the player put more than one letter
            if (usedColumns.size() > 1) {
                //check if the row with the start tile truly has letters that are neighbored and form only one word
                if (findStringsInRow(board, usedRows.get(0)).size() == 1) {
                    return true;
                }
            }
        } //check if the player put a word vertically only on the column with the start tile (and on the start tile)
        else if (usedColumns.size() == 1 && usedColumns.get(0) == 7) {
            //check if the player put more than one letter
            if (usedRows.size() > 1) {
                //check if the column with the start tile truly has letters that are neighbored and form only one word
                if (findStringsInColumn(board, usedColumns.get(0)).size() == 1) {
                    return true;
                }
            }
        }
        //return false if neither one of the above options was met
        return false;
    }

    /**
     * This method checks how many rows have at least one letter on them Uses
     * deep copy
     *
     * @param board
     * @return the arraylist of indices of rows with letters
     */
    private ArrayList<Integer> findUsedRows(Board board) {
        ArrayList<Integer> usedRows = new ArrayList<>();
        ArrayList<Tile[]> allRows = board.getAllRows();
        allRows.forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                if (row[i].getLetter() != null) {
                    usedRows.add(i);
                    break;
                }
            }
        });
        return usedRows;
    }

    /**
     * This method checks how many columns have at least one letter on them Uses
     * deep copy
     *
     * @param board
     * @return the arraylist of indices of columns with letters
     */
    private ArrayList<Integer> findUsedColumns(Board board) {
        ArrayList<Integer> usedColumns = new ArrayList<>();
        ArrayList<Tile[]> allColumns = board.getAllColumns();
        allColumns.forEach(column -> {
            for (int i = 0; i < column.length; i++) {
                if (column[i].getLetter() != null) {
                    usedColumns.add(i);
                    break;
                }
            }
        });
        return usedColumns;
    }

    /**
     * This method analyses the board by finding all of the individual strings
     * found in a specific row
     *
     * Point2D: x = column index where string starts , y = column index where
     * string ends String: a continuous string of Letters found in a row
     * ArrayList<Pair>: all continuous strings, along with their column
     * positions, found on that row
     *
     * @param board
     * @param rowNb
     * @return the arraylist of all continuous strings, along with their column
     * positions, found on the inputted row
     */
    private ArrayList<Pair<Point2D, String>> findStringsInRow(Board board, int rowNb) {
        ArrayList<Pair<Point2D, String>> rowStrings = new ArrayList<>();
        Tile[] row = board.getRow(rowNb);
        //string container
        String string = "";
        //starting position for an individual string
        int stringStart = 0;
        //end position for an individual string
        int stringEnd;
        Point2D stringStartEndIndices;
        for (int i = 0; i < row.length; i++) {
            //check if the current tile being checked has a letter
            while (i < row.length && row[i].getLetter() != null) {
                //give the starting position of the string if the string container is empty
                if (string.isEmpty()) {
                    stringStart = i;
                }
                //add the letter to the string container and move on to the next tile
                string += row[i].getLetter();
                i++;
            }
            //if reached the end of an indivual string (current tile is the one after the last tile containing a letter)
            if (!string.isEmpty()) {
                //give the end position of the string (the tile before the current one)
                stringEnd = i - 1;
                //add the word reference to the list
                stringStartEndIndices = new Point2D(stringStart, stringEnd);
                rowStrings.add(new Pair<>(stringStartEndIndices, string));
                //empty the string container
                string = "";
            }
        }
        return rowStrings;
    }

    /**
     * This method analyses the board by finding all of the individual strings
     * found in a specific column
     *
     * Point2D: x = row index where string starts , y = row index where string
     * ends String: a continuous string of Letters found in a column
     * ArrayList<Pair>: all continuous strings, along with their row positions,
     * found on that column
     *
     * @param board
     * @param columnNb
     * @return the arraylist of all continuous strings, along with their row
     * positions, found on the inputted column
     */
    private ArrayList<Pair<Point2D, String>> findStringsInColumn(Board board, int columnNb) {
        ArrayList<Pair<Point2D, String>> columnStrings = new ArrayList<>();
        Tile[] column = board.getColumn(columnNb);
        //string container
        String string = "";
        //starting position for an individual string
        int stringStart = 0;
        //end position for an individual string
        int stringEnd;
        Point2D stringStartEndIndices;
        for (int i = 0; i < column.length; i++) {
            //check if the current tile being checked has a letter
            while (i < column.length && column[i].getLetter() != null) {
                //give the starting position of the string if the string container is empty
                if (string.isEmpty()) {
                    stringStart = i;
                }
                //add the letter to the string container and move on to the next tile
                string += column[i].getLetter();
                i++;
            }
            //if reached the end of an indivual string (current tile is the one after the last tile containing a letter)
            if (!string.isEmpty()) {
                //give the end position of the string (the tile before the current one)
                stringEnd = i - 1;
                //add the word reference to the list
                stringStartEndIndices = new Point2D(stringStart, stringEnd);
                columnStrings.add(new Pair<>(stringStartEndIndices, string));
                //empty the string container
                string = "";
            }
        }
        return columnStrings;
    }

    /**
     * Checks that all the placed letters from a player are all connected in a
     * single line. A player cannot try to play multiple words. Play must be
     * either on same row or column.
     *
     * @param coordinates
     * @param tiles
     * @return true if the play is a single word, false otherwise.
     */
    public boolean isPlayOneSingleWord(List<Point2D> coordinates, Tile[][] tiles) {
        //if one letter was placed,
        if (coordinates.size() == 1) {
            return true;
        }
        //find out if the coordinates are either on the same row or column
        int rowIndex = (int) coordinates.get(0).getX();
        int colIndex = (int) coordinates.get(0).getY();

        boolean sameRow = true;
        boolean sameCol = true;

        for (Point2D coordinate : coordinates) {
            if ((int) (coordinate.getX()) != rowIndex) {
                sameRow = false;
                break;
            }
        }

        for (Point2D coordinate : coordinates) {
            if ((int) (coordinate.getY()) != colIndex) {
                sameCol = false;
                break;
            }
        }

        if (sameRow && sameCol) {
            return false;
        }

        int validCountVertical = 0;
        int validCountHorizontal = 0;

        if (sameCol) {
            //sort the word vertically
            Collections.sort(coordinates, Comparator.comparingDouble(Point2D::getX));
            for (int i = 0; i < coordinates.size(); i++) {
                if (i + 1 < coordinates.size()) {
                    //is this coordinate the direct vertical neighbor of the one below.
                    if (coordinates.get(i).getX() == coordinates.get(i + 1).getX() - 1) {
                        validCountVertical++;
                        //if there is a gap in between a couple of the just placed letters, find out whether the gap is filled with letters and no empty tiles
                    } else {
                        double tilesInBetween = coordinates.get(i + 1).getX() - coordinates.get(i).getX() - 1;
                        int tilesWithLetters = 0;
                        for (int j = 1; j <= tilesInBetween; j++) {
                            if (tiles[(int) coordinates.get(i + j).getX()][(int) coordinates.get(i).getY()].getLetter() != null && tiles[(int) coordinates.get(i + j).getX()][(int) coordinates.get(i).getY()].isTilePlayed()) {
                                tilesWithLetters++;
                            } //if there is a blank tile between two placed letters, the play is invalid since a player can only place one word on the board
                            else {
                                return false;
                            }
                        }
                        if (tilesInBetween == tilesWithLetters) {
                            validCountVertical++;
                        }
                    }
                }
            }
        } else if (sameRow) {
            //sort the word horizontally
            Collections.sort(coordinates, Comparator.comparingDouble(Point2D::getY));
            for (int i = 0; i < coordinates.size(); i++) {
                if (i + 1 < coordinates.size()) {
                    //is this coordinate the direct horizontal neighbor of the one below.
                    if (coordinates.get(i).getY() == coordinates.get(i + 1).getY() - 1) {
                        validCountHorizontal++;
                        //if there is a gap in between a couple of the just placed letters, find out whether the gap is filled with letters and no empty tiles
                    } else {
                        double tilesInBetween = coordinates.get(i + 1).getY() - coordinates.get(i).getY() - 1;
                        int tilesWithLetters = 0;
                        for (int j = 1; j <= tilesInBetween; j++) {
                            if (tiles[(int) coordinates.get(i).getX()][(int) coordinates.get(i + j).getY()].getLetter() != null && tiles[(int) coordinates.get(i).getX()][(int) coordinates.get(i + j).getY()].isTilePlayed()) {
                                tilesWithLetters++;
                            } //if there is a blank tile between two placed letters, the play is invalid since a player can only place one word on the board
                            else {
                                return false;
                            }
                        }
                        if (tilesInBetween == tilesWithLetters) {
                            validCountHorizontal++;
                        }
                    }
                }
            }
        }
        return ((sameRow || sameCol) && (coordinates.size() - 1 == validCountVertical || coordinates.size() - 1 == validCountHorizontal));
    }
}
