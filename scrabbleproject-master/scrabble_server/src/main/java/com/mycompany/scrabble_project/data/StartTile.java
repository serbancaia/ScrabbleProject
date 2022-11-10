package com.mycompany.scrabble_project.data;

/**
 * This class extends the Tile class
 * It represents the middle tile on the board
 * which is the tile that the first word placed
 * needs to use
 * 
 * @author Alin Caia
 */
public class StartTile extends Tile{
    /**
     * Calls the Tile class to create a StartTile
     */
    public StartTile(){
        super(1,2);
    }
}
