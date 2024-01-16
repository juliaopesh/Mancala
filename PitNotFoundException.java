package mancala; 

import java.io.Serializable;

public class PitNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public PitNotFoundException() {
        super("Pit not found.");
    }
    //second constructor allows to specify a custom message
    public PitNotFoundException(String message){
        super("Pit not found: " + message);
    }
}