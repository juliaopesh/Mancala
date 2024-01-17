package mancala;

import java.io.Serializable;

public class InvalidMoveException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public InvalidMoveException() {
        super();
    }
    
    public InvalidMoveException(String message) {
        super(message);
    }
}