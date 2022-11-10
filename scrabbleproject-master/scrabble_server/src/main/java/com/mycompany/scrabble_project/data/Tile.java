package com.mycompany.scrabble_project.data;

/**
 * This class represents one of the tiles on 
 * the board that is used to hold a letter
 * 
 * @author Alin Caia
 */
public class Tile {

    private Letter letter = null;
    private int letterMultip;
    private int wordMultip;
    private boolean isTilePlayed = false;
    
    /**
     * Default Constructor
     * a normal tile won't double or triple a letter or word value
     */
    public Tile() {
        this.letterMultip = 1;
        this.wordMultip = 1;
    }

    /**
     * Constructor for the child classes of Tile
     * 
     * @param letterMultip the letter multiplier
     * @param wordMultip the word multiplier
     */
    public Tile(int letterMultip, int wordMultip) {
        this.letterMultip = letterMultip;
        this.wordMultip = wordMultip;
    }

    /**
     * Getter for the Letter field letter
     * 
     * @return the letter
     */
    public Letter getLetter() {
        return letter;
    }

    /**
     * Getter for the int field letterMultip
     * 
     * @return the letter multiplier
     */
    public int getLetterMultip() {
        return letterMultip;
    }

    /**
     * Getter for the int field wordMultip
     * 
     * @return the word multiplier
     */
    public int getWordMultip() {
        return wordMultip;
    }

    /**
     * This method is used to put a letter on the tile
     * 
     * @param letterToAdd the letter selected (a Letter object)
     */
    public void addLetter(Letter letterToAdd) {
        this.letter = letterToAdd;
    }

    /**
     * This method is used to remove a letter placed on a tile
     * 
     * @return the letter that was removed
     */
    public Letter removeLetter() {
        Letter letterToRemove = this.letter;
        this.letter = null;
        return letterToRemove;
    }
    
    /**
     * This method checks whether the tile has been played before
     * 
     * @return true or false
     */
    public boolean isTilePlayed(){
        return this.isTilePlayed;
    }
    
    /**
     * Setter for the boolean field isTilePlayed 
     */
    public void setPlayedTile(){
        this.isTilePlayed = true;
    }
}
