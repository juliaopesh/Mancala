package mancala;

import java.io.Serializable;

public class GameNotOverException extends Exception implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public GameNotOverException() {
        super();
    }
    
    public GameNotOverException(String message) {
        super(message);
    }
}