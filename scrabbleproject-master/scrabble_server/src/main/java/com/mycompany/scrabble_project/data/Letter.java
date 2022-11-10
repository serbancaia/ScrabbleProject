package com.mycompany.scrabble_project.data;

import java.util.Objects;

/**
 * This class is used to represent a letter that a
 * Scrabble game would normally use
 * 
 * @author Alin Caia
 */
public class Letter {
    private char letter;
    private int value;
    
    /**
     * Default Constructor
     * 
     * @param letter a character ((between A and Z) or *)
     */
    public Letter(char letter){
        this.letter = letter;
        this.value = checkLetterValue(letter);
    }

    /**
     * Getter for the char field letter
     * 
     * @return the letter
     */
    public char getLetter()
    {
        return this.letter;
    }
    
    /**
     * Setter for the char field letter
     * 
     * @param letter a character ((between A and Z) or *)
     */
    private void setLetter(char letter)
    {
        this.letter = letter;
    }
    
    /**
     * Getter for the int field value
     * 
     * @return the value of the letter
     */
    public int getValue()
    {
        return this.value;
    }
    
    /**
     * Setter for the int field value
     * 
     * @param value an int (see checkLetterValue method)
     */
    private void setValue(int value)
    {
        this.value = value;
    }
    
    /**
     * Checks what the value of the selected letter is
     * 
     * @param letter a character ((between A and Z) or *)
     * @return the value of the letter selected
     */
    private int checkLetterValue(char letter){
        switch (letter) {
            case 'A':
            case 'E':
            case 'I':
            case 'L':
            case 'N':
            case 'O':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
                return 1;
            case 'D':
            case 'G':
                return 2;
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J':
            case 'X':
                return 8;
            case 'Q':
            case 'Z':
                return 10;
            default:
                return 0;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Letter other = (Letter) obj;
        if(!Objects.equals(this.letter, other.letter)){
            return false;
        }
        if(!Objects.equals(this.value, other.value)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.letter;
        hash = 43 * hash + this.value;
        return hash;
    }
}
