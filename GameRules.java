package mancala;
import java.io.Serializable;
/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
 //Mancala game -> has gameRules -> has mancalaDataStruct

public abstract class GameRules implements Serializable{
    private static final long serialVersionUID = 1L;
    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private boolean moveInStore = false; // Added for free turn logic
    private boolean ifCapture = false;
    static final int LOW_PIT = 1;
    static final int HI_PIT = 12;
    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    public boolean isSideEmpty(final int pitNum) throws PitNotFoundException {
        if (pitNum < LOW_PIT || pitNum > HI_PIT){
            throw new PitNotFoundException();
        }else{
            final int pitIndex = pitNum - 1;
            if (pitIndex <= 6) {
                // Check pits 1-6 for emptiness
                for (int i = 0; i < 6; i++) {
                    if (gameBoard.getNumStones(i + 1) > 0){
                        return false; // Side is not empty
                    }
                }
            }else {
                // Check remaining pits on the same side for emptiness
                for (int i = 6; i < HI_PIT; i++) {
                    if (gameBoard.getNumStones(i + 1) > 0) {
                        return false; // Side is not empty
                    }
                }
            }
        return true;
        }
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    abstract int distributeStones(int startPit) throws PitNotFoundException;

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    abstract int captureStones(int stoppingPoint)throws PitNotFoundException;

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.
        final Player player1 = one;
        final Player player2 = two;

        final Store store1 = new Store();
        final Store store2 = new Store();

        player1.setStore(store1);
        player2.setStore(store2);

        store1.setOwner(one);
        store2.setOwner(two);

        gameBoard.setStore(store1, 1);
        gameBoard.setStore(store2, 2);
        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    //MY METHODS 
    /** 
    * Get the current free turn status.
     * @return True if a free turn is allowed, false otherwise.
     */
    public boolean getFreeTurn() {
        return moveInStore;
    }

    /**
     * Set the free turn status.
     * @param prevMove True if the previous move was a free turn, false otherwise.
     */
    public void setFreeTurn(final boolean prevMove) {
        moveInStore = prevMove;
    }

    public boolean getIfCapture() {
        return ifCapture;
    }

    /**
     * Set whether a capture has occurred.
     * @param ifCaptured True if a capture has occurred, false otherwise.
     */
    public void setIfCapture(final boolean ifCaptured) {
        ifCapture = ifCaptured;
    }

    @Override
    public String toString() {
    StringBuilder boardString = new StringBuilder();
        boardString.append("Store 1: ").append(gameBoard.getStoreCount(1)).append(" stones\n");
        boardString.append("Store 2: ").append(gameBoard.getStoreCount(2)).append(" stones\n");

        // Iterate over each pit and append its content to the string
        for (int pitNum = LOW_PIT; pitNum <= HI_PIT; pitNum++) {
            final int stonesInPit = getNumStones(pitNum);
            boardString.append("Pit ").append(pitNum).append(": ").append(stonesInPit).append(" stones\n");
        }
        
        return boardString.toString();
    }
}
