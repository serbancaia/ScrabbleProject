package com.mycompany.scrabble_project.data;

/**
 * This class extends the Tile class
 * It represents a tile that doubles the value of the
 * letter placed here
 * 
 * @author Alin Caia
 */
public class DoubleLetterTile extends Tile{
    /**
     * Calls the Tile class to create a DLTile
     */
    public DoubleLetterTile(){
        super(2,1);
    }
}
