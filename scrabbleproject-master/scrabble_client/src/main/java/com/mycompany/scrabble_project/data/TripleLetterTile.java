package com.mycompany.scrabble_project.data;

/**
 * This class extends the Tile class
 * It represents a tile that triples the value of the
 * letter placed here
 * 
 * @author Alin Caia
 */
public class TripleLetterTile extends Tile{
    /**
     * Calls the Tile class to create a TLTile
     */
    public TripleLetterTile(){
        super(3,1);
    }
}
