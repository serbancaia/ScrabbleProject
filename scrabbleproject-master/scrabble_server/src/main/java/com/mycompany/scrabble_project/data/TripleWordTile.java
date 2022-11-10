package com.mycompany.scrabble_project.data;

/**
 * This class extends the Tile class
 * It represents a tile that triples the value of the
 * word using this tile
 * 
 * @author Alin Caia
 */
public class TripleWordTile extends Tile{
    /**
     * Calls the Tile class to create a TWTile
     */
    public TripleWordTile(){
        super(1,3);
    }
}
