package com.mycompany.scrabble_project.data;

import java.util.*;

/**
 * This class represents a player that is part of a Scrabble game
 * It can be either a normal human player or an ai
 * 
 * the rack will hold a null value at the index where a letter was removed
 * 
 * @author Alin Caia & Asli Zeybek
 */
public abstract class Player {
    private List<Letter> rack;
    private int lastScore;
    private int totalScore;
    
    /**
     * Default Constructor
     */
    public Player(){
        this.rack = new ArrayList<>(7);
        for(int i = 0 ; i < 7 ; i++){
            this.rack.add(null);
        }
        this.lastScore = 0;
        this.totalScore = 0;
    }
    
    /**
     * Getter for the rack List of Letters
     * Uses deep-copy
     * 
     * @return the rack
     */
    public List<Letter> getRack()
    {
       return this.rack;
    }
    
    /**
     * Setter for the rack List of Letters
     * 
     * @param rack
     */
    public void setRack(List<Letter> rack)
    {
        this.rack = rack;
    }
    
    /**
     * Modified getter for the rack length
     * it subtracts all the indices containing null values
     * 
     * @return the length of the rack
     */
    public int getRackLength()
    {
        return 7 - countNullsInRack();
    }
    
    /**
     * Getter for the int field lastScore
     * 
     * @return the last score the player had
     */
    public int getLastScore()
    {
        return this.lastScore;
    }
    
    /**
     * Setter for the int field lastScore
     * 
     * @param lastScore
     */
    public void setLastScore(int lastScore)
    {
        this.lastScore = lastScore;
    }
    
    /**
     * Getter for the int field totalScore
     * 
     * @return the total score of the player
     */
    public int getTotalScore()
    {
        return this.totalScore;
    }
    
    /**
     * Setter for the int field totalScore
     * 
     * @param totalScore 
     */
    public void setTotalScore(int totalScore)
    {
        this.totalScore = totalScore;
    }
    
    /**
     * This method sums up a player's total score.
     * 
     * @param score 
     */
    public void sumTotalScore(int score)
    {
        this.totalScore += score;
    }
    
    /**
     * This method calculates the score for a word.
     * 
     * @param word 
     */
    public void calcWordPoints(Tile[] word)
    {
        int wordScore = 0;
        int totalWordMultip = calcWordMultip(word);
        for(Tile letter : word)
        {
            int letterValue = letter.getLetter().getValue();
            wordScore += letterValue*letter.getLetterMultip();
        }
        sumTotalScore(wordScore*totalWordMultip);
        //return wordScore*totalWordMultip;
    }
    
    /**
     * Sums up all the double/triple word tiles into one int
     * 
     * @param tiles
     * @return returns the total multip value
     */
    private int calcWordMultip(Tile[] tiles)
    {
        int totalWordMultip = 1;
        for(Tile tile : tiles)
        {
            totalWordMultip += tile.getWordMultip();
        }
        return totalWordMultip;
    }
    
    /**
     * This method adds a letter to the rack
     * It finds a place on the rack where a null value lies
     * 
     * @param letter 
     */
    private void addLetterToRack(Letter letter){
        int index = this.rack.indexOf(null);
        this.rack.set(index, letter);
    }
    
    /**
     * This method adds letters in bulk by
     * repeatedly calling addLetterToRack
     * 
     * @param letters 
     */
    public void addLettersToRack(List<Letter> letters){
        letters.forEach(this::addLetterToRack);
    }
    
    /**
     * This method removes a letter from the rack
     * It sets the letter's position to null once removed
     * 
     * @param rackPosition 
     * @return the letter that was removed
     */
    private Letter removeLetterFromRack(int rackPosition){
        Letter letter = this.rack.get(rackPosition);
        this.rack.set(rackPosition, null);
        return letter;
    }
    
    /**
     * This method removes letters in bulk by
     * repeatedly calling removeLetterFromRack
     * 
     * @param rackPositions
     * @return the list of letters that were removed
     */
    public List<Letter> removeLettersFromRack(int[] rackPositions){
        List<Letter> letters = new ArrayList<>(rackPositions.length);
        for(int i = 0; i < rackPositions.length; i++){
            letters.add(removeLetterFromRack(rackPositions[i]));
        }
        return letters;
    }
    
    /**
     * This method counts all the null values in the rack List
     * 
     * @return the number of null values
     */
    public int countNullsInRack(){
        int count = 0;
        for(Letter letter : this.rack){
            if(letter == null){
                count++;
            }
        }
        return count;
    }
    
    /**
     * This method finds all the indices in the rack List
     * that don't contain a null value
     * 
     * @return all the indices on the rack that weren't null
     */
    public int[] findNonNullIndices(){
        int[] nonNullIndices = new int[getRackLength()];
        int count = 0;
        for(Letter letter : this.rack){
            if(letter != null){
                nonNullIndices[count] = this.rack.indexOf(letter);
                count++;
            }
        }
        return nonNullIndices;
    }
}
