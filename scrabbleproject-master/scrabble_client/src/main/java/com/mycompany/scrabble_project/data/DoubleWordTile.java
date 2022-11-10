package com.mycompany.scrabble_project.data;

/**
 * This class extends the Tile class
 * It represents a tile that doubles the value of the
 * word using this tile
 * 
 * @author Alin Caia
 */
public class DoubleWordTile extends Tile{
    /**
     * Calls the Tile class to create a DWTile
     */
    public DoubleWordTile(){
        super(1,2);
    }
}
