package com.mycompany.scrabble_project.data;

import java.util.ArrayList;

/**
 * This class represents the board in a Scrabble game
 * A board holds a 2D array of Tile objects
 * 
 * @author Alin Caia & Asli Zeybek
 */
public class Board {
    private Tile[][] tiles;
    
    /**
     * Default Constructor
     */
    public Board(){
        this.tiles = new Tile[15][15];
        this.addTilesToBoard();
    }

    /**
     * Getter for the 2D array of Tile objects
     * uses deep-copy
     * 
     * @return the 2D array of Tile objects
     */
    public Tile[][] getTiles() {
       return tiles;
    }
    
    /**
     * This method is used to add a tile at a
     * specific index in the 2D array of Tiles
     * 
     * @param tileToAdd
     * @param x row index
     * @param y column index
     */
    private void addTile(Tile tileToAdd, int x, int y){
        this.tiles[x][y] = tileToAdd;
    }
    
    /**
     * Getter for a specific tile on the 2D array of Tiles
     * uses deep-copy
     * 
     * @param x row index
     * @param y column index
     * @return the desired tile on the board
     */
    public Tile getTile(int x, int y){
        return tiles[x][y];
    }
    
    /**
     * Getter for a specific row on the board
     * uses deep-copy
     * 
     * @param x row index
     * @return the desired row on the board
     */
    public Tile[] getRow(int x){
        Tile[] copyRow = new Tile[this.tiles[x].length];
        for(int y = 0; y < this.tiles[x].length; y++){
            if(this.tiles[x][y] instanceof StartTile){
                copyRow[y] = new StartTile();
            }
            else if(this.tiles[x][y] instanceof DoubleLetterTile){
                copyRow[y] = new DoubleLetterTile();
            }
            else if(this.tiles[x][y] instanceof TripleLetterTile){
                copyRow[y] = new TripleLetterTile();
            }
            else if(this.tiles[x][y] instanceof DoubleWordTile){
                copyRow[y] = new DoubleWordTile();
            }
            else if(this.tiles[x][y] instanceof TripleWordTile){
                copyRow[y] = new TripleWordTile();
            }
            else{
                copyRow[y] = new Tile(this.tiles[x][y].getLetterMultip(), this.tiles[x][y].getWordMultip());
            }
            if(this.tiles[x][y].getLetter() != null){
                copyRow[y].addLetter(new Letter(this.tiles[x][y].getLetter().getLetter()));
            }
        }
        return copyRow;
    }
    
    /**
     * Getter for specific rows. 
     * Calls getRow in bulk
     * 
     * @param rowNbs desired row indices
     * @return the desired rows on the board
     */
    public ArrayList<Tile[]> getRows(int[] rowNbs){
        ArrayList<Tile[]> rows = new ArrayList<>();
        for(int i = 0; i < rowNbs.length; i++){
            rows.add(getRow(rowNbs[i]));
        }
        return rows;
    }
    
    /**
     * Getter for all the rows on the board
     * Calls getRows with all possible indices
     * 
     * @return all rows on the board
     */
    public ArrayList<Tile[]> getAllRows(){
        return getRows(new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14});
    }
    
    /**
     * Getter for a specific column on the board
     * uses deep-copy
     * 
     * @param y column index
     * @return the desired column on the board
     */
    public Tile[] getColumn(int y){
        Tile[] copyColumn = new Tile[15];
        for(int x = 0; x < tiles.length; x++){
            if(this.tiles[x][y] instanceof StartTile){
                copyColumn[y] = new StartTile();
            }
            else if(this.tiles[x][y] instanceof DoubleLetterTile){
                copyColumn[y] = new DoubleLetterTile();
            }
            else if(this.tiles[x][y] instanceof TripleLetterTile){
                copyColumn[y] = new TripleLetterTile();
            }
            else if(this.tiles[x][y] instanceof DoubleWordTile){
                copyColumn[y] = new DoubleWordTile();
            }
            else if(this.tiles[x][y] instanceof TripleWordTile){
                copyColumn[y] = new TripleWordTile();
            }
            else{
                copyColumn[y] = new Tile(this.tiles[x][y].getLetterMultip(), this.tiles[x][y].getWordMultip());
            }
            if(this.tiles[x][y].getLetter() != null){
                copyColumn[y].addLetter(new Letter(this.tiles[x][y].getLetter().getLetter()));
            }
        }
        return copyColumn;
    }
    
    /**
     * Getter for specific columns. 
     * Calls getColumn in bulk
     * 
     * @param columnNbs desired column indices
     * @return the desired columns on the board
     */
    public ArrayList<Tile[]> getColumns(int[] columnNbs){
        ArrayList<Tile[]> columns = new ArrayList<>();
        for(int i = 0; i < columnNbs.length; i++){
            columns.add(getColumn(columnNbs[i]));
        }
        return columns;
    }
    
    /**
     * Getter for all the columns on the board
     * Calls getColumns with all possible indices
     * 
     * @return all columns on the board
     */
    public ArrayList<Tile[]> getAllColumns(){
        return getColumns(new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14});
    }
    
    /**
     * This method is used to add tiles to the board.
     * 
     */
    private void addTilesToBoard()
    {
        //Initializing positions of tiles
        int[][] DLTiles = {{0,1},{0,13},{3,6},{3,8},{4,5},{4,9},{5,4},{5,10},{6,3},{6,11},
            {8,3},{8,11},{9,4},{9,10},{10,5},{10,9},{11,6},{11,8},{14,1},{14,13}};
        int[][] DWTiles = {{0,7},{1,1},{1,13},{3,5},{3,9},{5,3},{5,11},{7,0},{7,4},{7,10},
            {7,14},{9,3},{9,11},{11,5},{11,9},{13,1},{13,13},{14,7}};
        int[][] TLTiles = {{0,5},{0,9},{2,3},{2,11},{3,2},{3,12},{4,1},{4,13},{5,0},{5,6},
            {5,8},{5,14},{9,0},{9,6},{9,8},{9,14},{10,1},{10,13},{11,2},{11,12},{12,3},{12,11},{14,5},{14,9}};
        int[][] TWTiles = {{0,4},{0,10},{1,0},{1,14},{13,0},{13,14},{14,4},{14,10}};
        
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (isDLTile(DLTiles, i, j))
                {
                    this.addTile(new DoubleLetterTile(), i, j);
                }
                else if (isDWTile(DWTiles, i, j))
                {
                    this.addTile(new DoubleWordTile(), i, j);
                }
                else if (isTLTile(TLTiles, i, j))
                {
                    this.addTile(new TripleLetterTile(), i, j);
                }
                else if (isTWTile(TWTiles, i, j))
                {
                    this.addTile(new TripleWordTile(), i, j);
                }
                else if (isStartTile(i, j))
                {
                    this.addTile(new StartTile(), i, j);
                }
                else {
                    this.addTile(new Tile(), i, j);
                }
            }
        }
    }
    
    /**
     * Checks if the tile is a double letter tile
     * 
     * @param DLTiles
     * @param x row index on board
     * @param y column index on board
     * @return true or false
     */
    private boolean isDLTile(int[][] DLTiles, int x, int y)
    {
        for (int i = 0; i<DLTiles.length; i++)
        {
            if (DLTiles[i][0] == x && DLTiles[i][1] == y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if tile is a double word tile
     * 
     * @param DWTiles
     * @param x row index on board
     * @param y column index on board
     * @return true or false
     */
    private boolean isDWTile(int[][] DWTiles, int x, int y)
    {
        for (int i = 0; i<DWTiles.length; i++)
        {
            if (DWTiles[i][0] == x && DWTiles[i][1] == y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if tile is a triple letter tile
     * 
     * @param TLTiles
     * @param x row index on board
     * @param y column index on board
     * @return true or false
     */
    private boolean isTLTile(int[][] TLTiles, int x, int y)
    {
        for (int i = 0; i<TLTiles.length; i++)
        {
            if (TLTiles[i][0] == x && TLTiles[i][1] == y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if tile is a triple word tile
     * 
     * @param TWTiles
     * @param x row index on board
     * @param y column index on board
     * @return true or false
     */
    private boolean isTWTile(int[][] TWTiles, int x, int y)
    {
        for (int i = 0; i<TWTiles.length; i++)
        {
            if (TWTiles[i][0] == x && TWTiles[i][1] == y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method checks if a coordinates x and y
     * are the starting tile.
     * 
     * @param x row index on board
     * @param y column index on board
     * @return boolean - true if position is start tile
     */
    private boolean isStartTile(int x, int y)
    {
        return (x == 7) && (y == 7);
    }

}
